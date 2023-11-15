package mhha.sample.mychahting.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mhha.sample.mychahting.databinding.ItemUserBinding

class UserAdapter(private val onclick: (UserItem) -> Unit): ListAdapter<UserItem, UserAdapter.ViewHolder>(differ) {


    inner class ViewHolder(private  val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: UserItem){
            binding.nicknameTextView.text=item.userName
            binding.descriptionTextView.text = item.description

            binding.root.setOnClickListener {
                onclick(item)
            }

        }//fun bind(item: UserItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val differ = object : DiffUtil.ItemCallback<UserItem>(){
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }
        }
    }//companion object


}