package mhha.sample.mychahting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mhha.sample.mychahting.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    } //override fun onCreate(savedInstanceState: Bundle?)
} //class MainActivity : AppCompatActivity()