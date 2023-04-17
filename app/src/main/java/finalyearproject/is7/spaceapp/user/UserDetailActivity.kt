package finalyearproject.is7.spaceapp.user

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.R
import finalyearproject.is7.spaceapp.databinding.ActivityUserDetailBinding

class UserDetailActivity:AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val uid =  intent.getStringExtra("uid")
        val displaypic = intent.getStringExtra("displaypic")

        binding.UserNameText.text = name

        binding.UserBackButton.setOnClickListener {
            finish()
        }

        if (displaypic != "") {
            Glide.with(this).load(displaypic).into(binding.UserDisplayPic)
        }
        else {
            Glide.with(this).load(R.drawable.profile).into(binding.UserDisplayPic)
        }

        mDb.collection("User").document(uid!!).get()
            .addOnSuccessListener {
                binding.UserEmailText.text = it.getString("email")
                binding.UserRoleText.text = it.getString("role")
                val orgId = it.getString("org")
                mDb.collection("Organisation").document(orgId!!).get()
                    .addOnSuccessListener { org ->
                        binding.UserOrganisationText.text = org.getString("orgName")
                    }

                if (it.getBoolean("is_Active") == true) {
                    binding.deleteUserButton.text = "Deactivate User"
                    binding.deleteUserButton.setOnClickListener {
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
                    binding.deleteUserButton.text = "Activate User"
                    binding.deleteUserButton.setOnClickListener {
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
