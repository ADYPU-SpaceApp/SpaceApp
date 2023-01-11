package finalyearproject.is7.spaceapp.dev

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import finalyearproject.is7.spaceapp.R

class DevCreateUserActivity: AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()
    private var mAuth2 = Firebase.auth
    private var mDb = FirebaseFirestore.getInstance()

    private lateinit var orgLogo: ImageView
    private lateinit var spnOrg: Spinner
    private lateinit var edtEmail: EditText
    private lateinit var edtName: EditText
    private lateinit var spnRole: Spinner
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnCreateUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev_create_user)

        orgLogo = findViewById(R.id.orgLogo)
        spnOrg = findViewById(R.id.spnOrg)
        edtEmail = findViewById(R.id.edtEmail)
        edtName = findViewById(R.id.edtName)
        spnRole = findViewById(R.id.spnRole)
        edtPassword = findViewById(R.id.edtPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        btnCreateUser = findViewById(R.id.btnCreateUser)

        val orgList = ArrayList<String>()
        mDb.collection("Organisation").get().addOnSuccessListener { result ->
            for (document in result) {
                orgList.add(document.data["orgName"].toString())
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, orgList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spnOrg.adapter = adapter
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
            val orgName = spnOrg.selectedItem.toString()
            val email = edtEmail.text.toString()
            val name = edtName.text.toString()
            val role = spnRole.selectedItem.toString()
            val password = edtPassword.text.toString()
            val confirmPassword = edtConfirmPassword.text.toString()

            if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                mDb.collection("Organisation").whereEqualTo("orgName", orgName).get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val orgId = document.id
                            mAuth2.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val user = mAuth2.currentUser
                                        val userId = user?.uid
                                        val userMap = HashMap<String, Any>()
                                        userMap["displaypic"] = ""
                                        userMap["name"] = name
                                        userMap["email"] = email
                                        userMap["org"] = orgId // mDb.collection("Organisation").document(orgId)
                                        userMap["role"] = role // mDb.collection("Role").document(role)
                                        userMap["is_Active"] = true
                                        userMap["createdBy"] = mAuth.currentUser?.uid as String
                                        mDb.collection("User").document(userId!!).set(userMap)
                                            .addOnSuccessListener {
                                                Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show()
                                                finish()
                                            }
                                    } else {
                                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }
            }
        }

    }

}
