package com.example.java2kotlin.ui.main

import androidx.lifecycle.Observer
import com.example.java2kotlin.data.NotesRepository
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.model.NoteResult
import com.example.java2kotlin.ui.base.BaseViewModel

class MainViewModel(val repository: NotesRepository = NotesRepository) : BaseViewModel<List<Note>?, MainViewState>() {
    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            if (t == null)
                return
            when(t) {
                is NoteResult.Success<*> -> viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
                is NoteResult.Error -> viewStateLiveData.value = MainViewState(error = t.error)
            }
        }
    }

    private val repositoryNotes = repository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}