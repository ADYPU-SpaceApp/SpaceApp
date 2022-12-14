package finalyearproject.is7.spaceapp

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class NoticeBoardActivity : AppCompatActivity() {

    private lateinit var noNotice: TextView
    private lateinit var loading: ProgressBar
    private lateinit var noticeRecyclerView: RecyclerView
    private lateinit var noticeList: ArrayList<Notice>
//    private lateinit var adapter: NoticeAdapter
    private lateinit var mDb: FirebaseFirestore
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_board)

        val orgId = intent.getStringExtra("orgId")!!

        mDb = FirebaseFirestore.getInstance()
        mDbRef = Firebase.database.getReference(orgId)

        noNotice = findViewById(R.id.NoNoticeText)
        noticeRecyclerView = findViewById(R.id.noticeRecyclerView)
        loading = findViewById(R.id.loading)
        loading.visibility = ProgressBar.VISIBLE
        noNotice.visibility = TextView.INVISIBLE
        noticeList = ArrayList()

        noticeRecyclerView.layoutManager = LinearLayoutManager(this)
        noticeRecyclerView.adapter = NoticeAdapter(this,noticeList,orgId)

        mDbRef.child("Notice").get()
            .addOnSuccessListener { notice ->
            for (n in notice.children) {
                if (n.child("is_Active").value == true) {
                    Log.d("Notice", n.child("title").value.toString())
                    val id = n.key
                    val title = n.child("title").value.toString()
                    val body = n.child("body").value.toString()
                    val createdById = n.child("created_By").value.toString()
//                    val createdBy = ""
//                    mDb.collection("User").document(createdById).get()
//                        .addOnSuccessListener {
//                            createdBy = it.data!!["name"] as String
//                        }
                    val createdAt = n.child("created_At").value.toString()
                    val updatedBy = n.child("updated_By").value.toString()
                    val updatedAt = n.child("updated_At").value.toString()
                    noticeList.add(
                        Notice(id, title, body,
                            createdById, createdAt, updatedBy, updatedAt)
                    )
                }
            }
            loading.visibility = ProgressBar.GONE
            noticeRecyclerView.adapter = NoticeAdapter(this,noticeList,orgId)
            if (noticeList.isEmpty()){
                noNotice.visibility = TextView.VISIBLE
            }
        }

    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }
}