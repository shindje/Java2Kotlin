package com.example.java2kotlin.ui.note

import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.ui.base.BaseViewState

class NoteViewState (note: Note? = null, error: Throwable? = null): BaseViewState<Note?>(note, error)