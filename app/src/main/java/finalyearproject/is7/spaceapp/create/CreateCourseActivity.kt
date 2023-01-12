package finalyearproject.is7.spaceapp.create

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.databinding.ActivityCreateCourseBinding

class CreateCourseActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateCourseBinding

    private lateinit var mDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDb = FirebaseFirestore.getInstance()
        val orgId = intent.getStringExtra("orgId")

        val departmentList = ArrayList<String>()
        mDb.collection("Organisation").document(orgId!!)
            .collection("College").get().addOnSuccessListener { result ->
                for (document in result) {
                    departmentList.add(document.id)
                }
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, departmentList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spnDepartment.adapter = adapter
            }

    }

}
