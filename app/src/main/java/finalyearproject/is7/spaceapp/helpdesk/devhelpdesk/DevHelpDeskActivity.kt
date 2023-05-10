package finalyearproject.is7.spaceapp.helpdesk.devhelpdesk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import finalyearproject.is7.spaceapp.databinding.ActivityDevHelpDeskBinding

class DevHelpDeskActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDevHelpDeskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevHelpDeskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orgId = intent.getStringExtra("orgId")

        binding.btnCreateTicketDevHelpDesk.setOnClickListener {
            val intent = Intent(this, CreateTicketActivity::class.java)
            intent.putExtra("orgId", orgId)
            startActivity(intent)
        }

        binding.btnViewTicketsDevHelpDesk.setOnClickListener {
            val intent = Intent(this, ViewTicketActivity::class.java)
            intent.putExtra("orgId", orgId)
            startActivity(intent)
        }

        binding.btnFaqDevHelpDesk.setOnClickListener {
            val intent = Intent(this, FaqActivity::class.java)
            startActivity(intent)
        }


    }

}