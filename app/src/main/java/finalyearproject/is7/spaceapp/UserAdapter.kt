package finalyearproject.is7.spaceapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import finalyearproject.is7.spaceapp.chatting.privatechat.AddChatActivity
import finalyearproject.is7.spaceapp.chatting.privatechat.PrivateChatRoomActivity
import finalyearproject.is7.spaceapp.org.OrgDetailActivity
import finalyearproject.is7.spaceapp.user.OtherUserProfileActivity
import finalyearproject.is7.spaceapp.user.UserDetailActivity

class UserAdapter(private val context: Context, private val userList: ArrayList<User>, private val orgId: String):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        if (currentUser.displaypic != "") {
            Glide.with(context).load(currentUser.displaypic).circleCrop().into(holder.displayPic)
        } else {
            Glide.with(context).load(R.drawable.profile).circleCrop().into(holder.displayPic)
        }

        holder.textName.text = currentUser.name

        holder.displayPic.setOnClickListener {
            val intent = Intent(context, OtherUserProfileActivity::class.java)

            intent.putExtra("otherUserUID", currentUser.uid)

            context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
            when (orgId) {
                "" -> {
                    val intent = Intent(context, UserDetailActivity::class.java)

                    intent.putExtra("name", currentUser.name)
                    intent.putExtra("uid", currentUser.uid)
                    intent.putExtra("displaypic", currentUser.displaypic)

                    context.startActivity(intent)
                }
                "org" -> {
                    val intent = Intent(context, OrgDetailActivity::class.java)

                    intent.putExtra("name", currentUser.name)
                    intent.putExtra("uid", currentUser.uid)
                    intent.putExtra("displaypic", currentUser.displaypic)

                    context.startActivity(intent)
                }
                else -> {
                    val intent = Intent(context, PrivateChatRoomActivity::class.java)

                    intent.putExtra("name", currentUser.name)
                    intent.putExtra("uid", currentUser.uid)
                    intent.putExtra("displaypic", currentUser.displaypic)

                    intent.putExtra("orgId", orgId)

                    context.startActivity(intent)
                }
            }

            if (context is AddChatActivity) {
                context.finish()
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val displayPic: ImageView = itemView.findViewById(R.id.txt_picture)
        val textName = itemView.findViewById<TextView>(R.id.txt_name)!!
    }
}
