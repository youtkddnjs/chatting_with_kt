package mhha.sample.mychahting.chatdetail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import mhha.sample.mychahting.Key
import mhha.sample.mychahting.R
import mhha.sample.mychahting.databinding.ActivityChatdetailBinding
import mhha.sample.mychahting.userlist.UserItem
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import kotlin.math.log

class ChatActivity: AppCompatActivity() {

    private lateinit var binding: ActivityChatdetailBinding
    private val chatAdpter = ChatAdpter()
    private lateinit var linearLayoutManager : LinearLayoutManager

    private var chatRoomId: String = ""
    private var otherUserId: String = ""
    private var myUserId: String = ""
    private var myUserName: String = ""
    private var otherUserFcmToken: String = ""



    private val chatItemList = mutableListOf<ChatItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatRoomId = intent.getStringExtra("chatRoomId") ?: return
        otherUserId = intent.getStringExtra("otherUserId") ?: return
        myUserId = Firebase.auth.currentUser?.uid ?: ""
        Log.d("chat", "chatRoomId : ${chatRoomId}, otherUserId : ${otherUserId}, myUserId : ${myUserId}")
        Firebase.database.reference.child(Key.DB_USERS).child(myUserId).get()
            .addOnSuccessListener {
                val myUserItem = it.getValue(UserItem::class.java)
                myUserName = myUserItem?.userName ?: ""
                getOtherUserData()
            }
        linearLayoutManager = LinearLayoutManager(applicationContext)
        binding.chatRecyclerView.apply {
            layoutManager = linearLayoutManager
//            layoutManager = LinearLayoutManager(context).apply {
//                reverseLayout = true 이런 값도 쓸 수 있음
//            }
            adapter = chatAdpter
        }

        // 채팅후 스크롤 되도록
        chatAdpter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                linearLayoutManager.smoothScrollToPosition(binding.chatRecyclerView,null, chatAdpter.itemCount)
            }
        })


        // 메세지 보내기
        binding.sendButton.setOnClickListener {
            val message = binding.messageEditText.text.toString()
            if(message.isEmpty()){
                Toast.makeText(applicationContext, "메세지를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val newChatItem = ChatItem(
                message = message,
                userId = myUserId,

            )
            Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).push().apply {
                newChatItem.chatId = key
                setValue(newChatItem)
            }

            val updates: MutableMap<String,Any> = hashMapOf(
                "${Key.DB_CHAT_ROOMS}/$myUserId/$otherUserId/lastMessage" to message,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/lastMessage" to message,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/chatRoomId" to chatRoomId,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserId" to myUserId,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserName" to myUserName,
            )
            Firebase.database.reference.updateChildren(updates)

            // FCM 보내기
            val client = OkHttpClient()
            val root = JSONObject()
            val notification = JSONObject()
            notification.put("title", getString(R.string.app_name))
            notification.put("body", message)
            root.put("to", otherUserFcmToken)
            root.put("priority", "high")
            root.put("notification",notification)
            val requestBody = root.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder().post(requestBody).url("https://fcm.googleapis.com/fcm/send")
                .header("Authorization", "key=${getString(R.string.serverkey)}")
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.stackTraceToString()
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.e("Chatting", response.toString())
                }
            })

            binding.messageEditText.text.clear()
        }//binding.sendButton.setOnClickListener
    } //override fun onCreate(savedInstanceState: Bundle?)

    private fun getOtherUserData(){
        Firebase.database.reference.child(Key.DB_USERS).child(otherUserId).get()
            .addOnSuccessListener {
                val otherUesrItem = it.getValue(UserItem::class.java)
                otherUserFcmToken = otherUesrItem?.fcmToken.orEmpty()
                Log.d("token : ", "${otherUserFcmToken}")
                chatAdpter.otherUserItem = otherUesrItem

                binding.sendButton.isEnabled = true
                getChatData()
            }
    }

    private fun getChatData(){
        Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?: return

                chatItemList.add(chatItem)
                //11.채팅알림수신하기 06:00 참고
                chatAdpter.submitList(chatItemList.toMutableList())
//                chatAdpter.submitList(chatItemList)

//                binding.chatRecyclerView.scrollToPosition(chatItemList.size - 1 ) 이런 코드가 있음
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
    }
} //class ChatActivity: AppCompatActivity()