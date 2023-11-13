package mhha.sample.mychahting.userlist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import mhha.sample.mychahting.Key
import mhha.sample.mychahting.R
import mhha.sample.mychahting.databinding.FragmentUserlistBinding

class UserFrgment: Fragment(R.layout.fragment_userlist) {

    private lateinit var binding: FragmentUserlistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserlistBinding.bind(view)

        val userListAdapter = UserAdapter()
        binding.userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        } //binding.userListRecyclerView.apply

        val currentUserId = Firebase.auth.currentUser?.uid ?: ""

        Firebase.database
            .reference
            .child(Key.DB_USERS)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userItemList = mutableListOf<UserItem>()
                    snapshot.children.forEach {
                        val user = it.getValue(UserItem::class.java)
                            user ?: return
                        if (user.userId != currentUserId){
                            Log.d("userlog", "${user}")
                            userItemList.add(user)
                        }
                    }
                    userListAdapter.submitList(userItemList)
                }//override fun onDataChange(snapshot: DataSnapshot)

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })//.addListenerForSingleValueEvent

//        userListAdapter.submitList(
//            mutableListOf<UserItem?>().apply {
//                add(UserItem("11","22","33"))
//            }
//        )

    } //override fun onViewCreated(view: View, savedInstanceState: Bundle?)
} //class UserFrgment: Fragment(R.layout.fragment_userlist)