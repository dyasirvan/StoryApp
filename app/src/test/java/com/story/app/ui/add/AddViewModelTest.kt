package com.story.app.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.story.app.DataDummy
import com.story.app.MainDispatcherRule
import com.story.app.core.DataRepository
import com.story.app.core.Resource
import com.story.app.core.data.remote.model.GeneralResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var dataRepository: DataRepository

    private lateinit var addViewModel: AddViewModel
    private val generalResponseMock = DataDummy.generalResponse()
    private val fileMock = DataDummy.multipartFile()
    private val tokenMock = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXRrenRtVi1tQ0hHRDYyOEQiLCJpYXQiOjE2NjcxMTcyMTZ9.sR3fwAxHj64b5aGa7KfuhlGaQzw-zUx2hn8t_kjt0W0"
    private val descriptionMock = "bukan description"
    private val latitudeMock = -6.857053F
    private val longitudeMock = 107.53229F


    @Before
    fun setUp() {
        addViewModel = AddViewModel(dataRepository)
        addViewModel.setStoryParam(tokenMock, fileMock, descriptionMock, latitudeMock, longitudeMock)
    }

    @Test
    fun `when add story`()  = runTest {
        val expectedResponse : Flow<Resource<GeneralResponse>> = flow {
            emit(Resource.Success(generalResponseMock))
        }
        Mockito.`when`(dataRepository.addNewStory(tokenMock, fileMock, descriptionMock, latitudeMock, longitudeMock)).thenReturn(
            expectedResponse
        )
        addViewModel.postStory.observeForever {
            verify(dataRepository).addNewStory(tokenMock, fileMock, descriptionMock, latitudeMock, longitudeMock)
            assertNotNull(it.data)
            assertTrue(it is Resource.Success)
            assertFalse(it is Resource.Error)
            assertFalse(it?.data?.error ?: false)
        }
    }
}