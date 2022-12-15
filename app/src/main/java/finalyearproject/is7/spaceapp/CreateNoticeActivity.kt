package finalyearproject.is7.spaceapp

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

//class CreateNoticeActivity: AppCompatActivity(){
//
//    private var mAuth = FirebaseAuth.getInstance()
//    private var mDbRef = FirebaseDatabase.getInstance()
//    private var storage = FirebaseStorage.getInstance()
//
//    private lateinit var orgId: String
//    private lateinit var edtNoticeTitle: EditText
//    private lateinit var uploadNoticeButton: Button
//    private lateinit var selectedFileNametxt: TextView
//    private lateinit var submitButton: Button
//
//    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_create_notice)
//
//        orgId = intent.getStringExtra("orgId")!!
//
//        edtNoticeTitle = findViewById(R.id.edtNoticeTitle)
//        uploadNoticeButton = findViewById(R.id.uploadNoticeButton)
//        selectedFileNametxt = findViewById(R.id.selectedFileName_txt)
//        submitButton = findViewById(R.id.submitButton)
//
//        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                val data: Intent? = result.data
//                val uri = data?.data
//                val fileName = uri?.lastPathSegment
//                selectedFileNametxt.text = fileName
//            }
//        }
//
//
//        uploadNoticeButton.setOnClickListener {
//            if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), 1)
//            }
//            else {
//                val intent = Intent(Intent.ACTION_GET_CONTENT)
//                intent.type = "application/pdf"
//                resultLauncher.launch(intent)
//            }
//        }
//
//        submitButton.setOnClickListener {
//            val noticeTitle = edtNoticeTitle.text.toString()
//            if (noticeTitle.isEmpty()) {
//                edtNoticeTitle.error = "Please enter a title"
//                edtNoticeTitle.requestFocus()
//                return@setOnClickListener
//            }
//            if (selectedFileNametxt.text == "No file selected") {
//                Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            Log.d("CreateNoticeActivity", "Notice title: $noticeTitle")
//            Log.d("CreateNoticeActivity", "File name: ${selectedFileNametxt.text}")
//            val fileName = selectedFileNametxt.text.toString()
//            val storageRef = storage.reference
//            val noticeRef = storageRef.child("notices/$orgId/$fileName")
//            val uploadTask = noticeRef.putFile(intent.data!!)
//
//            uploadTask.addOnSuccessListener {
//                Log.d("CreateNoticeActivity", "File uploaded successfully")
//                val noticeId = mDbRef.reference.child("notices/$orgId").push().key
//                mDbRef.reference.child("notices/$orgId/$noticeId")
//                    .setValue(
//                        hashMapOf(
//                            "author" to mAuth.currentUser?.uid,
//                            "title" to noticeTitle,
//                            "fileUrl" to noticeRef.downloadUrl.toString()
//                        )
//                    )
//                Toast.makeText(this, "Notice uploaded successfully", Toast.LENGTH_SHORT).show()
//                finish()
//            }.addOnFailureListener {
//                Log.d("CreateNoticeActivity", "File upload failed")
//                Toast.makeText(this, "Notice upload failed", Toast.LENGTH_SHORT).show()
//            }
//
//            Toast.makeText(this, "Notice Created", Toast.LENGTH_SHORT).show()
//            finish()
//        }
//
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == 1) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                val intent = Intent(Intent.ACTION_GET_CONTENT)
//                intent.type = "application/pdf"
//                resultLauncher.launch(intent)
//            }
//            else {
//                ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), 1)
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    }
//
//}


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
//                    TriggerNotification().sendNotification(
//                        "New Notice",
//                        noticeTitle,
//                        orgId
//                    )
                    finish()
                }
            }
        }

    }

}
