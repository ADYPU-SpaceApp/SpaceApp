package finalyearproject.is7.spaceapp.chatting.groupchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import finalyearproject.is7.spaceapp.databinding.ActivityCreateGroupChatBinding

class CreateGroupChatActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateGroupChatBinding

    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = FirebaseFirestore.getInstance()
    private lateinit var mDbRef: DatabaseReference
    private var org: DocumentReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orgId = intent.getStringExtra("orgId").toString()
        org = mDb.collection("Organisation").document(orgId)
        mDbRef = Firebase.database.getReference(orgId)

        binding.searchViewSelectGroupMembers.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.btnCreateGroup.setOnClickListener {
            val groupName = binding.edtGroupName.text.toString()
            val groupMembers = null
            val groupMembersList = null // groupMembers.split(",")

            val groupChat = GroupChat(groupName, groupMembersList, orgId)
        }

    }

}