package com.truecaller.assignment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.truecaller.assignment.data.MainRepository
import com.truecaller.assignment.ui.MainViewModel
import com.truecaller.assignment.utilities.RepositoryResource
import com.truecaller.assignment.utilities.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var mainRepository: MainRepository

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(mainRepository, testDispatcher, testDispatcher)
    }

    @Test
    fun readURLContent_Success() = runTest {
        Mockito.`when`(mainRepository.getURLContent(Utility.DataURL)).thenReturn(RepositoryResource.Success(""))
        viewModel.readURL(Utility.DataURL)
        testDispatcher.scheduler.advanceUntilIdle()
        val result = viewModel.dataRead.getOrAwaitValue()
        assertEquals(true, result)
    }

    @Test
    fun readURLContent_Error() = runTest {
        Mockito.`when`(mainRepository.getURLContent(Utility.DataURL)).thenReturn(RepositoryResource.Error(""))
        viewModel.readURL(Utility.DataURL)
        testDispatcher.scheduler.advanceUntilIdle()
        val result = viewModel.dataRead.getOrAwaitValue()
        assertEquals(false, result)
    }

    @Test
    fun findNthCharacter_Input9_ExpectedSuccess() = runTest {
        viewModel.findNthCharacter("123456789", 9)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("9", viewModel.task1.get())
    }

    @Test
    fun findNthCharacter_Input300_ExpectedError() = runTest {
        viewModel.findNthCharacter("123456789", 300)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Data length is smaller than the 300", viewModel.task1.get())
    }

    @Test
    fun findNthCharacter_Input0_ExpectedError() = runTest {
        viewModel.findNthCharacter("123456789", 0)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Please enter a valid index", viewModel.task1.get())
    }


    @Test
    fun findEveryNthCharacter_Input2_ExpectedSuccess() = runTest {
        viewModel.findEveryNthCharacter("123456789", 2)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("2468", viewModel.task2.get())
    }

    @Test
    fun findEveryNthCharacter_Input1_ExpectedSuccess() = runTest {
        viewModel.findEveryNthCharacter("123456789", 1)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("123456789", viewModel.task2.get())
    }

    @Test
    fun findEveryNthCharacter_Input0_ExpectedSuccess() = runTest {
        viewModel.findEveryNthCharacter("123456789", 0)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Please enter a valid index", viewModel.task2.get())
    }

    @Test
    fun splitAndFindOccurrences_InputSpace_ExpectedSuccess() = runTest {
        viewModel.findOccurrences("<p> Truecaller Hello World </p>")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(5, viewModel.words.size)
        assertEquals(5, viewModel.wordsCount.size)
    }

    @Test
    fun splitAndFindOccurrences_InputSpaceTabLineBreak_ExpectedSuccess() = runTest {
        viewModel.findOccurrences("<p> Truecaller        Hello \n\n World      \n </p>")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(5, viewModel.words.size)
        assertEquals(5, viewModel.wordsCount.size)
    }

    @Test
    fun splitAndFindOccurrences_InputNoSpace_ExpectedSuccess() = runTest {
        viewModel.findOccurrences("Test")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, viewModel.words.size)
        assertEquals(1, viewModel.wordsCount.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}