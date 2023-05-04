package finalyearproject.is7.spaceapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import finalyearproject.is7.spaceapp.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity: AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitChangePasswordButton.setOnClickListener {
            val newPassword = binding.edtNewPassword.text.toString()
            val confirmPassword = binding.edtConfirmNewPassword.text.toString()

            if (newPassword == confirmPassword) {
                mAuth.currentUser?.updatePassword(newPassword)
                Toast.makeText(this, "Password Changed", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                binding.edtNewPassword.error = "Passwords do not match"
                binding.edtConfirmNewPassword.error = "Passwords do not match"
            }
        }

    }
}