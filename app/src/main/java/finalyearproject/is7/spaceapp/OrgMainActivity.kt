package finalyearproject.is7.spaceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrgMainActivity:AppCompatActivity() {

    private lateinit var orgLogo: ImageView
    private lateinit var orgName: TextView
    private lateinit var profileButton: Button
    private lateinit var createUserBtn: Button
    private lateinit var createNoticeBtn: Button
    private lateinit var logoutButton: Button

    private val mAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_org_main)

        orgLogo = findViewById(R.id.orgLogo)
        orgName = findViewById(R.id.orgName)
        profileButton = findViewById(R.id.profileButton)
        createUserBtn = findViewById(R.id.createUserButton)
        createNoticeBtn = findViewById(R.id.createNoticeButton)
        logoutButton = findViewById(R.id.logoutButton)

        if (mAuth.currentUser != null) {
            mDb.collection("Organisation").document(mAuth.currentUser!!.uid).get()
                .addOnSuccessListener { org ->
                    if (org.exists()) {
                        if (org.data?.get("displaypic") != null) {
                            Glide.with(this).load(org.data?.get("displaypic")).circleCrop().into(orgLogo)
                        }
                        val organisationName = "Welcome " + org.data?.get("orgName")
                        orgName.text = organisationName
                    }
                }
        }

        profileButton.setOnClickListener {
            startActivity(Intent(this, OrgProfileActivity::class.java))
        }

        createUserBtn.setOnClickListener {
            val goToCreateUserActivityIntent = Intent(this, CreateUserActivity::class.java)
            goToCreateUserActivityIntent.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateUserActivityIntent)
        }

        createNoticeBtn.setOnClickListener {
            val goToCreateNoticeActivity = Intent(this, CreateNoticeActivity::class.java)
            goToCreateNoticeActivity.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateNoticeActivity)

        }

        logoutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }

}
