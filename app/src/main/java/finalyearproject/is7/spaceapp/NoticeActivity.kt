package finalyearproject.is7.spaceapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class NoticeActivity : AppCompatActivity() {

    private lateinit var displayid: TextView
    private lateinit var displaytitle: TextView
    private lateinit var displaybody: TextView
    private lateinit var displayauthor: TextView
    private lateinit var displayupdatedAt: TextView
    private lateinit var displaycreatedAt: TextView
    private lateinit var removeBtn: Button

    private lateinit var mDbRef: DatabaseReference

    var mAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        val orgId = intent.getStringExtra("orgId")
        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")
        val author = intent.getStringExtra("author")
        val createdAt = intent.getStringExtra("createdAt")
        val updatedAt = intent.getStringExtra("updatedAt")

        mDbRef = Firebase.database.getReference(orgId!!)
        // convert time to readable timestamp format
        val createdAtTimestamp = createdAt?.toLong()
        val updatedAtTimestamp = updatedAt?.toLong()

        displayid = findViewById(R.id.noticeId)
        displaytitle = findViewById(R.id.noticeTitle)
        displaybody = findViewById(R.id.noticeBody)
        displayauthor = findViewById(R.id.noticeAuthor)
        displayupdatedAt = findViewById(R.id.noticeUpdatedOn)
        displaycreatedAt = findViewById(R.id.noticeCreatedOn)
        removeBtn = findViewById(R.id.removeNotice)

        val updated = "Last Updated On: $createdAtTimestamp"
        val created = "Created On: $updatedAtTimestamp"

        displayid.text = id
        displaytitle.text = title
        displaybody.text = body
        displayauthor.text = author
        displayupdatedAt.text = updated
        displaycreatedAt.text = created

        db.collection("User").document(mAuth.currentUser?.uid!!).get()
            .addOnSuccessListener { userDoc ->
                val role: DocumentReference = userDoc.data?.get("role") as DocumentReference
                role.get()
                    .addOnSuccessListener { roleDoc ->
                        if (roleDoc.data?.get("is_Staff") == true) {
                            removeBtn.setOnClickListener {
                                AlertDialog.Builder(this)
                                    .setTitle("Delete Notice")
                                    .setMessage("Are you sure you want to delete this notice?")
                                    .setPositiveButton("Yes") { _, _ ->
                                        // update is_Active to false
                                        mDbRef.child("Notice").child(id!!).child("is_Active")
                                            .setValue(false)
                                        finish()
                                    }
                                    .setNegativeButton("No") { _, _ -> }
                                    .show()
                            }
                        }
                        else {
                            removeBtn.isEnabled = false
                            removeBtn.visibility = Button.GONE
                        }
                    }
            }

    }

}
