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
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val NOTES = "notes"
private const val USERS = "users"

class FireStoreProvider(private val firebaseAuth: FirebaseAuth, private val db: FirebaseFirestore): RemoteDataProvider {
    private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    private val currentUser
        get() = firebaseAuth.currentUser

    private fun getUserNotes() = currentUser?.let { db.collection(USERS).document(it.uid).collection(NOTES) }
            ?: throw NoAuthException()

    override fun subscribeToAllNotes(): ReceiveChannel<NoteResult> =
        Channel<NoteResult>(Channel.CONFLATED).apply {
            var registration: ListenerRegistration? = null
            try {
                registration = getUserNotes().addSnapshotListener { snapshot, error ->
                    val value = error?.let { NoteResult.Error(it) }
                            ?: snapshot?.let {
                                val noteList = it.documents.map { it.toObject(Note::class.java) }
                                NoteResult.Success(noteList)
                            }
                    value?.let { offer(it) }
                }
            } catch (e: Throwable) {
                offer(NoteResult.Error(e))
            }
            invokeOnClose { registration?.remove() }
        }

    override suspend fun getNoteById(id: String): Note =
        suspendCoroutine { continuation ->
            try {
                getUserNotes().document(id).get()
                        .addOnSuccessListener {
                            continuation.resume(it.toObject(Note::class.java)!!)
                        }
                        .addOnFailureListener {
                            continuation.resumeWithException(it)
                        }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }

    override suspend fun saveNote(note: Note): Note =
        suspendCoroutine { continuation ->
            try {
                getUserNotes().document(note.id)
                        .set(note).addOnSuccessListener {
                            Log.d(TAG, "Note $note saved")
                            continuation.resume(note)
                        }.addOnFailureListener {
                            Log.d(TAG, "Error saving $note: ${it.message}")
                            continuation.resumeWithException(it)
                        }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }

    override suspend fun getCurrentUser(): User? =
        suspendCoroutine { continuation ->
            val user = currentUser?.let { User(it.displayName?: "", it.email?: "") }
            continuation.resume(user)
        }

    override suspend fun deleteNote(noteId: String): Unit =
            suspendCoroutine { continuation ->
                try {
                    getUserNotes().document(noteId).delete()
                            .addOnSuccessListener {
                                continuation.resume(Unit)
                            }.addOnFailureListener {
                                continuation.resumeWithException(it)
                            }
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }
            }
}