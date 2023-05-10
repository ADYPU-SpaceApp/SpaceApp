package finalyearproject.is7.spaceapp.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.ChangePasswordActivity
import finalyearproject.is7.spaceapp.LoginActivity
import finalyearproject.is7.spaceapp.create.CreateNonStaffUserActivity
import finalyearproject.is7.spaceapp.databinding.ActivityUserSettingsBinding
import finalyearproject.is7.spaceapp.helpdesk.devhelpdesk.DevHelpDeskActivity

class UserSettingsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityUserSettingsBinding

    private val mAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orgId = intent.getStringExtra("orgId")

        mDb.collection("User").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
//                orgId = it["org"] as String
                val role = it["role"] as String
                mDb.collection("Role").document(role).get()
                    .addOnSuccessListener { r ->
                        if (r.data?.get("is_Staff") == true) {
                            binding.btnAdminConsole.setOnClickListener{
                                val goToCreateStaffUserActivityIntent = Intent(this, CreateNonStaffUserActivity::class.java)
                                goToCreateStaffUserActivityIntent.putExtra("orgId", orgId)
                                startActivity(goToCreateStaffUserActivityIntent)
//                                val intent = Intent(this, UserAdminConsoleActivity::class.java)
//                                startActivity(intent)
                            }
                        } else {
                            binding.btnAdminConsole.visibility = View.GONE
//                            binding.txtHeadLine.text = "Coming Soon"
                        }
                    }
            }

        binding.btnDevHelpDesk.setOnClickListener{
            val intent = Intent(this, DevHelpDeskActivity::class.java)
            intent.putExtra("orgId", orgId)
            startActivity(intent)
        }

        binding.btnChangePassword.setOnClickListener{
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener{
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}