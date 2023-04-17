package finalyearproject.is7.spaceapp.create

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.databinding.ActivityCreateBatchBinding

class CreateBatchActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateBatchBinding

    private lateinit var mDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDb = FirebaseFirestore.getInstance()
        val orgId = intent.getStringExtra("orgId")!!

        val departmentList = ArrayList<String>()
        mDb.collection("Organisation").document(orgId)
            .collection("College").get().addOnSuccessListener { result ->
                for (document in result) {
                    departmentList.add(document.id)
                }
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, departmentList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spnDepartmentCreateBatch.adapter = adapter
            }

        // When department is selected
        binding.spnDepartmentCreateBatch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val courseList = ArrayList<String>()
                mDb.collection("Organisation").document(orgId)
                    .collection("College").document(binding.spnDepartmentCreateBatch.selectedItem.toString())
                    .collection("Course").get().addOnSuccessListener { result ->
                        for (document in result) {
                            courseList.add(document.id)
                        }
                        val adapter = ArrayAdapter(
                            this@CreateBatchActivity,
                            android.R.layout.simple_spinner_item,
                            courseList
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spnCourseCreateBatch.adapter = adapter
                    }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        binding.btnCreateBatch.setOnClickListener{

            val batchName = binding.edtCreateBatchName.text.toString()
            val department = binding.spnDepartmentCreateBatch.selectedItem.toString()
            val course = binding.spnCourseCreateBatch.selectedItem.toString()

            if (batchName.isEmpty() || department.isEmpty() || course.isEmpty()) {
                binding.edtCreateBatchName.error = "Something is Missing"
                binding.edtCreateBatchName.requestFocus()
                return@setOnClickListener
            }

            val batchId = mDb.collection("Organisation").document(orgId)
                .collection("College").document(department)
                .collection("Course").document(course)
                .collection("Batch").document().id

            mDb.collection("Organisation").document(orgId)
                .collection("College").document(department)
                .collection("Course").document(course)
                .collection("Batch").document(batchId).set(mapOf(
                    "batchName" to batchName
                )).addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener { e ->
                    binding.edtCreateBatchName.error = e.message
                    binding.edtCreateBatchName.requestFocus()
                }
        }
    }
}
