package mhha.sample.mychahting.userlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
        }

    } //override fun onViewCreated(view: View, savedInstanceState: Bundle?)
} //class UserFrgment: Fragment(R.layout.fragment_userlist)