package mhha.sample.mychahting.chatlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import mhha.sample.mychahting.R
import mhha.sample.mychahting.databinding.FragmentChatlistBinding
import mhha.sample.mychahting.databinding.FragmentUserlistBinding

class ChatListFragment: Fragment(R.layout.fragment_chatlist) {

    private lateinit var binding: FragmentChatlistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatlistBinding.bind(view)

        val chatListAdapter = ChatListAdpter()
        binding.chatListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatListAdapter
        } //binding.userListRecyclerView.apply

        chatListAdapter.submitList(
            mutableListOf<ChatRoomItem?>().apply {
                add(ChatRoomItem("11","22","33"))
            }
        )

    } //override fun onViewCreated(view: View, savedInstanceState: Bundle?)
} //class UserFrgment: Fragment(R.layout.fragment_userlist)