package finalyearproject.is7.spaceapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ChatRoomActivity:AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter: UserAdapter

    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = FirebaseFirestore.getInstance()

    private lateinit var orgId: String
    private var org: DocumentReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        userList = ArrayList()
        userRecyclerView = findViewById(R.id.userRecyclerView)

        orgId = intent.getStringExtra("orgId").toString()
        org = mDb.collection("Organisation").document(orgId)

        userAdapter = UserAdapter(this,userList, orgId)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
//        userRecyclerView.adapter = userAdapter

        mDb.collection("User").whereEqualTo("org",org).get()
            .addOnSuccessListener { users ->
                userList.clear()
                for (user in users) {
                    if (user.id != mAuth.currentUser!!.uid) {
                        userList.add(
                            User(
                                user.data["email"] as String,
                                user.data["name"] as String,
                                user.id
                            )
                        )
                    }

                }
                // update adapter with latest userList
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