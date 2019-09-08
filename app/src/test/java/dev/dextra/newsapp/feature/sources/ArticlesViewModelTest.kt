package dev.dextra.newsapp.feature.sources

import dev.dextra.newsapp.TestConstants
import dev.dextra.newsapp.api.model.ArticlesResponse
import dev.dextra.newsapp.base.BaseTest
import dev.dextra.newsapp.base.NetworkState
import dev.dextra.newsapp.base.TestSuite
import dev.dextra.newsapp.feature.news.NewsViewModel
import dev.dextra.newsapp.utils.JsonUtils
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.koin.test.get

class ArticlesViewModelTest : BaseTest() {

    val emptyResponse = ArticlesResponse(ArrayList(), "ok", 0)
    val errorResponse = ArticlesResponse(ArrayList(), "error", 0)

    lateinit var viewModel: NewsViewModel

    @Before
    fun setupTest() {
        viewModel = TestSuite.get()
    }

    @Test
    fun testGetArticles() {
        viewModel.loadNews("abc-news")

        assert(viewModel.articles.value?.size == 11)
        Assert.assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

        viewModel.onCleared()

        assert(viewModel.getDisposables().isEmpty())
    }

    @Test
    fun testErrorSources() {
        TestSuite.mock(TestConstants.newsURL).body(JsonUtils.toJson(errorResponse)).apply()

        viewModel.loadNews("abc-news")

        assert(viewModel.articles.value?.size == null)
        Assert.assertEquals(NetworkState.EMPTY, viewModel.networkState.value)
    }

    @Test
    fun testEmptySources() {
        TestSuite.mock(TestConstants.newsURL).body(JsonUtils.toJson(emptyResponse)).apply()

        viewModel.loadNews("abc-news")

        assert(viewModel.articles.value?.size == null)
        Assert.assertEquals(NetworkState.EMPTY, viewModel.networkState.value)
    }

}