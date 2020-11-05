package com.example.java2kotlin.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.java2kotlin.R
import com.example.java2kotlin.data.entity.Color
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.ext.getColorInt
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*
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

    inner class NoteViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(note: Note) {
            titleTextView.text = note.title
            textTextView.text = note.text
            itemView.setBackgroundColor(note.color.getColorInt(itemView.context))
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(note)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }
}