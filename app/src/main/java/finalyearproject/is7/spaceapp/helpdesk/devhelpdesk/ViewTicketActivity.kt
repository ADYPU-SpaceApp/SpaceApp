package finalyearproject.is7.spaceapp.helpdesk.devhelpdesk

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.databinding.ActivityViewTicketBinding

class ViewTicketActivity: AppCompatActivity() {

    private lateinit var binding: ActivityViewTicketBinding

    private lateinit var ticketList: ArrayList<Ticket>
    private lateinit var ticketAdapter: TicketAdapter

    private val mAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loading.visibility = android.view.View.VISIBLE

        ticketList = ArrayList()
        ticketAdapter = TicketAdapter(this, ticketList)
        binding.viewTicketRecycleView.layoutManager = LinearLayoutManager(this)
        binding.viewTicketRecycleView.adapter = ticketAdapter

        mDb.collection("DevTickets").whereEqualTo("user", mAuth.currentUser?.uid).get()
            .addOnSuccessListener { tickets ->
                ticketList.clear()
                for (ticket in tickets) {
                    val t = Ticket(
                        ticket.id,
                        ticket.data["description"].toString(),
                        ticket.data["status"].toString(),
                        ticket.data["user"].toString(),
                        ticket.data["timestamp"].toString().toLong()
                    )
                    ticketList.add(t)
                }
                binding.loading.visibility = android.view.View.GONE
                Log.d("TAG", "onCreate: ${ticketList.size}")
                ticketAdapter.notifyDataSetChanged()
            }

    }

}