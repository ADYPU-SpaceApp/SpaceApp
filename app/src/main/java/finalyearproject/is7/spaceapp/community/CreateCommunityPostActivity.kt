package finalyearproject.is7.spaceapp.community

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import finalyearproject.is7.spaceapp.databinding.ActivityCreateCommunityPostBinding

class CreateCommunityPostActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateCommunityPostBinding

    private val mAuth = FirebaseAuth.getInstance()
//    private val mDb = FirebaseFirestore.getInstance()
    private var mDbRef = FirebaseDatabase.getInstance()
    private var storage = FirebaseStorage.getInstance()

    private lateinit var orgId: String
    private var newPicUrl: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCommunityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orgId = intent.getStringExtra("orgId")!!
        val community = intent.getStringExtra("community")

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { new_pic_uri ->
            if (new_pic_uri != null) {
                Log.d("PhotoPicker", "Selected URI: $new_pic_uri")
                Glide.with(this).load(new_pic_uri).into(binding.communityPostImage)
                newPicUrl = new_pic_uri
            }
        }

        binding.communityPostImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createCommunityPostButton.setOnClickListener {

            if (newPicUrl == null ) {
                Toast.makeText(this, "No Image selected", Toast.LENGTH_SHORT).show()
            }

            if (binding.communityPostDescription.text.toString().isEmpty()){
                binding.communityPostDescription.error = "Please enter a description"
                binding.communityPostDescription.requestFocus()
            }

            if (newPicUrl != null && binding.communityPostDescription.text.isNotEmpty()){

                if (community != "nothing"){
                    val postId = mDbRef.getReference("$orgId/Community/posts").push().key
                    val postRef = storage.reference.child("CommunityPost/$orgId/$community/$postId")

                    postRef.putFile(newPicUrl!!).addOnSuccessListener {
                        Log.d(ContentValues.TAG, "onSuccess: Display pic uploaded")
                        postRef.downloadUrl
                            .addOnSuccessListener {
                                Log.d(ContentValues.TAG, "onSuccess: $it")
                                if (postId != null) {
                                    mDbRef.getReference("$orgId/Community/posts/$postId").setValue(
                                            hashMapOf(
                                                "caption" to binding.communityPostDescription,
                                                "communityId" to community,
                                                "createdAt" to System.currentTimeMillis(),
                                                "createdBy" to mAuth.currentUser?.uid,
                                                "is_Active" to true,
                                                "post" to it.toString()
                                            )
                                        )
                                }
                                Toast.makeText(this, "Post uploaded successfully", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }

                }

            }

        }



}
