package mhha.sample.mychahting.chatdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import mhha.sample.mychahting.Key
import mhha.sample.mychahting.databinding.ActivityChatdetailBinding
import mhha.sample.mychahting.userlist.UserItem

class ChatActivity: AppCompatActivity() {

    private lateinit var binding: ActivityChatdetailBinding
    private val chatAdpter = ChatAdpter()

    private var chatRoomId: String = ""
    private var otherUserId: String = ""
    private var myUserId: String = ""

    private val chatItemList = mutableListOf<ChatItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatRoomId = intent.getStringExtra("chatRoomId") ?: return
        otherUserId = intent.getStringExtra("otherUserId") ?: return
        myUserId = Firebase.auth.currentUser?.uid ?: ""

        Firebase.database.reference.child(Key.DB_USERS).child(myUserId).get()
            .addOnSuccessListener {
                val myUserItem = it.getValue(UserItem::class.java)
                val myUserName = myUserItem?.userName
            }
        Firebase.database.reference.child(Key.DB_USERS).child(otherUserId).get()
            .addOnSuccessListener {
                val otherUesrItem = it.getValue(UserItem::class.java)
                chatAdpter.otherUserItem = otherUesrItem
            }

        Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?: return
                chatItemList.add(chatItem)
                chatAdpter.submitList(chatItemList)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })


        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdpter
        }

    } //override fun onCreate(savedInstanceState: Bundle?)
} //class ChatActivity: AppCompatActivity()