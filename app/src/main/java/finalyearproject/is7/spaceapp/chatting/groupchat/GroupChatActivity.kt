package finalyearproject.is7.spaceapp.chatting.groupchat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.R
import finalyearproject.is7.spaceapp.databinding.ActivityGroupChatBinding
import finalyearproject.is7.spaceapp.user.UserProfileActivity

class GroupChatActivity: AppCompatActivity() {

    private lateinit var binding: ActivityGroupChatBinding

    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orgId = intent.getStringExtra("orgId").toString()

        // Add User Dp
        mDb.collection("User").document(mAuth.currentUser?.uid!!).get()
            .addOnSuccessListener {
                val imageUri = Uri.parse(it.data?.get("displaypic").toString())
                Log.d("CheckMe", "Image URI: $imageUri")
                if (imageUri != Uri.EMPTY) {
                    Glide.with(this).load(imageUri).circleCrop().into(binding.MainScreenUserImageGrpChat)
                } else {
                    Glide.with(this).load(R.drawable.profile).circleCrop().into(binding.MainScreenUserImageGrpChat)
                }
            }

        // List All Groups


        binding.MainScreenUserImageGrpChat.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }



    }

}