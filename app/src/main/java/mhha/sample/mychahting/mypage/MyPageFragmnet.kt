package mhha.sample.mychahting.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mhha.sample.mychahting.Key
import mhha.sample.mychahting.LoginActivity
import mhha.sample.mychahting.R
import mhha.sample.mychahting.databinding.FragmentMypageBinding
import mhha.sample.mychahting.userlist.UserItem

class MyPageFragmnet: Fragment(R.layout.fragment_mypage) {

    private lateinit var binding:FragmentMypageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMypageBinding.bind(view)

        val currentUserId = Firebase.auth.currentUser?.uid ?: ""
        val currentUserDB = Firebase.database.reference.child(Key.DB_USERS).child(currentUserId)

        currentUserDB.get().addOnSuccessListener {
            val currentUserItem = it.getValue(UserItem::class.java) ?: return@addOnSuccessListener

            binding.usernameEditText.setText(currentUserItem.userName)
            binding.descriptionEditText.setText(currentUserItem.description)
        }

        binding.applyButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            if(username.isEmpty()){
                Toast.makeText(context, "유저 이름을 입력 하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            val user = mutableMapOf<String,Any>()
            user["userName"] = username
            user["description"] = description
            currentUserDB.updateChildren(user)

        }//binding.applyButton.setOnClickListener

        binding.signOutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }

    }//override fun onViewCreated(view: View, savedInstanceState: Bundle?)

}