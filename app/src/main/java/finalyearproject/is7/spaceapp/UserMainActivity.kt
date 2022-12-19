package finalyearproject.is7.spaceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UserMainActivity : AppCompatActivity() {

    private lateinit var orgId: String

    private lateinit var profileButton: Button
    private lateinit var createUserBtn: Button
    private lateinit var communityBtn: Button
    private lateinit var viewNoticeButton: Button
    private lateinit var openChatRoomButton: Button

    private val mAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)

        profileButton = findViewById(R.id.profileButton)
        createUserBtn = findViewById(R.id.createUserButton)
        communityBtn = findViewById(R.id.communityButton)
        viewNoticeButton = findViewById(R.id.viewNoticeButton)
        openChatRoomButton = findViewById(R.id.openChatRoomButton)

        roleRestrictedPart()

        profileButton.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        communityBtn.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        viewNoticeButton.setOnClickListener {
            val goToNoticeBoardActivityIntent = Intent(this, NoticeBoardActivity::class.java)
            goToNoticeBoardActivityIntent.putExtra("orgId", orgId)
            startActivity(goToNoticeBoardActivityIntent)
        }

        openChatRoomButton.setOnClickListener {
            val goToChatRoomActivityIntent = Intent(this, ChatRoomActivity::class.java)
            goToChatRoomActivityIntent.putExtra("orgId", orgId)
            startActivity(goToChatRoomActivityIntent)
        }

    }

    override fun onRestart() {
        super.onRestart()
        roleRestrictedPart()
    }

    private fun roleRestrictedPart() {
        mDb.collection("User").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { user ->
                val org = user.data?.get("org") as DocumentReference
                org.get()
                    .addOnSuccessListener { o ->
                        if (o.exists()) {
                            orgId = o.id
                        }
                    }
                val role = user.data?.get("role") as DocumentReference
                role.get()
                    .addOnSuccessListener { r ->
                        if (r.data?.get("is_Staff") == true) {
                            createUserBtn.setOnClickListener {
                                val goToCreateUserActivityIntent =
                                    Intent(this, CreateUserActivity::class.java)
                                goToCreateUserActivityIntent.putExtra("orgId", orgId)
                                startActivity(goToCreateUserActivityIntent)
                            }
                        }
                        else {
                            createUserBtn.setOnClickListener {
                                Toast.makeText(this,"Sorry ur not a staff member",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
    }

}