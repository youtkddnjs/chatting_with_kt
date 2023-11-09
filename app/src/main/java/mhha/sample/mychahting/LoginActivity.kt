package mhha.sample.mychahting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import mhha.sample.mychahting.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                Firebase.auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){
                        if(it.isSuccessful){
                            //로그인 성공
                            Toast.makeText(this,"로그인 성공.",Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()

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