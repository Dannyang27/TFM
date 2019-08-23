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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.anko.toast
import kotlin.coroutines.CoroutineContext

class UserSearcherActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()
    override val coroutineContext get() = Dispatchers.Default + job

    private lateinit var searchView: SearchView
    private lateinit var recyclerview: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object{
        lateinit var viewAdapter : RecyclerView.Adapter<*>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_searcher)

        searchView = findViewById(R.id.search_user)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                toast(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        viewManager = LinearLayoutManager(this)
        val users = mutableListOf(User("danny27995@gmail.com", "Le Danny Yang", "At the gym", "kdnasdna"),
            User("celiasoler@gmail.com", "Celia Soler","Trabajando en el Oysho jiji", "kdnasdna"),
            User("weilesley@gmail.com", "Wei Lesley Yang","Viajando...", "kdnasdna"),
            User("jorgeguzman@gmail.com", "Jorge Guzman", "Drakukeo, te meto el dedo","kdnasdna"))
        viewAdapter = UserSearchAdapter(users)

        recyclerview = findViewById<RecyclerView>(R.id.seach_recyclerview).apply {
            setHasFixedSize(true)
            addItemDecoration(HorizontalDivider(this.context))
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}
