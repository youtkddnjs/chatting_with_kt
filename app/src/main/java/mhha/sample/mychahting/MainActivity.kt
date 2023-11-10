package mhha.sample.mychahting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import mhha.sample.mychahting.databinding.ActivityMainBinding
import mhha.sample.mychahting.userlist.UserFrgment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userFragment = UserFrgment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = Firebase.auth.currentUser

        if( currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.userList->{ return@setOnItemSelectedListener true}
                R.id.chatroomList->{ return@setOnItemSelectedListener true}
                R.id.myPage->{ return@setOnItemSelectedListener true}
                else -> { return@setOnItemSelectedListener false}
            }//when(it.itemId)
        }//binding.bottomNavigationView.setOnItemSelectedListener

    } //override fun onCreate(savedInstanceState: Bundle?)

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }//private fun replaceFragment(fragment: Fragment)

} //class MainActivity : AppCompatActivity()