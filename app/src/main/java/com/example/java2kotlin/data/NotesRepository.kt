package com.example.java2kotlin.data

import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.data.provider.FireStoreProvider
import com.example.java2kotlin.data.provider.RemoteDataProvider

object NotesRepository {
    private val remoteDataProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteDataProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteDataProvider.saveNote(note)
    fun getNoteById(id: String) = remoteDataProvider.getNoteById(id)
}