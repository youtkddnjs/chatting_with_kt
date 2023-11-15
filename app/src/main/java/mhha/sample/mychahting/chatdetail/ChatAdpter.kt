package mhha.sample.mychahting.chatdetail

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mhha.sample.mychahting.databinding.ItemChatBinding
import mhha.sample.mychahting.userlist.UserItem

class ChatAdpter: ListAdapter<ChatItem, ChatAdpter.ViewHolder>(differ) {

    var otherUserItem: UserItem? = null

    inner class ViewHolder(private  val binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ChatItem){
            if(item.userId == otherUserItem?.userId){
                binding.usernameTextView.isVisible = true
                binding.usernameTextView.text= otherUserItem?.userName //밖에서 넣어주기
                binding.messageTextView.text = item.meassge
                binding.messageTextView.gravity = Gravity.START
            }else{
                binding.usernameTextView.isVisible = false
                binding.messageTextView.text =item.meassge
                binding.messageTextView.gravity = Gravity.END
            }
        }//fun bind(item: UserItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val differ = object : DiffUtil.ItemCallback<ChatItem>(){
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }
        }
    }//companion object


}