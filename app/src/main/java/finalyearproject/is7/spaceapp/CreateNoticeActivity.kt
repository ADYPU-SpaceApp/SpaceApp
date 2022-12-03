package finalyearproject.is7.spaceapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CreateNoticeActivity:AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()
    private var database = FirebaseDatabase.getInstance()

    private lateinit var orgId: String
    private lateinit var edtNoticeTitle: EditText
    private lateinit var edtNoticeBody: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_notice)

        if (mAuth.currentUser == null) {
            finish()
        }

        orgId = intent.getStringExtra("orgId")!!

        edtNoticeTitle = findViewById(R.id.edtNoticeTitle)
        edtNoticeBody = findViewById(R.id.edtNoticeBody)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val noticeTitle = edtNoticeTitle.text.toString()
            val noticeBody = edtNoticeBody.text.toString()

            if (noticeTitle.isNotEmpty() && noticeBody.isNotEmpty()) {
                database.getReference("Organisation").child(orgId).child("Notice")
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
