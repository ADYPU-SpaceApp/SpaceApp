package finalyearproject.is7.spaceapp.community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import finalyearproject.is7.spaceapp.databinding.ActivityCommunityBinding

class CommunityActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCommunityBinding

    private var mAuth = Firebase.auth
    private var mDb = FirebaseFirestore.getInstance()
    private lateinit var mDbRef: DatabaseReference

    private lateinit var communityPostList: ArrayList<CommunityPost>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orgId = intent.getStringExtra("orgId")
        mDbRef = Firebase.database.getReference(orgId!!)

        binding.loading.visibility = ProgressBar.VISIBLE
        binding.noPostTextCommunity.visibility = TextView.INVISIBLE

//        binding.btnBackCommunity.setOnClickListener {
//            finish()
//        }

//        binding.btnVedioConference.setOnClickListener {
//            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
//        }

        mDb.collection("User").document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { documents ->

                val communityAdmin = documents.get("communityAdmin").toString()

                Log.d("CommunityAdmin", communityAdmin)

                if (communityAdmin != "" && communityAdmin != "null") {
                    binding.btnAddPost.visibility = android.view.View.VISIBLE
                    binding.btnAddPost.setOnClickListener {
                        val createCommunityPostIntent = Intent(this, CreateCommunityPostActivity::class.java)
                        createCommunityPostIntent.putExtra("orgId", orgId)
                        createCommunityPostIntent.putExtra("community", communityAdmin)
                        startActivity(createCommunityPostIntent)
//                        Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    binding.btnAddPost.visibility = android.view.View.GONE
                }

                communityPostList = ArrayList()
                mDbRef.child("Community").child("posts").get()
                    .addOnSuccessListener {
                        for (document in it.children) {
                            if (document.child("is_Active").value == true) {

                                val id = document.key
                                val communityId = document.child("communityId").value.toString()
                                val post = document.child("post").value.toString()
                                val caption = document.child("caption").value.toString()
                                val createdBy = document.child("createdBy").value.toString()
                                val createdAt = document.child("createdAt").value.toString()
                                val isActive = document.child("is_Active").value

                                communityPostList.add(CommunityPost(id, communityId, post, caption, createdBy, createdAt, isActive as Boolean))
                            }
                        }

                        binding.loading.visibility = ProgressBar.GONE

                        binding.communityPostRecyclerView.adapter = CommunityPostAdapter(this,communityPostList, orgId)
                        binding.communityPostRecyclerView.layoutManager = LinearLayoutManager(this)

                        if (communityPostList.isEmpty()){
                            binding.noPostTextCommunity.visibility = TextView.VISIBLE
                        }

                    }


            }

    }
}