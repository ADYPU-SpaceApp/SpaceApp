package finalyearproject.is7.spaceapp

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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

class CreateNoticeActivity: AppCompatActivity(){

    private var mAuth = FirebaseAuth.getInstance()
    private var mDbRef = FirebaseDatabase.getInstance()
    private var storage = FirebaseStorage.getInstance()

    private lateinit var orgId: String
    private lateinit var edtNoticeTitle: EditText
    private lateinit var uploadNoticeButton: Button
    private lateinit var selectedFileNametxt: TextView
    private lateinit var submitButton: Button

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_notice)

        orgId = intent.getStringExtra("orgId")!!

        edtNoticeTitle = findViewById(R.id.edtNoticeTitle)
        uploadNoticeButton = findViewById(R.id.uploadNoticeButton)
        selectedFileNametxt = findViewById(R.id.selectedFileName_txt)
        submitButton = findViewById(R.id.submitButton)

        var selectedPdfIntent: Intent? = null
        var selectedPdfUri: Uri? = null

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedPdfIntent = result.data
                selectedPdfUri = selectedPdfIntent?.data
                val fileName = selectedPdfUri?.lastPathSegment
                selectedFileNametxt.text = fileName
            }
        }


        uploadNoticeButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), 1)
            }
            else {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "application/pdf"
                resultLauncher.launch(intent)
            }
        }

        submitButton.setOnClickListener {
            val noticeTitle = edtNoticeTitle.text.toString()
            if (noticeTitle.isEmpty()) {
                edtNoticeTitle.error = "Please enter a title"
                edtNoticeTitle.requestFocus()
                return@setOnClickListener
            }
            if (selectedFileNametxt.text == "No file selected") {
                Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val noticeId = mDbRef.getReference("$orgId/Notice").push().key
            val noticeRef = storage.reference.child("Notices/$orgId/$noticeId")
            val uploadTask = noticeRef.putFile(selectedPdfUri!!)

            uploadTask.addOnSuccessListener {
                Log.d("CreateNoticeActivity", "File uploaded successfully")
                noticeRef.downloadUrl
                    .addOnSuccessListener { uri ->
                    mDbRef.getReference("$orgId/Notice/$noticeId").setValue(
                        hashMapOf(
                            "title" to noticeTitle,
                            "note" to uri.toString(),
                            "createdBy" to mAuth.currentUser?.email,
                            "createdAt" to System.currentTimeMillis(),
                            "is_Active" to true
                        )
                    )
                    Toast.makeText(this, "Notice uploaded successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            Toast.makeText(this, "Notice Created", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "application/pdf"
                resultLauncher.launch(intent)
            }
            else {
                ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), 1)
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    }

}


//class CreateNoticeActivity:AppCompatActivity() {
//
//    private var mAuth = FirebaseAuth.getInstance()
//    private var database = FirebaseDatabase.getInstance()
//
//    private lateinit var orgId: String
//    private lateinit var edtNoticeTitle: EditText
//    private lateinit var edtNoticeBody: EditText
//    private lateinit var submitButton: Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_create_notice)
//
//        if (mAuth.currentUser == null) {
//            finish()
//        }
//
//        orgId = intent.getStringExtra("orgId")!!
//
//        edtNoticeTitle = findViewById(R.id.edtNoticeTitle)
//        edtNoticeBody = findViewById(R.id.edtNoticeBody)
//        submitButton = findViewById(R.id.submitButton)
//
//        submitButton.setOnClickListener {
//            val noticeTitle = edtNoticeTitle.text.toString()
//            val noticeBody = edtNoticeBody.text.toString()
//
//            if (noticeTitle.isNotEmpty() && noticeBody.isNotEmpty()) {
//                database.getReference(orgId).child("Notice")
//                    .push().setValue(
//                    hashMapOf(
//                        "title" to noticeTitle,
//                        "body" to noticeBody,
//                        "is_Active" to true,
//                        "created_By" to mAuth.currentUser?.uid,
//                        "created_At" to System.currentTimeMillis(),
//                        "updated_By" to mAuth.currentUser?.uid,
//                        "updated_At" to System.currentTimeMillis()
//                    )
//                ).addOnSuccessListener {
//                    Toast.makeText(this, "Notice Created", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            }
//        }
//
//    }
//
//}
