package finalyearproject.is7.spaceapp.user

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import finalyearproject.is7.spaceapp.databinding.ActivityOtherUserProfileBinding

class OtherUserProfileActivity: AppCompatActivity() {

    private lateinit var binding: ActivityOtherUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val otherUserUID = intent.getStringExtra("otherUserUID")

        Log.d("OtherUserUID", otherUserUID!!)
    }

}