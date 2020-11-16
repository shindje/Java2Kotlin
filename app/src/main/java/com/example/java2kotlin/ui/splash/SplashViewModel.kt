package com.example.java2kotlin.ui.splash

import com.example.java2kotlin.data.NotesRepository
import com.example.java2kotlin.data.errors.NoAuthException
import com.example.java2kotlin.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: NotesRepository): BaseViewModel<Boolean>() {
    fun requestUser() {
        launch {
            repository.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }
    }
}