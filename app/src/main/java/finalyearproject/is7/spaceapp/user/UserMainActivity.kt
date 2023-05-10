package finalyearproject.is7.spaceapp.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.R
import finalyearproject.is7.spaceapp.docscenter.DocumentCenterActivity
import finalyearproject.is7.spaceapp.chatting.privatechat.PrivateChatActivity
import finalyearproject.is7.spaceapp.community.CommunityActivity
import finalyearproject.is7.spaceapp.databinding.ActivityUserMainBinding

class UserMainActivity : AppCompatActivity() {

    private lateinit var orgId: String

    private lateinit var binding: ActivityUserMainBinding

    private val mAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDb.collection("User").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { it ->
                Log.d("UserMainActivity", "User Document: ${it.data}")

//                val gson = Gson()
//                val json = gson.toJson(it.data)

                orgId = it["org"] as String
//                val role = it["role"] as String

                if (it.data?.get("displaypic") != "") {
                    val imageUri = Uri.parse(it.data?.get("displaypic").toString())
                    Log.d("CheckMe", "Image URI: $imageUri")
                    if (imageUri != null) {
                        Glide.with(this).load(imageUri).circleCrop().into(binding.imgUserDp)
                    } else {
                        Glide.with(this).load(R.mipmap.ic_launcher_round).circleCrop().into(binding.imgUserDp)
                    }
                }

                val hi = "Hi, " + it["name"]
                binding.txtUserName.text = hi

                if (orgId.isNotEmpty() || orgId.isNotBlank() || orgId != "") {
                    mDb.collection("Organisation").document(orgId).get()
                        .addOnSuccessListener {
                            Log.d("UserMainActivity", "Org Document: ${it.data}")
                            binding.txtGreetingOrgName.text = it["orgName"] as String
                        }
                }

            }



        binding.btnNotification.setOnClickListener {
            val goToUserSettingsActivityIntent = Intent(this, UserSettingsActivity::class.java)
            goToUserSettingsActivityIntent.putExtra("orgId", orgId)
            startActivity(goToUserSettingsActivityIntent)
        }

        binding.imgUserDp.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        binding.btnChatModule.setOnClickListener {
            val goToPrivateChatActivityIntent =
                Intent(this, PrivateChatActivity::class.java)
            goToPrivateChatActivityIntent.putExtra("orgId", orgId)
            startActivity(goToPrivateChatActivityIntent)
        }

        binding.btnDocsCenterModule.setOnClickListener {
            val goToDocumentCenterActivityIntent = Intent(this, DocumentCenterActivity::class.java)
            goToDocumentCenterActivityIntent.putExtra("orgId", orgId)
            startActivity(goToDocumentCenterActivityIntent)
        }

        binding.btnCommunityModule.setOnClickListener {
            val goToCommunityActivityIntent = Intent(this, CommunityActivity::class.java)
            goToCommunityActivityIntent.putExtra("orgId", orgId)
            startActivity(goToCommunityActivityIntent)
        }





    }
}