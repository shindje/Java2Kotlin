package com.example.java2kotlin.data.provider

import androidx.lifecycle.LiveData
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.model.NoteResult

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
}