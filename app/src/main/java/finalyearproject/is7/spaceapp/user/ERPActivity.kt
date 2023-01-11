package finalyearproject.is7.spaceapp.user

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import finalyearproject.is7.spaceapp.R

class ERPActivity: AppCompatActivity() {

    private lateinit var viewMarksBtn: Button
    private lateinit var viewAttendanceBtn: Button
    private lateinit var viewTimeTableBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_erp)

        viewMarksBtn = findViewById(R.id.viewMarksBtn)
        viewAttendanceBtn = findViewById(R.id.viewAttendanceBtn)
        viewTimeTableBtn = findViewById(R.id.viewTimeTableBtn)

//        viewMarksBtn.setOnClickListener {
//            val intent = Intent(this, ViewMarksActivity::class.java)
//            startActivity(intent)
//        }
//
//        viewAttendanceBtn.setOnClickListener {
//            val intent = Intent(this, ViewAttendanceActivity::class.java)
//            startActivity(intent)
//        }
//
//        viewTimeTableBtn.setOnClickListener {
//            val intent = Intent(this, ViewTimeTableActivity::class.java)
//            startActivity(intent)
//        }

    }

}