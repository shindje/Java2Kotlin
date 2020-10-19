package com.example.java2kotlin.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.model.NoteResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

private const val NOTES = "notes"

class FireStoreProvider: RemoteDataProvider {
    private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    private val db = FirebaseFirestore.getInstance()
    private val notes = db.collection(NOTES)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        var result = MutableLiveData<NoteResult>()
        notes.addSnapshotListener { value, error ->
            if (error != null)
                result.value = NoteResult.Error(error)
            else if (value != null) {
                val noteList = mutableListOf<Note>()
                for (doc: QueryDocumentSnapshot in value) {
                    noteList.add(doc.toObject(Note::class.java))
                }
                result.value = NoteResult.Success(noteList)
            }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        var result = MutableLiveData<NoteResult>()
        notes.document(id).get()
                .addOnSuccessListener {
                    result.value = NoteResult.Success(it.toObject(Note::class.java))
                }
                .addOnFailureListener {
                    result.value = NoteResult.Error(it)
                }
        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        var result = MutableLiveData<NoteResult>()
        notes.document(note.id)
                .set(note)
                .addOnSuccessListener {
                    Log.d(TAG, "Note $note saved")
                    result.value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    Log.d(TAG, "Error saving $note: ${it.message}")
                    result.value = NoteResult.Error(it)
                }
        return result
    }
}