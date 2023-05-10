package finalyearproject.is7.spaceapp.user

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.R
import finalyearproject.is7.spaceapp.databinding.ActivityErpBinding

class ERPActivity: AppCompatActivity() {

    private lateinit var binding: ActivityErpBinding

    private lateinit var webView: WebView
    private var mDb = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityErpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orgId = intent.getStringExtra("orgId")!!

        webView = findViewById(R.id.erpWebView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }
        mDb.collection("Organisation").document(orgId).get()
            .addOnSuccessListener {
                val url = it["erp"] as String
                webView.loadUrl(url)
                binding.loading.visibility = android.widget.ProgressBar.GONE
        }

    }
}