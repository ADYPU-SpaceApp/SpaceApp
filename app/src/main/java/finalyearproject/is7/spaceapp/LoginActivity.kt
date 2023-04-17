package finalyearproject.is7.spaceapp

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import finalyearproject.is7.spaceapp.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var mAuth = Firebase.auth
//    private var mDb = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loading.visibility = ProgressBar.INVISIBLE

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
                        startActivity(Intent(this, LauncherActivity::class.java))
                        finish()
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
}
