package finalyearproject.is7.spaceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DevMainActivity: AppCompatActivity() {

    private lateinit var profileButton: Button
    private lateinit var createOrgButton: Button
    private lateinit var createUserButton: Button
    private lateinit var logoutButton: Button

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev_main)

        profileButton = findViewById(R.id.profileButton)
        createOrgButton = findViewById(R.id.createOrgButton)
        createUserButton = findViewById(R.id.createUserButton)
        logoutButton = findViewById(R.id.logoutButton)

        profileButton.setOnClickListener {
            startActivity(Intent(this, DevProfileActivity::class.java))
        }

        createOrgButton.setOnClickListener {
            startActivity(Intent(this, DevCreateOrgActivity::class.java))
        }

        createUserButton.setOnClickListener {
            startActivity(Intent(this, DevCreateUserActivity::class.java))
        }

        logoutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }


    }

}
