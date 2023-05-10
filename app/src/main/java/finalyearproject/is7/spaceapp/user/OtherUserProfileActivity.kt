package finalyearproject.is7.spaceapp.user

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.R
import finalyearproject.is7.spaceapp.databinding.ActivityOtherUserProfileBinding

class OtherUserProfileActivity: AppCompatActivity() {

    private lateinit var binding: ActivityOtherUserProfileBinding

    private var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = intent.getStringExtra("otherUserUID")!!

        val u = db.collection("User").document(uid)
        u.get()
            .addOnSuccessListener { documents ->
                if (documents != null) {
                    if (documents.data?.get("displaypic") != "") {
                        val imageUri = Uri.parse(documents.data?.get("displaypic").toString())
                        Log.d("CheckMe", "Image URI: $imageUri")
                        if (imageUri != null) {
                            Glide.with(this).load(imageUri).circleCrop().into(binding.imgOtherUserProfileDisplayPic)
                        } else {
                            Glide.with(this).load(R.mipmap.ic_launcher_round).circleCrop()
                                .into(binding.imgOtherUserProfileDisplayPic)
                        }
                    }

                    binding.txtOtherUserProfileName.text = documents.data?.get("name") as CharSequence?

                    if (documents.data?.get("communityAdmin") != null) {
                        val r = "I'm admin of " + documents.data?.get("communityAdmin")
                        binding.txtOtherUserProfileCommunity.text = r
                    } else {
                        binding.txtOtherUserProfileCommunity.text = documents.data?.get("email") as CharSequence?
                    }

//                  if course exist in database, then get the course name
                    if (documents.data?.get("course") != null) {
                        // Student
                        val r = "I'm " + documents.data?.get("role") + " at " + documents.data?.get("department") + " for " + documents.data?.get("course")
                        binding.txtOtherUserProfileRole.text = r
                    } else {
                        // Staff
                        val r = "I'm " + documents.data?.get("role") + " in " + documents.data?.get("department")
                        binding.txtOtherUserProfileRole.text = r
                    }
                }
            }

    }

}