package com.example.java2kotlin.ui.note

import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.ui.base.BaseViewState

class NoteViewState (data: Data = Data(), error: Throwable? = null): BaseViewState<NoteViewState.Data>(data, error) {
    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}