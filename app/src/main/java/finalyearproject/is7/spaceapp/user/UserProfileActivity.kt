package finalyearproject.is7.spaceapp.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.ChangePasswordActivity
import finalyearproject.is7.spaceapp.LoginActivity
import finalyearproject.is7.spaceapp.R
import finalyearproject.is7.spaceapp.databinding.ActivityUserProfileBinding
import finalyearproject.is7.spaceapp.displaypic.DisplayPicActivity

class UserProfileActivity:AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    private var mAuth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the current user detail
        val uid = mAuth.currentUser?.uid!!
        val u = db.collection("User").document(uid)
        u.get()
            .addOnSuccessListener { documents ->
                if (documents != null) {
                    if (documents.data?.get("displaypic") != "") {
                        val imageUri = Uri.parse(documents.data?.get("displaypic").toString())
                        Log.d("CheckMe", "Image URI: $imageUri")
                        if (imageUri != null) {
                            Glide.with(this).load(imageUri).circleCrop().into(binding.imgProfileDisplayPic)
                        } else {
                            Glide.with(this).load(R.mipmap.ic_launcher_round).circleCrop()
                                .into(binding.imgProfileDisplayPic)
                        }
                    }

                    binding.imgProfileDisplayPic.setOnClickListener {
                        val displaypicintent = Intent(this, DisplayPicActivity::class.java)
                        displaypicintent.putExtra("is", "User")
                        startActivity(displaypicintent)
                    }

                    binding.txtUserProfileName.text = documents.data?.get("name") as CharSequence?

                    if (documents.data?.get("communityAdmin") != null) {
                        val r = "I'm admin of " + documents.data?.get("communityAdmin")
                        binding.txtUserProfileCommunity.text = r
                    } else {
                        binding.txtUserProfileCommunity.text = mAuth.currentUser!!.email
                    }
                    
//                  if course exist in database, then get the course name
                    if (documents.data?.get("course") != null) {
                        // Student
                        val r = "I'm " + documents.data?.get("role") + " at " + documents.data?.get("department") + " for " + documents.data?.get("course")
                        binding.txtUserProfileRole.text = r
                    } else {
                        // Staff
                        val r = "I'm " + documents.data?.get("role") + " in " + documents.data?.get("department")
                        binding.txtUserProfileRole.text = r
                    }
                }
            }


        binding.btnUserProfileChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        binding.btnUserProfileLogout.setOnClickListener{
            mAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnUserProfileBackUser.setOnClickListener {
            finish()
        }
    }

    override fun onRestart() {
        super.onRestart()
        val uid = mAuth.currentUser?.uid!!
        val u = db.collection("User").document(uid)
        u.get()
            .addOnSuccessListener { documents ->
                if (documents != null) {
                    if (documents.data?.get("displaypic") != "") {
                        val imageUri = Uri.parse(documents.data?.get("displaypic").toString())
                        Log.d("CheckMe", "Image URI: $imageUri")
                        if (imageUri != null) {
                            Glide.with(this).load(imageUri).circleCrop().into(binding.imgProfileDisplayPic)
                        } else {
                            Glide.with(this).load(R.mipmap.ic_launcher_round).circleCrop().into(binding.imgProfileDisplayPic)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Jaineel", "get failed with ", exception)
            }

    }

}