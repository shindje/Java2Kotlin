package com.example.java2kotlin.ui.note

import androidx.annotation.VisibleForTesting
import com.example.java2kotlin.data.NotesRepository
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.model.NoteResult
import com.example.java2kotlin.ui.base.BaseViewModel

class NoteViewModel (private val repository: NotesRepository): BaseViewModel<NoteViewState.Data, NoteViewState>() {
    private val currentNote: Note?
        get() = viewStateLiveData.value?.data?.note

    fun save(note: Note) {
        viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = note))
    }

    @VisibleForTesting
    public override fun onCleared() {
        currentNote?.let {
            repository.saveNote(it)
        }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever {
            it?.let {
                viewStateLiveData.value =
                        when (it) {
                            is NoteResult.Success<*> -> NoteViewState(NoteViewState.Data(note = it.data as? Note))
                            is NoteResult.Error -> NoteViewState(error = it.error)
                        }
            }
        }
    }

    fun deleteNote() {
        currentNote?.let {
            repository.deleteNote(it.id).observeForever { t ->
                t?.let {
                    viewStateLiveData.value = when (it) {
                        is NoteResult.Success<*> -> NoteViewState(NoteViewState.Data(isDeleted = true))
                        is NoteResult.Error -> NoteViewState(error = it.error)
                    }
                }
            }
        }
    }
}