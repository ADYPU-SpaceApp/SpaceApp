package finalyearproject.is7.spaceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DevMainActivity: AppCompatActivity() {

    private lateinit var profileButton: Button
    private lateinit var createDevActivity: Button
    private lateinit var createOrgButton: Button
    private lateinit var listOrgButton: Button
    private lateinit var createUserButton: Button
    private lateinit var listUserButton: Button
    private lateinit var logoutButton: Button

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev_main)

        profileButton = findViewById(R.id.profileButton)
        createDevActivity = findViewById(R.id.createDevButton)
        createOrgButton = findViewById(R.id.createOrgButton)
        listOrgButton = findViewById(R.id.listOrgButton)
        createUserButton = findViewById(R.id.createUserButton)
        listUserButton = findViewById(R.id.listUserButton)
        logoutButton = findViewById(R.id.logoutButton)

        profileButton.setOnClickListener {
            startActivity(Intent(this, DevProfileActivity::class.java))
        }

        createDevActivity.setOnClickListener {
            startActivity(Intent(this, CreateDevActivity::class.java))
        }

        createOrgButton.setOnClickListener {
            startActivity(Intent(this, DevCreateOrgActivity::class.java))
        }

        listOrgButton.setOnClickListener {
            val intent = Intent(this, DevListXYZActivity::class.java)
            intent.putExtra("xyz", "Org")
            startActivity(intent)
        }

        createUserButton.setOnClickListener {
            startActivity(Intent(this, DevCreateUserActivity::class.java))
        }

        listUserButton.setOnClickListener {
            val intent = Intent(this, DevListXYZActivity::class.java)
            intent.putExtra("xyz", "User")
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }


    }

}
