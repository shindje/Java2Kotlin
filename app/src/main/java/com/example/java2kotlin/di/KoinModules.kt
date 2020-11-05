package com.example.java2kotlin.di

import com.example.java2kotlin.data.NotesRepository
import com.example.java2kotlin.data.provider.FireStoreProvider
import com.example.java2kotlin.data.provider.RemoteDataProvider
import com.example.java2kotlin.ui.main.MainViewModel
import com.example.java2kotlin.ui.note.NoteViewModel
import com.example.java2kotlin.ui.splash.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.experimental.builder.viewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { NotesRepository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}