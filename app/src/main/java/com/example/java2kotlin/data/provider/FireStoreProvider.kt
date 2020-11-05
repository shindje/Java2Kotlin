package com.example.java2kotlin.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.data.errors.NoAuthException
import com.example.java2kotlin.model.NoteResult
import com.example.java2kotlin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val NOTES = "notes"
private const val USERS = "users"

class FireStoreProvider(private val firebaseAuth: FirebaseAuth, private val db: FirebaseFirestore): RemoteDataProvider {
    private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    private val notes = db.collection(NOTES)
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    private fun getUserNotes() = currentUser?.let { db.collection(USERS).document(it.uid).collection(NOTES) }
            ?: throw NoAuthException()

    override fun subscribeToAllNotes(): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUserNotes().addSnapshotListener { snapshot, error ->
                    value = error?.let { NoteResult.Error(it) }
                            ?: snapshot?.let {
                                val noteList = it.documents.map { it.toObject(Note::class.java) }
                                NoteResult.Success(noteList)
                            }
                }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }

        }

    override fun getNoteById(id: String): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUserNotes().document(id).get()
                        .addOnSuccessListener {
                            value = NoteResult.Success(it.toObject(Note::class.java))
                        }
                        .addOnFailureListener {
                            throw it
                        }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

    override fun saveNote(note: Note): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUserNotes().document(note.id)
                        .set(note).addOnSuccessListener {
                            Log.d(TAG, "Note $note saved")
                            value = NoteResult.Success(note)
                        }.addOnFailureListener {
                            Log.d(TAG, "Error saving $note: ${it.message}")
                            throw it
                        }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

    override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User?>().apply {
        value = currentUser?.let { User(it.displayName?: "", it.email?: "") }
    }

    override fun deleteNote(noteId: String): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                getUserNotes().document(noteId).delete()
                        .addOnSuccessListener {
                            value = NoteResult.Success(null)
                        }.addOnFailureListener {
                            value = NoteResult.Error(it)
                        }
            }
}