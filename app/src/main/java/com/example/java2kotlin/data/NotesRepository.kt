package com.example.java2kotlin.data

import android.graphics.Color
import com.example.java2kotlin.data.entity.Note

object NotesRepository {
    val notes = listOf<Note>(
            Note("Note 1", "Text note 1", Color.RED),
            Note("Note 2", "Text note 2", Color.BLUE),
            Note("Note 3", "Text note 3", Color.GREEN),
            Note("Note 4", "Text note 4", Color.YELLOW),
    )
}