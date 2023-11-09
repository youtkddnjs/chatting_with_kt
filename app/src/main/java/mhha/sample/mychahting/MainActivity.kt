package mhha.sample.mychahting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import mhha.sample.mychahting.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = Firebase.auth.currentUser

        if( currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    } //override fun onCreate(savedInstanceState: Bundle?)
} //class MainActivity : AppCompatActivity()