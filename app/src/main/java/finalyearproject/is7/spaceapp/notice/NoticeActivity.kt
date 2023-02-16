@file:Suppress("DEPRECATION")

package finalyearproject.is7.spaceapp.notice

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import finalyearproject.is7.spaceapp.R
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class NoticeActivity : AppCompatActivity() {

    private lateinit var pdfView: PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        val note = intent.getStringExtra("note")!!

        pdfView = findViewById(R.id.pdfView)
        RetrievePDFFromURL(pdfView).execute(note)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    class RetrievePDFFromURL(pdfView: PDFView) : AsyncTask<String, Void, InputStream>() {

        @SuppressLint("StaticFieldLeak")
        private var myPdfView: PDFView = pdfView

        // on below line we are calling our do in background method.
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: String?): InputStream? {
            // on below line we are creating a variable for our input stream.
            var inputStream: InputStream? = null
            try {
                // on below line we are creating an url
                // for our url which we are passing as a string.
                val url = URL(params[0])

                // on below line we are creating our http url connection.
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection

                // on below line we are checking if the response
                // is successful with the help of response code
                // 200 response code means response is successful
                if (urlConnection.responseCode == 200) {
                    // on below line we are initializing our input stream
                    // if the response is successful.
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            }
            // on below line we are adding catch block to handle exception
            catch (e: Exception) {
                // on below line we are simply printing
                // our exception and returning null
                e.printStackTrace()
                return null
            }
            // on below line we are returning input stream.
            return inputStream
        }

        // on below line we are calling on post execute
        // method to load the url in our pdf view.
        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: InputStream?) {
            // on below line we are loading url within our
            // pdf view on below line using input stream.
            myPdfView.fromStream(result).load()
        }
    }
}
