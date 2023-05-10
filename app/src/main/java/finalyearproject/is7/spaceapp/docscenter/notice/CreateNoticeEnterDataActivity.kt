package finalyearproject.is7.spaceapp.docscenter.notice

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import finalyearproject.is7.spaceapp.databinding.ActivityCreateNoticeEnterDataBinding

class CreateNoticeEnterDataActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoticeEnterDataBinding

    private var mAuth = FirebaseAuth.getInstance()
    private var database = FirebaseDatabase.getInstance()

    private lateinit var orgId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoticeEnterDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if (mAuth.currentUser == null) {
//            finish()
//        }

        orgId = intent.getStringExtra("orgId")!!

        binding.btnSubmitNotice.setOnClickListener {
            val noticeTitle = binding.edtNoticeTitle.text.toString()
            val noticeBody = binding.edtNoticeBody.text.toString()

            if (noticeTitle.isNotEmpty() && noticeBody.isNotEmpty()) {
                database.getReference(orgId).child("Notice")
                    .push().setValue(
                    hashMapOf(
                        "title" to noticeTitle,
                        "body" to noticeBody,
                        "is_Active" to true,
                        "created_By" to mAuth.currentUser?.uid,
                        "created_At" to System.currentTimeMillis(),
                        "updated_By" to mAuth.currentUser?.uid,
                        "updated_At" to System.currentTimeMillis()
                    )
                ).addOnSuccessListener {
                    Toast.makeText(this, "Notice Created", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    }

}