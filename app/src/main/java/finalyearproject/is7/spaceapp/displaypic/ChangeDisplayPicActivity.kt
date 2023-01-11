package finalyearproject.is7.spaceapp.displaypic

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import finalyearproject.is7.spaceapp.R

class ChangeDisplayPicActivity : AppCompatActivity() {

    private lateinit var selectedImg: ImageView
    private lateinit var btnCancelEdit: Button
    private lateinit var btnSaveEdit: Button
    private lateinit var loading: ProgressBar
    private lateinit var storage: FirebaseStorage
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changedisplaypic)

        selectedImg = findViewById(R.id.selectedImg)
        btnCancelEdit = findViewById(R.id.btn_CancelEdit)
        btnSaveEdit = findViewById(R.id.btn_SaveEdit)

        loading = findViewById(R.id.changeDisplayPicLoading)
        loading.visibility = ProgressBar.INVISIBLE

        val aa = intent.getStringExtra("is").toString()

        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        mDb = FirebaseFirestore.getInstance()

        // Get new display pic uri from extra
        val newDisplayPicUri = Uri.parse(intent.getStringExtra("newDisplayPicUri"))
        Log.d(TAG, "onCreate: $newDisplayPicUri")
        if (newDisplayPicUri != null) {
            Glide.with(this).load(newDisplayPicUri).into(selectedImg)
        }

        btnCancelEdit.setOnClickListener {
            finish()
        }

        btnSaveEdit.setOnClickListener {
            loading.visibility = ProgressBar.VISIBLE

            val ref = storage.reference.child("${aa}DisplayPic/${mAuth.currentUser?.uid}")
            ref.putFile(newDisplayPicUri)
                .addOnSuccessListener {
                    Log.d(TAG, "onSuccess: Display pic uploaded")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "onSuccess: $it")
                        mDb.collection(aa).document(mAuth.currentUser?.uid!!)
                                .update("displaypic", it.toString())
                                .addOnSuccessListener {
                                    Log.d(TAG, "onSuccess: Display pic updated")
                                    finish()
                                }
                    }
                }
        }
    }

}