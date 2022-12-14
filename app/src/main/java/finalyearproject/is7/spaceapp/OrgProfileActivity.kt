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

class OrgProfileActivity:AppCompatActivity() {

    private lateinit var orgLogo: ImageView
    private lateinit var orgName: TextView
    private lateinit var orgEmail: TextView

    private val mAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_org_profile)

        orgLogo = findViewById(R.id.orgLogo)
        orgName = findViewById(R.id.orgName)
        orgEmail = findViewById(R.id.orgEmail)

        mDb.collection("Organisation").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { org ->
                if (org.data?.get("displaypic") != "") {
                    Glide.with(this).load(org.data?.get("displaypic")).circleCrop().into(orgLogo)
                }
                orgName.text = org.data?.get("orgName").toString()
                orgEmail.text = org.data?.get("email").toString()

                orgLogo.setOnClickListener {
                    val displayPicActivityIntent = Intent(this, DisplayPicActivity::class.java)
                    displayPicActivityIntent.putExtra("is", "Organisation")
                    startActivity(displayPicActivityIntent)
                }

            }



    }

    override fun onResume() {
        super.onResume()
        val uid = mAuth.currentUser?.uid!!
        mDb.collection("Organisation").document(uid).get()
            .addOnSuccessListener { documents ->
                if (documents != null) {
                    if (documents.data?.get("displaypic") != "") {
                        val imageUri = Uri.parse(documents.data?.get("displaypic").toString())
                        Log.d("CheckMe", "Image URI: $imageUri")
                        if (imageUri != null) {
                            Glide.with(this).load(imageUri).into(orgLogo)
                        } else {
                            Glide.with(this).load(R.mipmap.ic_launcher_round).into(orgLogo)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Jaineel", "get failed with ", exception)
            }
    }

}