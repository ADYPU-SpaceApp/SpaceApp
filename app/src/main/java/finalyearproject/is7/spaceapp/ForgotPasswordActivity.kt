package finalyearproject.is7.spaceapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import finalyearproject.is7.spaceapp.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity:AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    private var mAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitForgotPasswdEmailButton.setOnClickListener {
            val email = binding.edtForgotPasswdEmail.text.toString()
            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Email Sent", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else {
                        Toast.makeText(this, "Email Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

}
