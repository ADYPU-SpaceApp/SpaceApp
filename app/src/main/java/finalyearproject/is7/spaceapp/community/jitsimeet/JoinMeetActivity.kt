package finalyearproject.is7.spaceapp.community.jitsimeet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import finalyearproject.is7.spaceapp.databinding.ActivityJoinMeetBinding

class JoinMeetActivity: AppCompatActivity() {

    private lateinit var binding: ActivityJoinMeetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinMeetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnJoinMeet.setOnClickListener{
            val roomCode = binding.edtRoomId.text.toString()
            if (roomCode.isEmpty() || roomCode.length < 6 || roomCode.length > 6){
                binding.edtRoomId.error = "Please enter a room code"
                binding.edtRoomId.requestFocus()
                return@setOnClickListener
            } else {

            }
        }


    }

}