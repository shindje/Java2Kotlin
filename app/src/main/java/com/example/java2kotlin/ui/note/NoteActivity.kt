package com.example.java2kotlin.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.java2kotlin.R
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.ext.format
import com.example.java2kotlin.ext.getColorInt
import com.example.java2kotlin.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_note.*
import java.util.*

class NoteActivity: BaseActivity<Note?, NoteViewState>() {
    companion object {
        private val EXTRA_NOTE = "noteId"

        fun getStartIntent(context: Context, noteId: String?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            return intent
        }
    }

    private var note: Note? = null
    override val viewModel: NoteViewModel by lazy { ViewModelProvider(this).get(NoteViewModel::class.java) }
    override val layoutResId = R.layout.activity_note

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            triggerSaveNote()
        }

    }

    private fun triggerSaveNote() {
        if (titleEditText.text == null || titleEditText.text!!.length < 3) return

        note = note?.copy(title = titleEditText.text.toString(),
                          text = textEditText.text.toString(),
                          lastChangeDate = Date())
                   ?: createNote()

        if (note != null)
            viewModel.save(note!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let { viewModel.loadNote(noteId) }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (noteId == null) {
            supportActionBar?.title = getString(R.string.defaultNoteTitle)
            titleEditText.addTextChangedListener(textChangeListener)
            textEditText.addTextChangedListener(textChangeListener)
        }
    }

    override fun renderData(data: Note?) {
        this.note = data
        initView()
    }

    private fun initView() {
        note?.run {
            supportActionBar?.title = lastChangeDate.format()
            titleEditText.setText(title)
            textEditText.setText(text)
            toolbar.setBackgroundColor(color.getColorInt(this@NoteActivity))
        }
        titleEditText.addTextChangedListener(textChangeListener)
        textEditText.addTextChangedListener(textChangeListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    private fun createNote(): Note = Note(UUID.randomUUID().toString(),
                                          titleEditText.text.toString(),
                                          textEditText.text.toString())
}