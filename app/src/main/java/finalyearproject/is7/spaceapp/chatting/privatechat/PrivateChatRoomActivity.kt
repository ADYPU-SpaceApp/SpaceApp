package finalyearproject.is7.spaceapp.chatting.privatechat

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import finalyearproject.is7.spaceapp.R
import finalyearproject.is7.spaceapp.User
import finalyearproject.is7.spaceapp.UserAdapter
import finalyearproject.is7.spaceapp.user.UserProfileActivity

class PrivateChatRoomActivity:AppCompatActivity() {

    private lateinit var mainScreenUserImage: ImageView
    private lateinit var grpButton: Button
    private lateinit var addFriendBtn: Button
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter: UserAdapter

    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = FirebaseFirestore.getInstance()
    private lateinit var mDbRef: DatabaseReference

    private lateinit var orgId: String
    private var org: DocumentReference? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_chat_room)

        userList = ArrayList()
        userRecyclerView = findViewById(R.id.userRecyclerView)

//        grpButton = findViewById(R.id.grpBtn)
        addFriendBtn = findViewById(R.id.addFriendButton)

        orgId = intent.getStringExtra("orgId").toString()
        org = mDb.collection("Organisation").document(orgId)
        mDbRef = Firebase.database.getReference(orgId)

        mainScreenUserImage = findViewById(R.id.MainScreenUserImage)

        mDb.collection("User").document(mAuth.currentUser?.uid!!).get()
            .addOnSuccessListener {
                val imageUri = Uri.parse(it.data?.get("displaypic").toString())
                Log.d("CheckMe", "Image URI: $imageUri")
                if (imageUri != Uri.EMPTY) {
                    Glide.with(this).load(imageUri).circleCrop().into(mainScreenUserImage)
                } else {
                    Glide.with(this).load(R.drawable.profile).circleCrop().into(mainScreenUserImage)
                }
            }

//        grpButton.setOnClickListener {
//            val intent = Intent(this, GroupActivity::class.java)
//            intent.putExtra("orgId", orgId)
//            startActivity(intent)
//        }

        addFriendBtn.setOnClickListener{
            val intent = Intent(this, AddChatActivity::class.java)
            intent.putExtra("orgId", orgId)
            startActivity(intent)
        }

        mainScreenUserImage.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        Log.d("CheckMe", "Org: $orgId")
        userAdapter = UserAdapter(this,userList, orgId)

        userRecyclerView.layoutManager = LinearLayoutManager(this)

        mDb.collection("User").whereEqualTo("org",orgId).get()
            .addOnSuccessListener { users ->
                userList.clear()
                for (user in users) {
                    if (user.id != mAuth.currentUser!!.uid) {
                        val chatRoomCode = mAuth.currentUser!!.uid + user.id
                        Log.d("CheckMe", "ChatRoomCode: $chatRoomCode")
                        mDbRef.child("chats").child(chatRoomCode).get()
                            .addOnSuccessListener {
                                if (it.exists()) {
                                    Log.d("CheckMe", "ChatRoom Exists")
                                    val u = User(
                                        user.data["email"].toString(),
                                        user.data["name"].toString(),
                                        user.id,
                                        user.data["displaypic"].toString()
                                    )
                                    userList.add(u)
                                    userAdapter.notifyDataSetChanged()
                                }
                            }

                    }
                }
                userRecyclerView.adapter = userAdapter


            }
            .addOnFailureListener { e ->
                Log.d("Jaineel",e.toString())
            }

        userRecyclerView.adapter = UserAdapter(this, userList, orgId)

    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }

}