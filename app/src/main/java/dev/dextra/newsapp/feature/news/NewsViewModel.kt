package dev.dextra.newsapp.feature.news

import androidx.lifecycle.MutableLiveData
import dev.dextra.newsapp.api.model.Article
import dev.dextra.newsapp.api.repository.NewsRepository
import dev.dextra.newsapp.base.BaseViewModel
import dev.dextra.newsapp.base.NetworkState


class NewsViewModel(private val newsRepository: NewsRepository) : BaseViewModel() {

    val articles = MutableLiveData<List<Article>>()
    val networkState = MutableLiveData<NetworkState>()


    fun loadNews(sourceId: String, currentPage: Int = 1) {
        addDisposable(
            newsRepository.getEverything(sourceId, currentPage)
                .doOnSubscribe { networkState.postValue(NetworkState.RUNNING) }
                .subscribe({ response ->
                    if(response.articles.isNotEmpty()){
                        articles.postValue(response.articles)
                        networkState.postValue(NetworkState.SUCCESS)
                    }else{
                        networkState.postValue(NetworkState.EMPTY)
                    }
                },
                    {
                        networkState.postValue(NetworkState.ERROR)
                    })
        )
    }
}
