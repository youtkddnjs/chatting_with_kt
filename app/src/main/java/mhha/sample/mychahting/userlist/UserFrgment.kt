package mhha.sample.mychahting.userlist

import android.content.Intent
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
import mhha.sample.mychahting.chatdetail.ChatActivity
import mhha.sample.mychahting.chatlist.ChatRoomItem
import mhha.sample.mychahting.databinding.FragmentUserlistBinding
import java.util.UUID

class UserFrgment: Fragment(R.layout.fragment_userlist) {

    private lateinit var binding: FragmentUserlistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserlistBinding.bind(view)

        val userListAdapter = UserAdapter{ otherUser ->

            var myUserId = Firebase.auth.currentUser?.uid ?: ""
            val chatRoomDB = Firebase.database.reference.child(Key.DB_CHAT_ROOMS).child(myUserId).child(otherUser.userId ?: "")
            chatRoomDB.get().addOnSuccessListener {
                var chatRoomId= ""
                if(it.value != null){
                    //해당하는 방이 있음.
                    val chatRoom = it.getValue(ChatRoomItem::class.java)
                    chatRoomId = chatRoom?.chatRoomId ?: ""
                }else{
                    chatRoomId = UUID.randomUUID().toString()
                    var newChatRoom = ChatRoomItem(
                        chatRoomId = chatRoomId,
                        otherUserName = otherUser.userName,
                        otherUserId = otherUser.userId,
                    )
                    chatRoomDB.setValue(newChatRoom)
                }
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("otherUserId", otherUser.userId)
                intent.putExtra("chatRoomId", chatRoomId)
                startActivity(intent)
            } //chatRoomDB.get().addOnSuccessListener
        } //val userListAdapter = UserAdapter


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