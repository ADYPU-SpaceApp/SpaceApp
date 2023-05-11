package finalyearproject.is7.spaceapp.chatting.privatechat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import finalyearproject.is7.spaceapp.R
import finalyearproject.is7.spaceapp.chatting.Message
import finalyearproject.is7.spaceapp.chatting.MessageAdapter
import finalyearproject.is7.spaceapp.user.OtherUserProfileActivity

class PrivateChatRoomActivity:AppCompatActivity() {

    private lateinit var userPic: ImageView
    private lateinit var userName: TextView
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter

    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDb: FirebaseFirestore
    private lateinit var mDbRef: DatabaseReference

    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_chat_room)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        val orgId = intent.getStringExtra("orgId")
        val displaypic = intent.getStringExtra("displaypic")

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        mDb = FirebaseFirestore.getInstance()
        mDbRef = Firebase.database.getReference(orgId!!)

        userPic = findViewById(R.id.ReceiverImage)
        userName = findViewById(R.id.ReceiverName)

        if (displaypic != "") {
            Glide.with(this).load(displaypic).circleCrop().into(userPic)
        }
        else {
            Glide.with(this).load(R.drawable.profile).circleCrop().into(userPic)
        }

        userPic.setOnClickListener {
            val intent = Intent(this, OtherUserProfileActivity::class.java)
            intent.putExtra("otherUserUID", receiverUid)
            startActivity(intent)
        }

        userName.text = name

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        mDbRef.child("chats").child(senderRoom!!).child("message")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapShot in snapshot.children){
                        val message = postSnapShot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        // adding the message to database
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            val timestamp = System.currentTimeMillis()
            val messageObject = Message(message, timestamp, senderUid)

            if (message==""){
                Toast.makeText(this,"No message entered",Toast.LENGTH_SHORT).show()
                messageBox.setText("")
            } else {
                mDbRef.child("chats").child(senderRoom!!).child("message").push()
                    .setValue(messageObject)
                    .addOnSuccessListener {
                        mDbRef.child("chats").child(receiverRoom!!).child("message").push()
                            .setValue(messageObject)
//                            .addOnSuccessListener {
//                                mDbRef.child("chats").child(senderRoom!!).child("lastMessage")
//                                    .setValue(messageObject)
//                                    .addOnSuccessListener {
//                                        mDbRef.child("chats").child(receiverRoom!!).child("lastMessage")
//                                            .setValue(messageObject)
//                                    }
//                            }

//                            mDb.collection("User").document(senderUid!!)
//                                .collection("Chats").document(receiverUid!!)
//                                .update("lastMessage", messageObject)
//                                .addOnSuccessListener {
//                                    mDb.collection("User").document(receiverUid)
//                                        .collection("Chats").document(senderUid)
//                                        .update("lastMessage", messageObject)
//                        }
                    }

                messageBox.setText("")
            }
        }

    }
}