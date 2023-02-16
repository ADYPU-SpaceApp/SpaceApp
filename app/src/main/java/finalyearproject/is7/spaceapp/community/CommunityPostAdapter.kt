package finalyearproject.is7.spaceapp.community

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.R

class CommunityPostAdapter(private val context: Context, private val postList: ArrayList<CommunityPost>, private val orgId: String):
    RecyclerView.Adapter<CommunityPostAdapter.CommunityPostViewHolder>() {

    val mDb = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPostViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.community_post_layout, parent, false)
        return CommunityPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityPostViewHolder, position: Int) {
        val currentPost = postList[position]

        val community = currentPost.communityId

        mDb.collection("Organisation").document(orgId)
            .collection("Community").document(community!!).get().addOnSuccessListener {
                val communityName = it.getString("name")
                val communityDp = it.getString("displaypic")

                holder.communityName.text = communityName

                if (communityDp != "") {
                    Glide.with(context).load(communityDp).circleCrop().into(holder.communityDp)
                } else {
                    Glide.with(context).load(R.drawable.profile).circleCrop().into(holder.communityDp)
                }
        }

        Glide.with(context).load(currentPost.post).into(holder.communityPost)

        holder.communityPostCaption.text = currentPost.caption
        holder.communityPostTime.text = currentPost.createdAt

    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class CommunityPostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val communityDp: ImageView = itemView.findViewById(R.id.community_dp)
        val communityName: TextView = itemView.findViewById(R.id.community_name)
        val communityPost: ImageView = itemView.findViewById(R.id.community_post_image)
        val communityPostCaption: TextView = itemView.findViewById(R.id.community_post_description)
        val communityPostTime: TextView = itemView.findViewById(R.id.community_post_time)
    }

}