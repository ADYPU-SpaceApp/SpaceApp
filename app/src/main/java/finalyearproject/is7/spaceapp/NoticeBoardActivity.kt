package finalyearproject.is7.spaceapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class NoticeBoardActivity : AppCompatActivity() {

    private lateinit var noNotice: TextView
    private lateinit var createNoticeBtn: Button
    private lateinit var loading: ProgressBar
    private lateinit var noticeRecyclerView: RecyclerView
    private lateinit var noticeList: ArrayList<Notice>

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDb: FirebaseFirestore
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_board)

        val orgId = intent.getStringExtra("orgId")!!

        mAuth = FirebaseAuth.getInstance()
        mDb = FirebaseFirestore.getInstance()
        mDbRef = Firebase.database.getReference(orgId)

        createNoticeBtn = findViewById(R.id.communityButton)
        noNotice = findViewById(R.id.NoNoticeText)
        noticeRecyclerView = findViewById(R.id.noticeRecyclerView)
        loading = findViewById(R.id.loading)
        loading.visibility = ProgressBar.VISIBLE
        noNotice.visibility = TextView.INVISIBLE
        noticeList = ArrayList()
        
        mDb.collection("User").document(mAuth.currentUser?.uid!!).get()
            .addOnSuccessListener { userDoc ->
                val role = userDoc.data?.get("role").toString()
                mDb.collection("Role").document(role).get()
                    .addOnSuccessListener { roleDoc ->
                        if (roleDoc.data?.get("is_Staff") == true) {
                            createNoticeBtn.setOnClickListener {

                                val goToCreateNoticeActivityIntent =
                                    Intent(this, CreateNoticeActivity::class.java)
                                goToCreateNoticeActivityIntent.putExtra("orgId", orgId)
                                startActivity(goToCreateNoticeActivityIntent)
                            }
                        }
                        else {
                            createNoticeBtn.isEnabled = false
                            createNoticeBtn.visibility = Button.GONE
                        }
                    }
            }
        
        noticeRecyclerView.layoutManager = LinearLayoutManager(this)
        noticeRecyclerView.adapter = NoticeAdapter(this,noticeList,orgId)

        mDbRef.child("Notice").get()
            .addOnSuccessListener { notice ->
            for (n in notice.children) {
                if (n.child("is_Active").value == true) {
                    Log.d("Notice", n.child("title").value.toString())

                    val id = n.key
                    val title = n.child("title").value.toString()
                    val note = n.child("note").value.toString()

                    noticeList.add(Notice(id!!,title,note))
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

    override fun onResume() {
        super.onResume()
        finish()
        startActivity(intent)
    }

}