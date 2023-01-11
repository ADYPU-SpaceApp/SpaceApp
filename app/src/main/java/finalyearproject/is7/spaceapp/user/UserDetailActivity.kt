package finalyearproject.is7.spaceapp.user

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.R

class UserDetailActivity:AppCompatActivity() {

    private lateinit var userDisplayPic: ImageView
    private lateinit var userNameText: TextView
    private lateinit var userEmailText: TextView
    private lateinit var userRoleText: TextView
    private lateinit var userOrgText: TextView
    private lateinit var deleteUserButton: Button

    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        userDisplayPic = findViewById(R.id.UserDisplayPic)
        userNameText = findViewById(R.id.UserNameText)
        userEmailText = findViewById(R.id.UserEmailText)
        userRoleText = findViewById(R.id.UserRoleText)
        userOrgText = findViewById(R.id.UserOrganisationText)
        deleteUserButton = findViewById(R.id.deleteUserButton)

        val name = intent.getStringExtra("name")
        val uid =  intent.getStringExtra("uid")
        val displaypic = intent.getStringExtra("displaypic")

        userNameText.text = name

        if (displaypic != "") {
            Glide.with(this).load(displaypic).into(userDisplayPic)
        }
        else {
            Glide.with(this).load(R.drawable.profile).into(userDisplayPic)
        }

        mDb.collection("User").document(uid!!).get()
            .addOnSuccessListener {
                userEmailText.text = it.getString("email")
                userRoleText.text = it.getString("role")
                val orgId = it.getString("org")
                mDb.collection("Organisation").document(orgId!!).get()
                    .addOnSuccessListener { org ->
                        userOrgText.text = org.getString("orgName")
                    }

                if (it.getBoolean("is_Active") == true) {
                    deleteUserButton.text = "Deactivate User"
                    deleteUserButton.setOnClickListener {
                        AlertDialog.Builder(this)
                            .setTitle("Delete Notice")
                            .setMessage("Are you sure you want to delete this notice?")
                            .setPositiveButton("Yes") { _, _ ->
                                mDb.collection("User").document(uid).update("is_Active", false)
                                mDb.collection("User").document(uid).update("deactivation_reason", "User was deleted by ${mAuth.currentUser?.email} on ${System.currentTimeMillis()}")
                                finish()
                            }
                            .setNegativeButton("No") { _, _ -> }
                            .show()

                    }
                }
                else {
                    deleteUserButton.text = "Activate User"
                    deleteUserButton.setOnClickListener {
                        mDb.collection("User").document(uid).update("is_Active", true)
                        // remove deactivation reason field in document
                        mDb.collection("User").document(uid)
                            .update("deactivation_reason", null)
                        finish()
                    }
                }


            }



    }

}
