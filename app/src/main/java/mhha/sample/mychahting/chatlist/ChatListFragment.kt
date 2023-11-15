package mhha.sample.mychahting.chatlist

import android.content.Intent
import android.os.Bundle
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
import mhha.sample.mychahting.databinding.FragmentChatlistBinding
import mhha.sample.mychahting.databinding.FragmentUserlistBinding

class ChatListFragment: Fragment(R.layout.fragment_chatlist) {

    private lateinit var binding: FragmentChatlistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatlistBinding.bind(view)

        val chatListAdapter = ChatListAdpter{chatRoomItem ->
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("otherUserId", chatRoomItem.otherUserId)
            intent.putExtra("chatRoomId", chatRoomItem.chatRoomId)
            startActivity(intent)
        }
        binding.chatListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatListAdapter
        } //binding.userListRecyclerView.apply

        val currentUserId = Firebase.auth.currentUser?.uid ?: return
        val chatRoomsDB = Firebase.database.reference.child(Key.DB_CHAT_ROOMS).child(currentUserId)

        chatRoomsDB.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatRoomList = snapshot.children.map {
                    it.getValue(ChatRoomItem::class.java)
                }
                chatListAdapter.submitList(chatRoomList)
            }//override fun onDataChange(snapshot: DataSnapshot)

            override fun onCancelled(error: DatabaseError) {

            }//override fun onCancelled(error: DatabaseError)
        })//chatRoomsDB.addValueEventListener(object: ValueEventListener


//        chatListAdapter.submitList(
//            mutableListOf<ChatRoomItem?>().apply {
//                add(ChatRoomItem("11","22","33"))
//            }
//        )

    } //override fun onViewCreated(view: View, savedInstanceState: Bundle?)
} //class UserFrgment: Fragment(R.layout.fragment_userlist)