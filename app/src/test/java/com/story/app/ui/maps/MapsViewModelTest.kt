package com.story.app.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.story.app.DataDummy
import com.story.app.MainDispatcherRule
import com.story.app.core.DataRepository
import com.story.app.core.data.local.entity.StoryEntity
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
class MapsViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataRepository: DataRepository

    private lateinit var mapsViewModel: MapsViewModel
    private val storyEntities = DataDummy.getStories()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(dataRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when get stories from local`()  = runTest {
        val expectedResponse : Flow<List<StoryEntity>> = flow {
            emit(storyEntities)
        }
        Mockito.`when`(dataRepository.getLocalStories()).thenReturn(
            expectedResponse
        )
        mapsViewModel.getStories().observeForever {
            Mockito.verify(dataRepository).getLocalStories()
            assertNotNull(it)
            assertTrue(it.isNotEmpty())
            assertEquals(it.size, storyEntities.size)
        }
    }

}