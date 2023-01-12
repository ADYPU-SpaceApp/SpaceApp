package finalyearproject.is7.spaceapp.create

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.databinding.ActivityCreateDepartmentBinding

class CreateDepartmentActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateDepartmentBinding

    private lateinit var mDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateDepartmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orgId = intent.getStringExtra("orgId")
        mDb = FirebaseFirestore.getInstance()

        binding.btnCreateDepartment.setOnClickListener {

            val name = binding.edtDepartmentName.text.toString()

            if (name.isEmpty()) {
                binding.edtDepartmentName.error = "Department name is required"
                binding.edtDepartmentName.requestFocus()
                return@setOnClickListener
            }

            // create document with id as name
            mDb.collection("Organisation")
                .document(orgId!!).collection("College")
                .document(name).set(hashMapOf("name" to name))
                .addOnSuccessListener {
                    Toast.makeText(this, "Department created successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }

        }

    }

}
