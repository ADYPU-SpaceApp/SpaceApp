package finalyearproject.is7.spaceapp.dev

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.databinding.ActivityCreateDevBinding

class CreateDevActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateDevBinding

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()
    private val mAuth2 = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateDevBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createDevButton.setOnClickListener {

            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                return@setOnClickListener
            }
                createDev(name, email, password)

        }


    }

    private fun createDev(name: String, email: String, password: String) {
        mAuth2.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val uid = mAuth2.currentUser?.uid
                    val dev = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "displaypic" to ""
                    )
                    mDb.collection("Dev").document(uid!!).set(dev)
                        .addOnCompleteListener {db_it ->
                            if (db_it.isSuccessful) {
                                Toast.makeText(this, "Dev Created", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                        .addOnFailureListener { db_it ->
                            Toast.makeText(this, db_it.message, Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        mAuth2.signOut()
    }


}
