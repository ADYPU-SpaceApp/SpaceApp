package finalyearproject.is7.spaceapp.helpdesk.devhelpdesk

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalyearproject.is7.spaceapp.databinding.ActivityCreateTicketBinding

class CreateTicketActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateTicketBinding

    private val mAuth = FirebaseAuth.getInstance()
    private val mDb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreateTicket.setOnClickListener {
            if (binding.edtCreateTicketDescription.text.toString().isEmpty()) {
                binding.edtCreateTicketDescription.error = "Please enter a description"
                binding.edtCreateTicketDescription.requestFocus()
            }
            else  {
                val userUid = mAuth.currentUser?.uid
                val description = binding.edtCreateTicketDescription.text.toString()
                val ticket = hashMapOf(
                    "description" to description,
                    "status" to "Open",
                    "user" to userUid,
                    "timestamp" to System.currentTimeMillis()
                )

                mDb.collection("DevTickets").add(ticket)
                    .addOnSuccessListener {
                        val ticketId = it.id
                        Toast.makeText(this, "Ticket created", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Ticket ID: $ticketId", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }

    }

}