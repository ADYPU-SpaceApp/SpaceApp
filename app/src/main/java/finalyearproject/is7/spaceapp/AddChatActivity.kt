package finalyearproject.is7.spaceapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import finalyearproject.is7.spaceapp.databinding.ActivityAddChatBinding

class AddChatActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddChatBinding
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter: UserAdapter

    private lateinit var orgId: String

    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = FirebaseFirestore.getInstance()
    private lateinit var mDbRef: DatabaseReference

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orgId = intent.getStringExtra("orgId").toString()

        mDbRef = Firebase.database.getReference(orgId)

        userList = ArrayList()
        userAdapter = UserAdapter(this, userList, orgId)
        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.userRecyclerView.adapter = userAdapter


        mDb.collection("User").whereEqualTo("org",orgId).get()
            .addOnSuccessListener { users ->
                userList.clear()
                for (user in users) {
                    if (user.id != mAuth.currentUser?.uid) {
                        val u = User(
                            user.data["email"].toString(),
                            user.data["name"].toString(),
                            user.id,
                            user.data["displaypic"].toString()
                        )
                        userList.add(u)
                    }
                }
                userAdapter.notifyDataSetChanged()
            }
    }

}
