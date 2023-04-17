package finalyearproject.is7.spaceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import finalyearproject.is7.spaceapp.databinding.ActivityLauncherBinding
import finalyearproject.is7.spaceapp.dev.DevMainActivity
import finalyearproject.is7.spaceapp.org.OrgMainActivity
import finalyearproject.is7.spaceapp.user.UserMainActivity

class LauncherActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLauncherBinding

    private var mAuth = Firebase.auth
    private var mDb = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (mAuth.currentUser != null) {
            loginAndGotoActivity()
        }
        else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun loginAndGotoActivity() {
        mDb.collection("Dev").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { user ->
                if (user.exists()) {
                    startActivity(Intent(this, DevMainActivity::class.java))
                }
            }
        mDb.collection("Organisation").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { org ->
                if (org.exists()) {
                    if (org["is_Active"] == true) {
                        startActivity(Intent(this, OrgMainActivity::class.java))
                    }
                    else {
                        Toast.makeText(this, "Your Organisation is not active", Toast.LENGTH_SHORT).show()
                        mAuth.signOut()
                    }
                }
            }
        mDb.collection("User").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { user ->
                if (user.exists()) {
                    if (user["is_Active"] == true ) {
                        mDb.collection("Organisation").document(user["org"].toString()).get()
                            .addOnSuccessListener { org ->
                                if (org.exists()) {
                                    if (org["is_Active"] == true) {
                                        startActivity(Intent(this, UserMainActivity::class.java))
                                    }
                                    else {
                                        Toast.makeText(this, "Your Organisation is not active", Toast.LENGTH_SHORT).show()
                                        mAuth.signOut()
                                    }
                                }
                            }
                    }
                    else {
                        Toast.makeText(this, "Your account is not active", Toast.LENGTH_SHORT).show()
                        mAuth.signOut()
                    }
                }
            }
        finish()
    }


}