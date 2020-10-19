package com.example.java2kotlin.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.java2kotlin.R
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.ui.base.BaseActivity
import com.example.java2kotlin.ui.note.NoteActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {
    override val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    override val layoutResId: Int = R.layout.activity_main
    lateinit var adapter: MainRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        adapter = MainRVAdapter(object : MainRVAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                showNote(note)
            }
        })
        mainRecycler.adapter = adapter

        fab.setOnClickListener {
            showNote(null)
        }
    }

    fun showNote(note: Note?) {
        val intent = NoteActivity.getStartIntent(this, note?.id)
        startActivity(intent)
    }

    override fun renderData(data: List<Note>?) {
        if (data == null)
            return
        adapter.notes = data
    }
}