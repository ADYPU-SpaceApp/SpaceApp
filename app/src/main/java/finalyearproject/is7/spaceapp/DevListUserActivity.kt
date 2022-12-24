package finalyearproject.is7.spaceapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DevListUserActivity: AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter: UserAdapter
    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_dev_list_user)
        setContentView(R.layout.activity_add_chat)

        userRecyclerView = findViewById(R.id.userRecyclerView)

        userList = ArrayList()
        userAdapter = UserAdapter(this, userList, "")
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = userAdapter

        listUsers()
    }

    override fun onRestart() {
        super.onRestart()
        listUsers()
    }

    private fun listUsers() {
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
}
