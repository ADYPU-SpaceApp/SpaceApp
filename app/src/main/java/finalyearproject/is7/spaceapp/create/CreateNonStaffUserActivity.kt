package finalyearproject.is7.spaceapp.create

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import finalyearproject.is7.spaceapp.databinding.ActivityCreateNonStaffUserBinding

class CreateNonStaffUserActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateNonStaffUserBinding
    
    private var mAuth = FirebaseAuth.getInstance()
    private var mAuth2 = Firebase.auth
    private var mDb = FirebaseFirestore.getInstance()

    private lateinit var orgId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNonStaffUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (mAuth.currentUser == null) {
            finish()
        }
        orgId = intent.getStringExtra("orgId")!!

        binding.backButtonCreateStudentActivity.setOnClickListener {
            finish()
        }

        val departmentList = ArrayList<String>()
        mDb.collection("Organisation").document(orgId)
            .collection("College").get().addOnSuccessListener { result ->
                for (document in result) {
                    departmentList.add(document.id)
                }
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, departmentList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spnDepartment.adapter = adapter
            }

        // When department is selected
        binding.spnDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val courseList = ArrayList<String>()
                mDb.collection("Organisation").document(orgId)
                    .collection("College").document(binding.spnDepartment.selectedItem.toString())
                    .collection("Course").get().addOnSuccessListener { result ->
                        for (document in result) {
                            courseList.add(document.id)
                        }
                        val adapter = ArrayAdapter(
                            this@CreateNonStaffUserActivity,
                            android.R.layout.simple_spinner_item,
                            courseList
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spnCourse.adapter = adapter
                    }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        // When course is selected
        binding.spnCourse.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val classList = ArrayList<String>()
                mDb.collection("Organisation").document(orgId)
                    .collection("College").document(binding.spnDepartment.selectedItem.toString())
                    .collection("Course").document(binding.spnCourse.selectedItem.toString())
                    .collection("Batch").get().addOnSuccessListener { result ->
                        for (document in result) {
                            classList.add(document.data["batchName"].toString())
                        }
                        val adapter = ArrayAdapter(
                            this@CreateNonStaffUserActivity,
                            android.R.layout.simple_spinner_item,
                            classList
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spnBatch.adapter = adapter
                    }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.btnCreateStudent.setOnClickListener {
            Log.d("CreateNonStaffUser", "Create Student Button Clicked")
            val email = binding.edtCreateStudentEmail.text.toString()
            val name = binding.edtCreateStudentName.text.toString()
            val role = "Student" // spnRole.selectedItem.toString()
            val department = binding.spnDepartment.selectedItem.toString()
            val course = binding.spnCourse.selectedItem.toString()
            val classIntake = binding.spnBatch.selectedItem.toString()
            // Get classId from mDb

            val password = binding.edtCreateStudentPassword.text.toString()
            val confirmPassword = binding.edtCreateStudentConfirmPassword.text.toString()

            mDb.collection("Organisation").document(orgId)
                .collection("College").document(binding.spnDepartment.selectedItem.toString())
                .collection("Course").document(binding.spnCourse.selectedItem.toString())
                .collection("Batch").whereEqualTo("batchName", classIntake).get()
                .addOnSuccessListener { Batch ->
                    if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                        if (password == confirmPassword) {
                            for (b in Batch) {
                                val batchId = b.id
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
                                            userMap["course"] = course // mDb.collection("Organisation").document(orgId).collection("College").document(department).collection("Course").document(course)
                                            userMap["Batch"] = batchId // mDb.collection("Organisation").document(orgId).collection("College").document(department).collection("Course").document(course).collection("Class").document(classId)
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
                        } 
                        else {
                            Toast.makeText(this, "Password and confirm password do not match", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }
}