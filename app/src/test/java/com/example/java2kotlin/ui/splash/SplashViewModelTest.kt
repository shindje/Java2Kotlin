package com.example.java2kotlin.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.java2kotlin.data.NotesRepository
import com.example.java2kotlin.data.errors.NoAuthException
import com.example.java2kotlin.model.NoteResult
import com.example.java2kotlin.model.User
import com.example.java2kotlin.ui.note.NoteViewModel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    private val mockRepository = mockk<NotesRepository>()
    private val userLiveData = MutableLiveData<User>()
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        clearAllMocks()
        viewModel = SplashViewModel(mockRepository)
    }

    @Test
    fun `requestUser should return isAuth = true`(){
        var result: Boolean? = null
        val testData = true
        every { mockRepository.getCurrentUser() } returns userLiveData
        viewModel.viewStateLiveData.observeForever {
            result = it.data
        }

        viewModel.requestUser()
        userLiveData.value = User("", "")
        assertEquals(testData, result)
    }


    @Test
    fun `requestUser should return error`(){
        var result: Throwable? = null
        every { mockRepository.getCurrentUser() } returns userLiveData
        viewModel.viewStateLiveData.observeForever {
            result = it.error
        }

        viewModel.requestUser()
        userLiveData.value = null
        assertTrue(result is NoAuthException)
    }
}