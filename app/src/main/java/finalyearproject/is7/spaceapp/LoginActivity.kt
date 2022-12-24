package finalyearproject.is7.spaceapp

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity: AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var txtForgotPasswd: TextView
    private lateinit var btnLogin: Button
    private lateinit var loading: ProgressBar

    private var mAuth = Firebase.auth
    private var mDb = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.Email)
        edtPassword = findViewById(R.id.Password)
        txtForgotPasswd = findViewById(R.id.ForgotPassword)
        btnLogin = findViewById(R.id.LoginButton)
        loading = findViewById(R.id.loading)
        loading.visibility = ProgressBar.INVISIBLE

        if (mAuth.currentUser != null) {
            loginAndGotoActivity()
        }

        // Check internet connection if not connected then show toast
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        if (!isConnected) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }

        txtForgotPasswd.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            loading.visibility = ProgressBar.VISIBLE
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        loading.visibility = ProgressBar.GONE
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        loginAndGotoActivity()
                    }
                    else {
                        loading.visibility = ProgressBar.INVISIBLE
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    loading.visibility = ProgressBar.INVISIBLE
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
            .addOnSuccessListener { user ->
                if (user.exists()) {
                    startActivity(Intent(this, OrgMainActivity::class.java))
                }
            }
        mDb.collection("User").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { user ->
                if (user.exists()) {
                    if (user["is_Active"] == true ) {
                    startActivity(Intent(this, UserMainActivity::class.java))
                    }
                    else {
                        Toast.makeText(this, "Your account is not active", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        finish()
    }

}
