package finalyearproject.is7.spaceapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity:AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var submitBtn: Button

    private var mAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        edtEmail = findViewById(R.id.edtEmail)
        submitBtn = findViewById(R.id.submitButton)

        submitBtn.setOnClickListener {
            val email = edtEmail.text.toString()
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
