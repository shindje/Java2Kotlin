package com.example.java2kotlin.ui.note

import androidx.lifecycle.ViewModel
import com.example.java2kotlin.data.NotesRepository
import com.example.java2kotlin.data.entity.Note

class NoteViewModel (private val repository: NotesRepository = NotesRepository): ViewModel() {
    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null)
            repository.saveNote(pendingNote!!)
    }
}