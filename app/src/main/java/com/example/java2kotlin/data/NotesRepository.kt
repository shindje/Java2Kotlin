package com.example.java2kotlin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.java2kotlin.data.entity.Color
import com.example.java2kotlin.data.entity.Note
import java.util.*

object NotesRepository {
    val notes: MutableList<Note> = mutableListOf()

    private val notesLiveData = MutableLiveData<List<Note>>()

    init {
        for (i in 1..50)
        notes.add(Note(UUID.randomUUID().toString(), "Note " + i, "Text note " + i, Color.values()[(i-1) % Color.values().size]))

        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note) {
        for (i in 0 until notes.size)
            if (notes[i] == note) {
                notes.set(i, note)
                return
            }

        notes.add(note)
    }
}