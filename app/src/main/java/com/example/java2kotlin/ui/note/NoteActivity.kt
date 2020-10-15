package com.example.java2kotlin.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.java2kotlin.R
import com.example.java2kotlin.data.entity.Color
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.ext.DATE_TIME_FORMAT
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity: AppCompatActivity() {
    companion object {
        private val EXTRA_NOTE = "note"

        fun getStartIntent(context: Context, note: Note?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, note)
            return intent
        }
    }

    private var note: Note? = null
    private lateinit var viewModel: NoteViewModel
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

        setContentView(R.layout.activity_note)
        note = intent.getParcelableExtra(EXTRA_NOTE)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title =
                if (note == null)
                    getString(R.string.defaultNoteTitle)
                else
                    SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note!!.lastChangeDate)

        initView()

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    private fun initView() {
        if (note != null) {
            titleEditText.setText(note?.title ?: "")
            textEditText.setText(note?.text ?: "")
            var color = when(note!!.color) {
                Color.WHITE -> R.color.colorWHITE
                Color.VIOLET -> R.color.colorVIOLET
                Color.YELLOW -> R.color.colorYELLOW
                Color.RED -> R.color.colorRED
                Color.PINK -> R.color.colorPINK
                Color.GREEN -> R.color.colorGREEN
                Color.BLUE -> R.color.colorBLUE
            }
            toolbar.setBackgroundColor(resources.getColor(color, null))
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