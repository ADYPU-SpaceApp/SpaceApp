package finalyearproject.is7.spaceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UserMainActivity : AppCompatActivity() {

    private lateinit var orgId: String

    private lateinit var orgLogo: ImageView
    private lateinit var orgName: TextView
    private lateinit var profileButton: Button
    private lateinit var createUserBtn: Button
    private lateinit var createNoticeBtn: Button
    private lateinit var viewNoticeButton: Button
    private lateinit var openChatRoomButton: Button
    private lateinit var logoutButton: Button

    private val mAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)

        orgLogo = findViewById(R.id.orgLogo)
        orgName = findViewById(R.id.orgName)
        profileButton = findViewById(R.id.profileButton)
        createUserBtn = findViewById(R.id.createUserButton)
        createNoticeBtn = findViewById(R.id.createNoticeButton)
        viewNoticeButton = findViewById(R.id.viewNoticeButton)
        openChatRoomButton = findViewById(R.id.openChatRoomButton)
        logoutButton = findViewById(R.id.logoutButton)

        if (mAuth.currentUser != null) {
            mDb.collection("User").document(mAuth.currentUser!!.uid).get()
                .addOnSuccessListener { user ->
                    val org = user.data?.get("org") as DocumentReference
                    org.get()
                        .addOnSuccessListener { o ->
                            if (o.exists()) {
                                orgId = o.id
                                if (o.data?.get("displaypic") != null) {
                                    Glide.with(this).load(o.data?.get("displaypic")).circleCrop().into(orgLogo)
                                }
                                val organisationName = "Welcome " + user.data!!["name"] + " to " + o.data?.get("orgName")
                                orgName.text = organisationName
                            }
                        }
                    val role = user.data?.get("role") as DocumentReference
                    role.get()
                        .addOnSuccessListener { r ->
                            if (r.data?.get("is_Staff") == false) {
                                createUserBtn.visibility = Button.GONE
                                createNoticeBtn.visibility = Button.GONE
                            }
                        }
                }
        }

        profileButton.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        createUserBtn.setOnClickListener {
            val goToCreateUserActivityIntent = Intent(this, CreateUserActivity::class.java)
            goToCreateUserActivityIntent.putExtra("orgId", orgId)
            startActivity(goToCreateUserActivityIntent)
        }

        createNoticeBtn.setOnClickListener {
            val goToCreateNoticeActivityIntent = Intent(this, CreateNoticeActivity::class.java)
            goToCreateNoticeActivityIntent.putExtra("orgId", orgId)
            startActivity(goToCreateNoticeActivityIntent)
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

        logoutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }
}