package com.example.tfm.activity

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.adapter.UserSearchAdapter
import com.example.tfm.divider.HorizontalDivider
import com.example.tfm.viewmodel.UserSearcherViewModel
import kotlinx.android.synthetic.main.activity_user_searcher.*

class UserSearcherActivity : AppCompatActivity() {

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter : UserSearchAdapter
    private lateinit var userSearcherViewModel: UserSearcherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_searcher)

        userSearcherViewModel = ViewModelProviders.of(this).get(UserSearcherViewModel::class.java)

        userSearcherViewModel.getUsers().observe(this, Observer {
            viewAdapter.updateList(it)
        })

        viewManager = LinearLayoutManager(this)
        viewAdapter = UserSearchAdapter(mutableListOf())

        seach_recyclerview.apply {
            setHasFixedSize(true)
            addItemDecoration(HorizontalDivider(this.context))
            layoutManager = viewManager
            adapter = viewAdapter
        }

        userSearcherViewModel.searchUsers("")

        search_user.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userSearcherViewModel.searchUsersInCache(newText)
                return true
            }
        })
    }
}
