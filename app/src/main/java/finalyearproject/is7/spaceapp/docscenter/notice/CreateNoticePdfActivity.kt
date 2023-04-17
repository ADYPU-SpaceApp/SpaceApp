package finalyearproject.is7.spaceapp.docscenter.notice

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import finalyearproject.is7.spaceapp.databinding.ActivityCreateNoticePdfBinding

class CreateNoticePdfActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoticePdfBinding
    
    private var mAuth = FirebaseAuth.getInstance()
    private var mDbRef = FirebaseDatabase.getInstance()
    private var storage = FirebaseStorage.getInstance()

    private lateinit var orgId: String

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoticePdfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        orgId = intent.getStringExtra("orgId")!!

        var selectedPdfIntent: Intent?
        var selectedPdfUri: Uri? = null

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedPdfIntent = result.data
                selectedPdfUri = selectedPdfIntent?.data
                binding.txtSelectedFileName.text = selectedPdfUri?.path
            }
        }

        binding.btnSelectNotice.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), 1)
            }
            else {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "application/pdf"
                resultLauncher.launch(intent)
            }
        }

        binding.btnSubmitNotice.setOnClickListener {
            val noticeTitle = binding.edtNoticeTitle.text.toString()
            if (noticeTitle.isEmpty()) {
                binding.edtNoticeTitle.error = "Please enter a title"
                binding.edtNoticeTitle.requestFocus()
                return@setOnClickListener
            }
            if (binding.txtSelectedFileName.text == "No file selected") {
                Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val noticeId = mDbRef.getReference("$orgId/Notice").push().key
            val noticeRef = storage.reference.child("Notices/$orgId/$noticeId")
            val uploadTask = noticeRef.putFile(selectedPdfUri!!)

            uploadTask.addOnSuccessListener {
                Log.d("CreateNoticePdfActivity", "File uploaded successfully")
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



