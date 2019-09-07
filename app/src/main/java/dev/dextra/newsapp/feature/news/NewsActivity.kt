package dev.dextra.newsapp.feature.news

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.dextra.newsapp.R
import dev.dextra.newsapp.api.model.Article
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.base.BaseListActivity
import dev.dextra.newsapp.base.NetworkState
import dev.dextra.newsapp.feature.news.adapter.ArticleListAdapter
import dev.dextra.newsapp.feature.sources.adapter.SourcesListAdapter
import kotlinx.android.synthetic.main.activity_news.*
import org.koin.android.ext.android.inject


const val NEWS_ACTIVITY_SOURCE = "NEWS_ACTIVITY_SOURCE"

class NewsActivity : BaseListActivity() {

    override val emptyStateTitle: Int = R.string.empty_state_title_articles
    override val emptyStateSubTitle: Int = R.string.empty_state_subtitle_article
    override val errorStateTitle: Int = R.string.error_state_title_article
    override val errorStateSubTitle: Int = R.string.error_state_subtitle_article
    override val mainList: View
        get() = news_list

    private val newsViewModel: NewsViewModel by inject()
    private var viewAdapter: ArticleListAdapter = ArticleListAdapter()
    private var viewManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)
    private var currentPage = 1
    private lateinit var mSource: Source


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_news)
        doBinds()
        setupRecyclerView()

        (intent?.extras?.getSerializable(NEWS_ACTIVITY_SOURCE) as Source).let { source ->
            this.mSource = source
            title = source.name
            loadNews(source, currentPage)
        }
        configurationPaginate()
        super.onCreate(savedInstanceState)

    }

    private fun setupRecyclerView() {
        news_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun doBinds() {
        newsViewModel.articles.observe(this, Observer { articles ->
            viewAdapter.apply {
                currentPage++
                add(articles)
                notifyDataSetChanged()
            }
        })
        newsViewModel.networkState.observe(this, networkStateObserver)

    }

    private fun configurationPaginate() {
        news_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastItem = layoutManager.findLastCompletelyVisibleItemPosition()
                val currentTotalCount = layoutManager.itemCount
                if (currentTotalCount <= lastItem + 2) {
                    loadNews(mSource, currentPage)
                }
            }
        })
    }

    private fun loadNews(source: Source, currentPage: Int = 1) {
        newsViewModel.loadNews(source.id, currentPage)
    }

    override fun executeRetry() {
        loadNews(mSource, currentPage)
    }

    override fun setupLandscape() {}
    override fun setupPortrait() {}

}
