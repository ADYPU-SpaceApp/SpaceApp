package finalyearproject.is7.spaceapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class CreateUserActivity:AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()
    private var mAuth2 = Firebase.auth
    private var mDb = FirebaseFirestore.getInstance()

    private lateinit var orgId: String

    private lateinit var edtEmail: EditText
    private lateinit var edtName: EditText
    private lateinit var spnRole: Spinner
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnCreateUser: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        if (mAuth.currentUser == null) {
            finish()
        }

        orgId = intent.getStringExtra("orgId")!!

        edtEmail = findViewById(R.id.edtEmail)
        edtName = findViewById(R.id.edtName)
        spnRole = findViewById(R.id.spnRole)
        edtPassword = findViewById(R.id.edtPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        btnCreateUser = findViewById(R.id.btnCreateUser)

        backBtn = findViewById(R.id.backButton)
        backBtn.setOnClickListener {
            finish()
        }

        val roleList = ArrayList<String>()
        mDb.collection("Role").get().addOnSuccessListener { result ->
            for (document in result) {
                roleList.add(document.id)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roleList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spnRole.adapter = adapter
        }

        btnCreateUser.setOnClickListener {
            val email = edtEmail.text.toString()
            val name = edtName.text.toString()
            val role = spnRole.selectedItem.toString()
            val password = edtPassword.text.toString()
            val confirmPassword = edtConfirmPassword.text.toString()

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    mAuth2.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = mAuth2.currentUser
                                val userMap = HashMap<String, Any>()
                                userMap["displaypic"] = ""
                                userMap["name"] = name
                                userMap["email"] = email
                                userMap["org"] = mDb.collection("Organisation").document(orgId)
                                userMap["role"] = mDb.collection("Role").document(role)
                                userMap["is_Active"] = true
                                mDb.collection("User").document(user!!.uid).set(userMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show()
                        }
                    mAuth2.signOut()
                }
                else {
                    Toast.makeText(this, "Password and confirm password do not match", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

}
