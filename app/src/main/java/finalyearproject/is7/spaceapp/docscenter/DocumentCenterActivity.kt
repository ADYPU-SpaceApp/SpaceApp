package finalyearproject.is7.spaceapp.docscenter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import finalyearproject.is7.spaceapp.databinding.ActivityDocumentCenterBinding
import finalyearproject.is7.spaceapp.docscenter.notice.NoticeBoardActivity
import finalyearproject.is7.spaceapp.user.ERPActivity

class DocumentCenterActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDocumentCenterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orgId = intent.getStringExtra("orgId")

        binding.btnNoticeBoard.setOnClickListener{
            val intent = Intent(this, NoticeBoardActivity::class.java)
            intent.putExtra("orgId", orgId)
            startActivity(intent)
        }

        binding.btnErp.setOnClickListener{
            val intent = Intent(this, ERPActivity::class.java)
            startActivity(intent)
        }

    }

}