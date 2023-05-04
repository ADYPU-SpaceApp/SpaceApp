package finalyearproject.is7.spaceapp.chatting.groupchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import finalyearproject.is7.spaceapp.R
import finalyearproject.is7.spaceapp.User

class GroupAdapter(private val userList: List<User>) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    private val selectedUsers = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_user_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)

        holder.itemView.setOnClickListener {
            if (selectedUsers.contains(user)) {
                selectedUsers.remove(user)
                holder.checkBox.isChecked = false
            } else {
                selectedUsers.add(user)
                holder.checkBox.isChecked = true
            }
        }
    }

    override fun getItemCount() = userList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkboxUser)

        fun bind(user: User) {
            // Bind user data to views here
        }
    }
}