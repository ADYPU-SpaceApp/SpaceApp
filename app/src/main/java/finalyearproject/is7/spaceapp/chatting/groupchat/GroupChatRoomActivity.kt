package finalyearproject.is7.spaceapp.chatting.groupchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import finalyearproject.is7.spaceapp.databinding.ActivityGroupChatRoomBinding

class GroupChatRoomActivity: AppCompatActivity() {

    private lateinit var binding: ActivityGroupChatRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orgId = intent.getStringExtra("orgId").toString()
        val groupId = intent.getStringExtra("groupId").toString()


    }

}