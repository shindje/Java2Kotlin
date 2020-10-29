package com.example.java2kotlin.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.java2kotlin.R
import com.example.java2kotlin.data.entity.Color
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.ext.format
import com.example.java2kotlin.ext.getColorInt
import com.example.java2kotlin.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_note.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class NoteActivity: BaseActivity<NoteViewState.Data, NoteViewState>() {
    companion object {
        private val EXTRA_NOTE = "noteId"

        fun getStartIntent(context: Context, noteId: String?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            return intent
        }
    }

    private var note: Note? = null
    override val model: NoteViewModel by viewModel()
    override val layoutResId = R.layout.activity_note
    private var color: Color = Color.WHITE

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
                          lastChangeDate = Date(),
                          color = color)
                   ?: createNote()

        if (note != null)
            model.save(note!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let { model.loadNote(noteId) }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (noteId == null) {
            supportActionBar?.title = getString(R.string.defaultNoteTitle)
        }

        colorPicker.onColorClickListener = {
            color = it
            setToolbarColor(it)
            triggerSaveNote()
        }
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted)
            finish()

        this.note = data.note
        initView()
    }

    private fun initView() {
        note?.run {
            supportActionBar?.title = lastChangeDate.format()

            removeEditListener()
            titleEditText.setText(title)
            textEditText.setText(text)
            setEditListener()

            setToolbarColor(color)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = menuInflater.inflate(R.menu.note_menu, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                android.R.id.home -> onBackPressed().let { true }
                R.id.palette -> togglePalette().let { true }
                R.id.delete -> deleteNote().let { true }
                else -> super.onOptionsItemSelected(item)
            }

    private fun createNote(): Note = Note(UUID.randomUUID().toString(),
                                          titleEditText.text.toString(),
                                          textEditText.text.toString())

    private fun deleteNote() {
        AlertDialog.Builder(this)
                .setTitle(R.string.delete_menu_title)
                .setMessage(getString(R.string.delete_dialog_message))
                .setPositiveButton(R.string.ok) {dialog, which ->
                    model.deleteNote()
                }
                .setNegativeButton(R.string.cancel) {dialog, which ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun  setEditListener() {
        titleEditText.addTextChangedListener(textChangeListener)
        textEditText.addTextChangedListener(textChangeListener)
    }

    private fun removeEditListener() {
        titleEditText.removeTextChangedListener(textChangeListener)
        textEditText.removeTextChangedListener(textChangeListener)
    }

    private fun setToolbarColor(color: Color) {
        toolbar.setBackgroundColor(color.getColorInt(this))
    }

    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    override fun onBackPressed() {
        if (colorPicker.isOpen) {
            colorPicker.close()
            return
        }
        super.onBackPressed()
    }
}