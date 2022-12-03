package finalyearproject.is7.spaceapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DisplayPicActivity : AppCompatActivity() {

    private lateinit var aa: String

    private lateinit var displaypic: ImageView
    private lateinit var btnedit: Button

    private var mAuth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()

    private val pickMedia = registerForActivityResult(PickVisualMedia()) { new_pic_uri ->
        if (new_pic_uri != null) {
            Log.d("PhotoPicker", "Selected URI: $new_pic_uri")
            val changeDisplayPicIntent = Intent(this, ChangeDisplayPicActivity::class.java)
            changeDisplayPicIntent.putExtra("newDisplayPicUri", new_pic_uri.toString())
            changeDisplayPicIntent.putExtra("is", aa)
            startActivity(changeDisplayPicIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_displaypic)

        aa = intent.getStringExtra("is").toString()

        displaypic = findViewById(R.id.ViewDisplayPic)
        btnedit = findViewById(R.id.btn_edit)

        db.collection(aa).document(mAuth.currentUser?.uid!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val dpsrc = Uri.parse(document.getString("displaypic"))
                    if (dpsrc.toString() != "") {
                        Glide.with(this).load(dpsrc).into(displaypic)
                    } else {
                        Glide.with(this).load(R.mipmap.ic_launcher_round).into(displaypic)
                    }
                }
            }

        btnedit.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
    }

    override fun onResume() {
        super.onResume()
        db.collection(aa).document(mAuth.currentUser?.uid!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val dpsrc = Uri.parse(document.getString("displaypic"))
                    if (dpsrc.toString() != "") {
                        Glide.with(this).load(dpsrc).into(displaypic)
                    } else {
                        Glide.with(this).load(R.mipmap.ic_launcher_round).into(displaypic)
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        pickMedia.unregister()
        finish()
    }

}