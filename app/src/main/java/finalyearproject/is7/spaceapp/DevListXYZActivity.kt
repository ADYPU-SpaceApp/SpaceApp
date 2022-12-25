package finalyearproject.is7.spaceapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DevListXYZActivity: AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter: UserAdapter
    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chat)

        userRecyclerView = findViewById(R.id.userRecyclerView)

        val xyz = intent.getStringExtra("xyz")

        userList = ArrayList()

        if (xyz == "User") {
            listUsers()
        }
        else if (xyz == "Org") {
            listOrg()
        }
    }

    override fun onRestart() {
        super.onRestart()
        // Refresh the list
        val xyz = intent.getStringExtra("xyz")
        if (xyz == "User") {
            listUsers()
        }
        else if (xyz == "Org") {
            listOrg()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listUsers() {
        userAdapter = UserAdapter(this, userList, "")
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = userAdapter

        mDb.collection("User").get()
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

    @SuppressLint("NotifyDataSetChanged")
    private fun listOrg() {
        userAdapter = UserAdapter(this, userList, "org")
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = userAdapter

        mDb.collection("Organisation").get()
            .addOnSuccessListener { orgs ->
                userList.clear()
                for (org in orgs) {
                    val u = User(
                        org.data["email"].toString(),
                        org.data["orgName"].toString(),
                        org.id,
                        org.data["displaypic"].toString()
                    )
                    userList.add(u)
                }
                userAdapter.notifyDataSetChanged()
            }
    }
}
