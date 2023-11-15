package mhha.sample.mychahting.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mhha.sample.mychahting.databinding.ItemChatroomBinding
import mhha.sample.mychahting.databinding.ItemUserBinding

class ChatListAdpter(private val onClick: (ChatRoomItem) -> Unit ): ListAdapter<ChatRoomItem, ChatListAdpter.ViewHolder>(differ) {


    inner class ViewHolder(private  val binding: ItemChatroomBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ChatRoomItem){
            binding.nicknameTextView.text= item.otherUserName
            binding.lastMessageTextView.text = item.lastMessage

            binding.root.setOnClickListener {
                onClick
            }

        }//fun bind(item: UserItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatroomBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val differ = object : DiffUtil.ItemCallback<ChatRoomItem>(){
            override fun areItemsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean {
                return oldItem.chatRoomID == newItem.chatRoomID
            }

            override fun areContentsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean {
                return oldItem == newItem
            }
        }
    }//companion object


}