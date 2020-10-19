package com.example.java2kotlin.ui.main

import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.ui.base.BaseViewState

class MainViewState (notes: List<Note>? = null, error: Throwable? = null): BaseViewState<List<Note>?>(notes, error)