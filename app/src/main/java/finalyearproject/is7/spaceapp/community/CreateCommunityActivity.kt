package finalyearproject.is7.spaceapp.community

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.databinding.ActivityCreateCommunityBinding

class CreateCommunityActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateCommunityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orgId = intent.getStringExtra("orgId")

        val leaderList = ArrayList<String>()
        val mDb = FirebaseFirestore.getInstance()
        mDb.collection("User").whereEqualTo("org", orgId).get().addOnSuccessListener { result ->
            for (document in result) {
                leaderList.add(document.id)
            }
            Log.d("TAG", "onCreate: $leaderList")
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, leaderList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnSelectCommunityLeader.adapter = adapter
        }


        binding.btnCreateCommunity.setOnClickListener {
            val communityName = binding.communityNameEditText.text.toString()
            val communityDescription = binding.communityDescriptionEditText.text.toString()
            val communityLeader = binding.spnSelectCommunityLeader.selectedItem.toString()

            if (communityName.isEmpty()) {
                binding.communityNameEditText.error = "Community name is required"
                binding.communityNameEditText.requestFocus()
                return@setOnClickListener
            }

            if (communityDescription.isEmpty()) {
                binding.communityDescriptionEditText.error = "Community description is required"
                binding.communityDescriptionEditText.requestFocus()
                return@setOnClickListener
            }

            if (communityLeader.isEmpty()) {
                Toast.makeText(this, "Community leader is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val community = Community(communityName, communityDescription, communityLeader)

            val communityRef = mDb.collection("Organisation").document(orgId!!).collection("Community")
            val communityId = communityRef.document().id

            communityRef.document(communityId).set(community).addOnSuccessListener {
                mDb.collection("User").document(communityLeader).update("communityAdmin", communityId).addOnSuccessListener {
                    Toast.makeText(this, "Community created successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                    .addOnFailureListener {
                        Toast.makeText(this, "Community creation failed", Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(this, "Community creation failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

}
