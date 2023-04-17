package finalyearproject.is7.spaceapp.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import finalyearproject.is7.spaceapp.databinding.ActivityCreatingPostBinding

class CreatingPostActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreatingPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatingPostBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}