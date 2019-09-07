package dev.dextra.newsapp.feature.news.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.dextra.newsapp.R
import dev.dextra.newsapp.api.model.Article
import kotlinx.android.synthetic.main.item_article.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ArticleListAdapter(val articles: List<Article>) :
    RecyclerView.Adapter<ArticleListAdapter.ViewHolder>() {

    private val dateFormat =
        SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT)
    private val parseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var article = articles[position]

        holder.itemView.apply {
            article_name.text = article.title
            article_description.text = article.description
            article_author.text = article.author
            article_date.text = dateFormat.format(parseFormat.parse(article.publishedAt))

            setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(article.url)
                context.startActivity(i)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}