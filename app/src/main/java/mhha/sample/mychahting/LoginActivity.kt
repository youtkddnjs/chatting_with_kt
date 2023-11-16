package mhha.sample.mychahting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.messaging.messaging
import mhha.sample.mychahting.Key.Companion.DB_URL
import mhha.sample.mychahting.Key.Companion.DB_USERS
import mhha.sample.mychahting.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //회원 가입 버튼
        binding.signUpButton.setOnClickListener{
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                Firebase.auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){
                        if(it.isSuccessful){
                            //회원가입 성공
                            Toast.makeText(this,"회원가입 성공.",Toast.LENGTH_SHORT).show()
                        }else{
                            //회원가입 실패
                            Toast.makeText(this,"회원가입 실패.",Toast.LENGTH_SHORT).show()
                            Log.e("SingUp", it.exception.toString())
                        }
                    }
            } else {
                Toast.makeText(this,"입력 해주세요.",Toast.LENGTH_SHORT).show()
            }
        }//binding.signUpButton.setOnClickListener

        //로그인 버튼
        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                Firebase.auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){
                        val currenUser = Firebase.auth.currentUser
                        if(it.isSuccessful && currenUser != null ){
                            //로그인 성공
                            Toast.makeText(this,"로그인 성공.",Toast.LENGTH_SHORT).show()
                            val userId = currenUser.uid

                            Firebase.messaging.token.addOnCompleteListener {
                                val token = it.result
                                val user = mutableMapOf<String, Any>()
                                user["userId"] = userId
                                user["userName"] = email
                                user["fcmToken"] = token
                                Firebase.database(DB_URL).reference.child(DB_USERS).child(userId).updateChildren(user)

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }.addOnFailureListener { 
                                //확인해볼것
                            }
                        }else{
                            //로그인 실패
                            Toast.makeText(this,"로그인 실패.",Toast.LENGTH_SHORT).show()
                            Log.d("Login", it.exception.toString())
                        }
                    }
            } else {
                Toast.makeText(this,"입력 해주세요.",Toast.LENGTH_SHORT).show()
            }
        } //binding.signInButton.setOnClickListener

    } //override fun onCreate(savedInstanceState: Bundle?)
}//class LoginActivity: AppCompatActivity()