package finalyearproject.is7.spaceapp.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.docscenter.DocumentCenterActivity
import finalyearproject.is7.spaceapp.chatting.privatechat.PrivateChatRoomActivity
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
            .addOnSuccessListener {
                Log.d("UserMainActivity", "User Document: ${it.data}")
                orgId = it["org"] as String
//                val role = it["role"] as String
//                mDb.collection("Role").document(role).get().addOn
            }

        binding.settingsButton.setOnClickListener {
            val goToUserSettingsActivityIntent = Intent(this, UserSettingsActivity::class.java)
            startActivity(goToUserSettingsActivityIntent)
        }

        binding.profileButton.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        binding.communityButton.setOnClickListener {
//            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
            val goToCommunityActivityIntent = Intent(this, CommunityActivity::class.java)
            goToCommunityActivityIntent.putExtra("orgId", orgId)
            startActivity(goToCommunityActivityIntent)
        }

        binding.viewNoticeButton.setOnClickListener {
            val goToDocumentCenterActivityIntent = Intent(this, DocumentCenterActivity::class.java)
            goToDocumentCenterActivityIntent.putExtra("orgId", orgId)
            startActivity(goToDocumentCenterActivityIntent)
        }

        binding.openChatRoomButton.setOnClickListener {
            val goToPrivateChatRoomActivityIntent =
                Intent(this, PrivateChatRoomActivity::class.java)
            goToPrivateChatRoomActivityIntent.putExtra("orgId", orgId)
            startActivity(goToPrivateChatRoomActivityIntent)
        }

    }
}