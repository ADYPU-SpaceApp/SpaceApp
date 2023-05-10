package finalyearproject.is7.spaceapp.org

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.LoginActivity
import finalyearproject.is7.spaceapp.community.CreateCommunityActivity
import finalyearproject.is7.spaceapp.create.*
import finalyearproject.is7.spaceapp.databinding.ActivityOrgMainBinding
import finalyearproject.is7.spaceapp.docscenter.notice.CreateNoticePdfActivity

class OrgMainActivity:AppCompatActivity() {

    private lateinit var binding: ActivityOrgMainBinding

    private val mAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrgMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()

        binding.profileButton.setOnClickListener {
            startActivity(Intent(this, OrgProfileActivity::class.java))
        }

        binding.createUserButton.setOnClickListener {
            val goToCreateStaffUserActivityIntent = Intent(this, CreateStaffUserActivity::class.java)
            goToCreateStaffUserActivityIntent.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateStaffUserActivityIntent)
        }

        binding.createStudentButton.setOnClickListener {
            val goToCreateStudentUserActivityIntent = Intent(this, CreateNonStaffUserActivity::class.java)
            goToCreateStudentUserActivityIntent.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateStudentUserActivityIntent)
        }

        binding.communityButton.setOnClickListener {
            val goToCreateNoticePdfActivity = Intent(this, CreateNoticePdfActivity::class.java)
            goToCreateNoticePdfActivity.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateNoticePdfActivity)
        }

        binding.createDepartmentButton.setOnClickListener {
            val goToCreateDepartmentActivity = Intent(this, CreateDepartmentActivity::class.java)
            goToCreateDepartmentActivity.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateDepartmentActivity)
        }

        binding.createCourseButton.setOnClickListener {
            val goToCreateCourseActivity = Intent(this, CreateCourseActivity::class.java)
            goToCreateCourseActivity.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateCourseActivity)
        }

        binding.createClassButton.setOnClickListener {
            val goToCreateBatchActivity = Intent(this, CreateBatchActivity::class.java)
            goToCreateBatchActivity.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateBatchActivity)
        }

        binding.createCommunityButton.setOnClickListener {
            val goToCreateCommunityActivity = Intent(this, CreateCommunityActivity::class.java)
            goToCreateCommunityActivity.putExtra("orgId", mAuth.currentUser!!.uid)
            startActivity(goToCreateCommunityActivity)
        }

        binding.btnUserProfileLogout.setOnClickListener {
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
                        Glide.with(this).load(org.data?.get("displaypic")).circleCrop().into(binding.orgLogo)
                    }
                    val organisationName = "Welcome " + org.data?.get("orgName")
                    binding.orgName.text = organisationName
                }
            }
    }

}
