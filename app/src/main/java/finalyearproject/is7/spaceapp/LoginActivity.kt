package finalyearproject.is7.spaceapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import finalyearproject.is7.spaceapp.databinding.ActivityLoginBinding
import finalyearproject.is7.spaceapp.dev.DevMainActivity
import finalyearproject.is7.spaceapp.org.OrgMainActivity
import finalyearproject.is7.spaceapp.user.UserMainActivity

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var mAuth = Firebase.auth
    private var mDb = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loading.visibility = ProgressBar.INVISIBLE

        if (mAuth.currentUser != null) {
            loginAndGotoActivity()
        }

        binding.txtForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtLoginEmail.text.toString()
            val password = binding.edtLoginPassword.text.toString()
            binding.loading.visibility = ProgressBar.VISIBLE
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        binding.loading.visibility = ProgressBar.GONE
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        loginAndGotoActivity()
                    }
                    else {
                        binding.loading.visibility = ProgressBar.INVISIBLE
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    binding.loading.visibility = ProgressBar.INVISIBLE
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
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
