package finalyearproject.is7.spaceapp.docscenter.orghelpdesk

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import finalyearproject.is7.spaceapp.databinding.ActivityOrgHelpDeskBinding

class OrgHelpDeskActivity: AppCompatActivity() {

    private lateinit var binding: ActivityOrgHelpDeskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrgHelpDeskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orgId = intent.getStringExtra("orgId")

        binding.btnRequestOrgHelpDesk.setOnClickListener {
//            val intent = Intent(this, RequestActivity::class.java)
//            intent.putExtra("orgId", orgId)
//            startActivity(intent)
            Toast.makeText(this, "Request", Toast.LENGTH_SHORT).show()
        }

        binding.btnReportOrgHelpDesk.setOnClickListener {
//            val intent = Intent(this, ReportActivity::class.java)
//            intent.putExtra("orgId", orgId)
//            startActivity(intent)
            Toast.makeText(this, "Report", Toast.LENGTH_SHORT).show()
        }

        binding.btnAssistanceOrgHelpDesk.setOnClickListener {
//            val intent = Intent(this, AssistanceActivity::class.java)
//            intent.putExtra("orgId", orgId)
//            startActivity(intent)
            Toast.makeText(this, "Assistance", Toast.LENGTH_SHORT).show()
        }

    }

}