package finalyearproject.is7.spaceapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class OrgDetailActivity: AppCompatActivity() {

    private lateinit var orgLogoImageView: ImageView
    private lateinit var orgNameTextView: TextView
    private lateinit var orgEmailTextView: TextView
    private lateinit var totalUsersTextView: TextView
    private lateinit var disableOrgButton: Button

    private val mDb = FirebaseFirestore.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_org_detail)

        orgLogoImageView = findViewById(R.id.orgLogo)
        orgNameTextView = findViewById(R.id.orgName)
        orgEmailTextView = findViewById(R.id.orgEmail)
        totalUsersTextView = findViewById(R.id.totalUsers)
        disableOrgButton = findViewById(R.id.disableOrgButton)

        val name = intent.getStringExtra("name")
        val uid = intent.getStringExtra("uid")
        val displaypic = intent.getStringExtra("displaypic")

        orgNameTextView.text = name

        if (displaypic != "") {
            Glide.with(this).load(displaypic).circleCrop().into(orgLogoImageView)
        }
        else {
            Glide.with(this).load(R.drawable.profile).circleCrop().into(orgLogoImageView)
        }

        mDb.collection("Organisation").document(uid!!).get()
            .addOnSuccessListener {
                orgEmailTextView.text = it.getString("email")

                if (it.getBoolean("is_Active") == true) {
                    disableOrgButton.text = "Disable Organisation"
                }
                else {
                    disableOrgButton.text = "Enable Organisation"
                }
            }

        mDb.collection("User").whereEqualTo("org", uid).get()
            .addOnSuccessListener {
                totalUsersTextView.text = it.size().toString()
            }

        disableOrgButton.setOnClickListener{
            mDb.collection("Organisation").document(uid).get()
                .addOnSuccessListener {
                    if (it.getBoolean("is_Active") == true) {
                        mDb.collection("Organisation").document(uid).update("is_Active", false)
                    }
                    else {
                        mDb.collection("Organisation").document(uid).update("is_Active", true)
                    }
                }
        }


    }

}
