package com.example.java2kotlin.ui.note

import androidx.annotation.VisibleForTesting
import com.example.java2kotlin.data.NotesRepository
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.model.NoteResult
import com.example.java2kotlin.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class NoteViewModel (private val repository: NotesRepository): BaseViewModel<NoteViewState.Data>() {
    private var currentNote: Note? = null

    fun save(note: Note) {
        currentNote = note
    }

    @VisibleForTesting
    public override fun onCleared() {
        launch {
            currentNote?.let {
                repository.saveNote(it)
            }
            super.onCleared()
        }
    }

    fun loadNote(noteId: String) {
        launch {
            try {
                repository.getNoteById(noteId).let {
                    currentNote = it
                    setData(NoteViewState.Data(note = it))
                }
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    fun deleteNote() {
        launch {
            try {
                currentNote?.let {
                    repository.deleteNote(it.id)
                    currentNote = null
                    NoteViewState.Data(isDeleted = true)
                }
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }
}