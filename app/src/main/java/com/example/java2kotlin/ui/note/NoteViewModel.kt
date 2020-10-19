package com.example.java2kotlin.ui.note

import com.example.java2kotlin.data.NotesRepository
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.model.NoteResult
import com.example.java2kotlin.ui.base.BaseViewModel

class NoteViewModel (private val repository: NotesRepository = NotesRepository): BaseViewModel<Note?, NoteViewState>() {
    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null)
            repository.saveNote(pendingNote!!)
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever {
            if (it == null)
                return@observeForever
            when(it) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(note = it.data as? Note)
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = it.error)
            }
        }
    }
}