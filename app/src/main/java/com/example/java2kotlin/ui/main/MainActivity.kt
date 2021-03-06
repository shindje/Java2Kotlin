package com.example.java2kotlin.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.java2kotlin.R
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.ui.base.BaseActivity
import com.example.java2kotlin.ui.note.NoteActivity
import com.example.java2kotlin.ui.splash.SplashActivity
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<List<Note>?>() {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override val model: MainViewModel by viewModel()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = MenuInflater(this).inflate(R.menu.main_menu, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.logout -> showLogoutDialog().let{true}
                else -> false
            }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(getString(R.string.logout) + "?")
                .setPositiveButton(R.string.ok) {dialog, which ->
                    logout()
                }
                .setNegativeButton(R.string.cancel) {dialog, which ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
    }
}