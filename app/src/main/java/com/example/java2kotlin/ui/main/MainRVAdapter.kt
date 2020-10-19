package com.example.java2kotlin.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.java2kotlin.R
import com.example.java2kotlin.data.entity.Color
import com.example.java2kotlin.data.entity.Note
import kotlinx.android.synthetic.main.item_note.view.*

class MainRVAdapter (private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<MainRVAdapter.NoteViewHolder>() {
    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            NoteViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_note,
                            parent,
                            false
                    )
            )

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) = holder.bind(notes[position])

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) = with(itemView) {
            titleTextView.text = note.title
            textTextView.text = note.text
            var color = when(note.color) {
                Color.WHITE -> R.color.colorWHITE
                Color.VIOLET -> R.color.colorVIOLET
                Color.YELLOW -> R.color.colorYELLOW
                Color.RED -> R.color.colorRED
                Color.PINK -> R.color.colorPINK
                Color.GREEN -> R.color.colorGREEN
                Color.BLUE -> R.color.colorBLUE
            }
            setBackgroundColor(context.resources.getColor(color, null))
            setOnClickListener {
                onItemClickListener.onItemClick(note)}
        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }
}