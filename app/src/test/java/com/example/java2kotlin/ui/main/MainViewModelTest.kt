package com.example.java2kotlin.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.java2kotlin.data.NotesRepository
import com.example.java2kotlin.data.entity.Note
import com.example.java2kotlin.model.NoteResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    private val mockRepository = mockk<NotesRepository>()
    private val notesLiveData = MutableLiveData<NoteResult>()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        every { mockRepository.getNotes() } returns notesLiveData
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever { result = it?.error }
        notesLiveData.value = NoteResult.Error(testData)
        assertEquals(result, testData)
    }

    @Test
    fun `should return Notes`() {
        var result: List<Note>? = null
        val testData = listOf(Note(id = "1"), Note(id = "2"))
        viewModel.getViewState().observeForever { result = it?.data}
        notesLiveData.value = NoteResult.Success(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onCleared()
        assertFalse(notesLiveData.hasObservers())
    }
}