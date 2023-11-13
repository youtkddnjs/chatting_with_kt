package mhha.sample.mychahting.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mhha.sample.mychahting.LoginActivity
import mhha.sample.mychahting.R
import mhha.sample.mychahting.databinding.FragmentMypageBinding

class MyPageFragmnet: Fragment(R.layout.fragment_mypage) {

    private lateinit var binding:FragmentMypageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMypageBinding.bind(view)

        binding.applyButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            if(username.isEmpty()){
                Toast.makeText(context, "유저 이름을 입력 하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // todo 파이어베이스 realtime database update

        }//binding.applyButton.setOnClickListener

        binding.signOutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }

    }//override fun onViewCreated(view: View, savedInstanceState: Bundle?)

}