package finalyearproject.is7.spaceapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DevProfileActivity: AppCompatActivity() {

    private lateinit var devDP: ImageView
    private lateinit var devName: TextView
    private lateinit var devEmail: TextView

    private val mAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev_profile)

        devDP = findViewById(R.id.devDP)
        devName = findViewById(R.id.devName)
        devEmail = findViewById(R.id.devEmail)

        mDb.collection("Dev").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { dev ->
                if (dev.data?.get("displaypic") != "") {
                    Glide.with(this).load(dev.data?.get("displaypic")).circleCrop().into(devDP)
                }
                devName.text = dev.data?.get("name").toString()
                devEmail.text = dev.data?.get("email").toString()
            }

        devDP.setOnClickListener {
            val displayPicActivityIntent = Intent(this, DisplayPicActivity::class.java)
            displayPicActivityIntent.putExtra("is", "Dev")
            startActivity(displayPicActivityIntent)
        }

    }

    override fun onRestart() {
        super.onRestart()
        val uid = mAuth.currentUser?.uid!!
        val u = mDb.collection("Dev").document(uid)
        u.get()
            .addOnSuccessListener { documents ->
                if (documents != null) {
                    if (documents.data?.get("displaypic") != "") {
                        val imageUri = Uri.parse(documents.data?.get("displaypic").toString())
                        Log.d("CheckMe", "Image URI: $imageUri")
                        if (imageUri != null) {
                            Glide.with(this).load(imageUri).into(devDP)
                        } else {
                            Glide.with(this).load(R.mipmap.ic_launcher_round).into(devDP)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Jaineel", "get failed with ", exception)
            }
    }

}