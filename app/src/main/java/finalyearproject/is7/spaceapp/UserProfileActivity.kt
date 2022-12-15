package finalyearproject.is7.spaceapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileActivity:AppCompatActivity() {

    private lateinit var displaypic: ImageView
    private lateinit var emailtext: TextView
    private lateinit var nametext: TextView
    private lateinit var roletext: TextView
    private lateinit var logoutBtn: Button

    private var mAuth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        displaypic = findViewById(R.id.ProfileDisplayPic)
        emailtext = findViewById(R.id.ProfileEmailText)
        nametext = findViewById(R.id.ProfileNameText)
        roletext = findViewById(R.id.ProfileRoleText)
        logoutBtn = findViewById(R.id.logoutButton)

        // Get the current user detail
        val uid = mAuth.currentUser?.uid!!
        val email = mAuth.currentUser?.email!!
        val u = db.collection("User").document(uid)
        u.get()
            .addOnSuccessListener { documents ->
                if (documents != null) {
                    if (documents.data?.get("displaypic") != "") {
                        val imageUri = Uri.parse(documents.data?.get("displaypic").toString())
                        Log.d("CheckMe", "Image URI: $imageUri")
                        if (imageUri != null) {
                            Glide.with(this).load(imageUri).circleCrop().into(displaypic)
                        } else {
                            Glide.with(this).load(R.mipmap.ic_launcher_round).circleCrop().into(displaypic)
                        }
                    }

                    displaypic.setOnClickListener {
                        val displaypicintent = Intent(this, DisplayPicActivity::class.java)
                        displaypicintent.putExtra("is", "User")
                        startActivity(displaypicintent)
                    }

                    emailtext.text = email
                    nametext.text = documents.data?.get("name") as CharSequence?

                    val role: DocumentReference = documents.data?.get("role") as DocumentReference
                    role.get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                val r = "I'm " + document.id
                                roletext.text = r
                            } else {
                                Log.d("Jaineel", "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("Jaineel", "get failed with ", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Jaineel", "get failed with ", exception)
            }

        logoutBtn.setOnClickListener{
            mAuth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }

    override fun onRestart() {
        super.onRestart()
        val uid = mAuth.currentUser?.uid!!
        val u = db.collection("User").document(uid)
        u.get()
            .addOnSuccessListener { documents ->
                if (documents != null) {
                    if (documents.data?.get("displaypic") != "") {
                        val imageUri = Uri.parse(documents.data?.get("displaypic").toString())
                        Log.d("CheckMe", "Image URI: $imageUri")
                        if (imageUri != null) {
                            Glide.with(this).load(imageUri).circleCrop().into(displaypic)
                        } else {
                            Glide.with(this).load(R.mipmap.ic_launcher_round).circleCrop().into(displaypic)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Jaineel", "get failed with ", exception)
            }

    }

}