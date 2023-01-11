package finalyearproject.is7.spaceapp.dev

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import finalyearproject.is7.spaceapp.LoginActivity
import finalyearproject.is7.spaceapp.R

class DevCreateOrgActivity: AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    private val mAuth2 = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()
    private val mStorage = FirebaseStorage.getInstance()

    private var logoUri: Uri? = null

    private lateinit var orgLogo: ImageView
    private lateinit var orgName: EditText
    private lateinit var orgEmail: EditText
    private lateinit var orgPassword: EditText
    private lateinit var orgConfirmPassword: EditText
    private lateinit var submit: Button

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { new_pic_uri ->
        if (new_pic_uri != null) {
            Log.d("PhotoPicker", "Selected URI: $new_pic_uri")
            logoUri = new_pic_uri
            Glide.with(this).load(new_pic_uri).into(orgLogo)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev_create_org)

        if (mAuth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        orgLogo = findViewById(R.id.orgLogo)
        orgName = findViewById(R.id.orgName)
        orgEmail = findViewById(R.id.orgEmail)
        orgPassword = findViewById(R.id.orgPassword)
        orgConfirmPassword = findViewById(R.id.orgConfirmPassword)
        submit = findViewById(R.id.createOrgButton)

        orgLogo.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        submit.setOnClickListener {
            val name = orgName.text.toString()
            val email = orgEmail.text.toString()
            val password = orgPassword.text.toString()
            val confirmPassword = orgConfirmPassword.text.toString()

            if (name == "" || email == "" || password == "" || confirmPassword == "") {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please fill in all fields")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            } else if (password != confirmPassword) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Passwords do not match")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
            else {
                createOrg(name, email, password)
            }
        }

    }

    private fun createOrg(name: String, email: String, password: String) {
        Log.d("Jaineel","$name, $email, $logoUri")
        mAuth2.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (logoUri != null) {
                    // upload logoUri to firebase storage
                    val logoRef = mStorage.reference.child("OrganisationDisplayPic/${mAuth2.currentUser?.uid}")
                    logoRef.putFile(logoUri!!)
                        .addOnSuccessListener {
                            logoRef.downloadUrl
                                .addOnSuccessListener { logoUrl ->
                                    val org = hashMapOf(
                                        "orgName" to name,
                                        "email" to email,
                                        "displaypic" to logoUrl.toString(),
                                        "is_Active" to true
                                    )
                                    mDb.collection("Organisation").document(mAuth2.currentUser?.uid!!)
                                        .set(org)
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "Organization created", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this, DevMainActivity::class.java))
                                            finish()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(this, "Failed to create organization", Toast.LENGTH_SHORT).show()
                                        }
                                }
                        }
                }
                else {
                    val org = hashMapOf(
                        "orgName" to name,
                        "email" to email,
                        "displaypic" to "",
                        "is_Active" to true,
                    )
                    mDb.collection("Organisation").document(mAuth2.currentUser?.uid!!)
                        .set(org)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Organization created", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, DevMainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to create organization", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        mAuth2.signOut()
    }

}
