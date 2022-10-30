package com.story.app.ui.register

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
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataRepository: DataRepository

    private lateinit var registerViewModel: RegisterViewModel
    private val generalResponse = DataDummy.generalResponse()
    private val emailMock = "bukan@email.com"
    private val passwordMock = "12345678"
    private val nameMock = "bukan nama"

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(dataRepository)
        registerViewModel.setRegisterParam(nameMock, emailMock, passwordMock)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when register`()  = runTest {
        val expectedResponse : Flow<Resource<GeneralResponse>> = flow {
            emit(Resource.Success(generalResponse))
        }
        Mockito.`when`(dataRepository.doRegister(emailMock, passwordMock, nameMock)).thenReturn(
            expectedResponse
        )
        registerViewModel.doRegister().observeForever {
            Mockito.verify(dataRepository).doRegister(emailMock, passwordMock, nameMock)
            assertNotNull(it.data)
            assertTrue(it is Resource.Success)
            assertFalse(it is Resource.Error)
            assertFalse(it.data?.error?:false)
        }
    }
}