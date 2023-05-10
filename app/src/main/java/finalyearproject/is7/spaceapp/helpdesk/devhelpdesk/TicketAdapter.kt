package finalyearproject.is7.spaceapp.helpdesk.devhelpdesk

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import finalyearproject.is7.spaceapp.R

class TicketAdapter(private val context: Context, private val TicketList: ArrayList<Ticket>):
    RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.ticket_layout, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val currentTicket = TicketList[position]

        holder.textTicket.text = currentTicket.description
        holder.textStatus.text = currentTicket.status

//        holder.textDescription.text = currentTicket.description
//        holder.textStatus.text = currentTicket.status
//        holder.textUser.text = currentTicket.user
//        holder.textTimestamp.text = currentTicket.timestamp.toString()

        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Clicked on ticket: ${currentTicket.id}", Toast.LENGTH_SHORT).show()
//            val intent = Intent(context, TicketDetailActivity::class.java)
//
//            intent.putExtra("id", currentTicket.id)
//            intent.putExtra("description", currentTicket.description)
//            intent.putExtra("status", currentTicket.status)
//            intent.putExtra("user", currentTicket.user)
//            intent.putExtra("timestamp", currentTicket.timestamp)
//
//            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return TicketList.size
    }

    class TicketViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textTicket: TextView = itemView.findViewById(R.id.txtTicket)
        val textStatus: TextView = itemView.findViewById(R.id.txtTicketStatus)
//        val textDescription: TextView = itemView.findViewById(R.id.textDescription)
//        val textStatus: TextView = itemView.findViewById(R.id.textStatus)
//        val textUser: TextView = itemView.findViewById(R.id.textUser)
//        val textTimestamp: TextView = itemView.findViewById(R.id.textTimestamp)
    }
}