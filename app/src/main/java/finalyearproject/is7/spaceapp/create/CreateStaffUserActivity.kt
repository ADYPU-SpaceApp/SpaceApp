package finalyearproject.is7.spaceapp.create

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import finalyearproject.is7.spaceapp.databinding.ActivityCreateStaffUserBinding

class CreateStaffUserActivity:AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()
    private var mAuth2 = Firebase.auth
    private var mDb = FirebaseFirestore.getInstance()

    private lateinit var orgId: String

    private lateinit var binding: ActivityCreateStaffUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStaffUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if (mAuth.currentUser == null) {
//            finish()
//        }

        orgId = intent.getStringExtra("orgId")!!

        binding.backButtonCreateStudentActivity.setOnClickListener {
            finish()
        }

        val roleList = ArrayList<String>()
        mDb.collection("Role").get().addOnSuccessListener { result ->
            for (document in result) {
                // To add only Staff User
                if (document.data["is_Staff"] == true) {
                    roleList.add(document.id)
                }

            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roleList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnRole.adapter = adapter
        }

        val departmentList = ArrayList<String>()
        mDb.collection("Organisation").document(orgId)
            .collection("College").get().addOnSuccessListener { result ->
            for (document in result) {
                departmentList.add(document.id)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, departmentList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnDepartment.adapter = adapter
        }

        binding.btnCreateStudent.setOnClickListener {
            val email = binding.edtCreateStudentEmail.text.toString()
            val name = binding.edtCreateStudentName.text.toString()
            val role = binding.spnRole.selectedItem.toString()
            val department = binding.spnDepartment.selectedItem.toString()
            val password = binding.edtCreateStudentPassword.text.toString()
            val confirmPassword = binding.edtCreateStudentConfirmPassword.text.toString()

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
                                userMap["org"] = orgId // mDb.collection("Organisation").document(orgId)
                                userMap["role"] = role // mDb.collection("Role").document(role)
                                userMap["department"] = department // mDb.collection("Organisation").document(orgId).collection("College").document(department)
                                userMap["is_Active"] = true
                                userMap["createdBy"] = mAuth.currentUser?.uid as String
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
