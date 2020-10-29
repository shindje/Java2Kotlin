package com.example.java2kotlin.ui.splash

import com.example.java2kotlin.data.NotesRepository
import com.example.java2kotlin.data.errors.NoAuthException
import com.example.java2kotlin.ui.base.BaseViewModel

class SplashViewModel(private val repository: NotesRepository): BaseViewModel<Boolean?, SplashViewState>() {
    fun requestUser() {
        repository.getCuurentUser().observeForever() {
            viewStateLiveData.value = it?.let {
                SplashViewState(isAuth = true)
            } ?: SplashViewState(error = NoAuthException())
        }
    }
}