package com.example.tfm.activity

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.adapter.UserSearchAdapter
import com.example.tfm.divider.HorizontalDivider
import com.example.tfm.model.User
import com.example.tfm.util.FirebaseUtil
import kotlinx.android.synthetic.main.activity_user_searcher.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class UserSearcherActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()
    override val coroutineContext get() = Dispatchers.Default + job

    private lateinit var viewManager: RecyclerView.LayoutManager

    private val cacheUserList = mutableListOf<User>()

    companion object{
        lateinit var viewAdapter : UserSearchAdapter
        lateinit var users: MutableList<User>

        fun updateList( users: MutableList<User>){
            viewAdapter.updateList(users)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_searcher)

        search_user.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewAdapter.updateList(cacheUserList.filter {
                                    it.name.contains(newText.toString(), ignoreCase = true)
                                    }.toMutableList())
                return true
            }
        })

        viewManager = LinearLayoutManager(this)

        users = mutableListOf()
        viewAdapter = UserSearchAdapter(users)

        seach_recyclerview.apply {
            setHasFixedSize(true)
            addItemDecoration(HorizontalDivider(this.context))
            layoutManager = viewManager
            adapter = viewAdapter
            FirebaseUtil.loadAllUsers(cacheUserList)
        }
    }
}
