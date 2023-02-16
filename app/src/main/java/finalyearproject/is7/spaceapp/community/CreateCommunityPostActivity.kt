package finalyearproject.is7.spaceapp.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import finalyearproject.is7.spaceapp.databinding.ActivityCreateCommunityPostBinding

class CreateCommunityPostActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateCommunityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCommunityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }

}
