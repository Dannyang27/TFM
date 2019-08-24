package com.example.tfm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.adapter.UserSearchAdapter
import com.example.tfm.divider.HorizontalDivider
import com.example.tfm.model.User
import com.example.tfm.util.FirebaseUtil
import com.example.tfm.util.getUsersByName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.anko.toast

class UserSearcherActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()
    override val coroutineContext get() = Dispatchers.Default + job

    private lateinit var searchView: SearchView
    private lateinit var recyclerview: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager

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

        searchView = findViewById(R.id.search_user)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                toast("searching...$newText")
                newText?.let{
                    FirebaseUtil.database.getUsersByName(newText)
                }
                return true
            }
        })

        viewManager = LinearLayoutManager(this)

        users = mutableListOf()
        viewAdapter = UserSearchAdapter(users)

        recyclerview = findViewById<RecyclerView>(R.id.seach_recyclerview).apply {
            setHasFixedSize(true)
            addItemDecoration(HorizontalDivider(this.context))
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}
