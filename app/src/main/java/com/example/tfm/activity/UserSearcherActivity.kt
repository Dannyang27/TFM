package com.example.tfm.activity

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.tfm.R
import com.example.tfm.adapter.UserSearchAdapter
import com.example.tfm.divider.HorizontalDivider
import com.example.tfm.viewmodel.UserSearcherViewModel

class UserSearcherActivity : AppCompatActivity() {

    @BindView(R.id.search_user) lateinit var searcher: SearchView
    @BindView(R.id.seach_recyclerview) lateinit var rvUsers: RecyclerView

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter : UserSearchAdapter
    private lateinit var userSearcherViewModel: UserSearcherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_searcher)
        ButterKnife.bind(this)

        userSearcherViewModel = ViewModelProviders.of(this).get(UserSearcherViewModel::class.java)
        userSearcherViewModel.getUsers().observe(this, Observer {
            viewAdapter.updateList(it)
        })

        viewManager = LinearLayoutManager(this)
        viewAdapter = UserSearchAdapter(mutableListOf())

        rvUsers.apply {
            setHasFixedSize(true)
            addItemDecoration(HorizontalDivider(context))
            layoutManager = viewManager
            adapter = viewAdapter
        }

        userSearcherViewModel.initRoomObserver(this)

        searcher.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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
