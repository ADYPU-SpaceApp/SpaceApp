package finalyearproject.is7.spaceapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoticeAdapter(val context: Context, private val noticeList: ArrayList<Notice>):
    RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.object_layout_in_activity, parent, false)
        return NoticeViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val currentNotice = noticeList[position]

        holder.textName.text = currentNotice.title

        holder.itemView.setOnClickListener {
            val intent = Intent(context,NoticeActivity::class.java)
            intent.putExtra("note",currentNotice.note)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    class NoticeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textName: TextView = itemView.findViewById(R.id.txt_name)
    }
}
