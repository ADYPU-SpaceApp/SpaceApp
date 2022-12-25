package finalyearproject.is7.spaceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class GroupActivity: AppCompatActivity() {

    private lateinit var createGroupBtn: Button
    private lateinit var viewMyGroupsBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        createGroupBtn = findViewById(R.id.createGroupBtn)
        viewMyGroupsBtn = findViewById(R.id.viewMyGroupsBtn)

        createGroupBtn.setOnClickListener {
            val intent = Intent(this, CreateGroupActivity::class.java)
            startActivity(intent)
        }

//        viewMyGroupsBtn.setOnClickListener {
//            val intent = Intent(this, ViewMyGroupsActivity::class.java)
//            startActivity(intent)
//        }

    }

}
