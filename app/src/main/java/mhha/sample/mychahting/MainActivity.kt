package mhha.sample.mychahting

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import mhha.sample.mychahting.chatlist.ChatListFragment
import mhha.sample.mychahting.databinding.ActivityMainBinding
import mhha.sample.mychahting.mypage.MyPageFragmnet
import mhha.sample.mychahting.userlist.UserFrgment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userFragment = UserFrgment()
    private val chatListFragment = ChatListFragment()
    private val myPageFragment = MyPageFragmnet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = Firebase.auth.currentUser

        Log.d("Permission", "${Build.VERSION.SDK_INT}")
        askNotificationPermission()

        if( currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }



        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.userList->{
                    replaceFragment(userFragment)
                    supportActionBar?.title = "친구"
                    return@setOnItemSelectedListener true
                }
                R.id.chatroomList->{
                    replaceFragment(chatListFragment)
                    supportActionBar?.title = "채팅"
                    return@setOnItemSelectedListener true
                }
                R.id.myPage->{
                    replaceFragment(myPageFragment)
                    supportActionBar?.title = "마이페이지"
                    return@setOnItemSelectedListener true
                }
                else -> { return@setOnItemSelectedListener false}
            }//when(it.itemId)
        }//binding.bottomNavigationView.setOnItemSelectedListener
        replaceFragment(userFragment)
        supportActionBar?.title = "친구"
    } //override fun onCreate(savedInstanceState: Bundle?)

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }//private fun replaceFragment(fragment: Fragment)

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // 알림 없음.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
                Log.d("Permission", "PackageManager.PERMISSION_GRANTED")
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                showPermissionRationaleDialog()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    private fun showPermissionRationaleDialog(){
        Log.d("Permission", "showPermissionRationaleDialog()")
        AlertDialog.Builder(this)
            .setMessage("알림 권한 필수")
            .setPositiveButton("권한 허용 하기"){ _, _ ->
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }.setNegativeButton("취소") { dialoginterface , _ -> dialoginterface.cancel()}
            .show()
    }

} //class MainActivity : AppCompatActivity()