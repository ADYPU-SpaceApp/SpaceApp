package finalyearproject.is7.spaceapp.org

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.LoginActivity
import finalyearproject.is7.spaceapp.R
import finalyearproject.is7.spaceapp.create.*

class OrgMainActivity:AppCompatActivity() {

    private lateinit var orgLogo: ImageView
    private lateinit var orgName: TextView
    private lateinit var profileButton: Button
    private lateinit var createUserBtn: Button
    private lateinit var createStudentBtn: Button
    private lateinit var createNoticeBtn: Button
    private lateinit var createDepartmentBtn: Button
    private lateinit var createCourseBtn: Button
    private lateinit var createClassBtn: Button
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
        createStudentBtn = findViewById(R.id.createStudentButton)
        createNoticeBtn = findViewById(R.id.communityButton)
        createDepartmentBtn = findViewById(R.id.createDepartmentButton)
        createCourseBtn = findViewById(R.id.createCourseButton)
        createClassBtn = findViewById(R.id.createClassButton)
        logoutButton = findViewById(R.id.logoutButton)

        setup()

        profileButton.setOnClickListener {
            startActivity(Intent(this, OrgProfileActivity::class.java))
        }

        createUserBtn.setOnClickListener {
            val goToCreateStaffUserActivityIntent = Intent(this, CreateStaffUserActivity::class.java)
            goToCreateStaffUserActivityIntent.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateStaffUserActivityIntent)
        }

        createStudentBtn.setOnClickListener {
            val goToCreateStudentUserActivityIntent = Intent(this, CreateNonStaffUserActivity::class.java)
            goToCreateStudentUserActivityIntent.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateStudentUserActivityIntent)
        }

        createNoticeBtn.setOnClickListener {
            val goToCreateNoticeActivity = Intent(this, CreateNoticeActivity::class.java)
            goToCreateNoticeActivity.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateNoticeActivity)
        }

        createDepartmentBtn.setOnClickListener {
            val goToCreateDepartmentActivity = Intent(this, CreateDepartmentActivity::class.java)
            goToCreateDepartmentActivity.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateDepartmentActivity)
        }

        createCourseBtn.setOnClickListener {
            val goToCreateCourseActivity = Intent(this, CreateCourseActivity::class.java)
            goToCreateCourseActivity.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateCourseActivity)
        }

        createClassBtn.setOnClickListener {
            val goToCreateClassActivity = Intent(this, CreateClassActivity::class.java)
            goToCreateClassActivity.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateClassActivity)
        }

        logoutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        setup()
    }

    private fun setup() {
        mDb.collection("Organisation").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { org ->
                if (org.exists()) {
                    if (org.data?.get("displaypic") != "") {
                        Glide.with(this).load(org.data?.get("displaypic")).circleCrop().into(orgLogo)
                    }
                    val organisationName = "Welcome " + org.data?.get("orgName")
                    orgName.text = organisationName
                }
            }
    }

}
