package com.soulkey.applemint.data

import androidx.lifecycle.LiveData
import com.soulkey.applemint.model.Article

interface ArticleRepository {
    fun getArticlesSingle(): List<Article>
    fun getNewArticles(): LiveData<List<Article>>
    fun getReadLater(): LiveData<List<Article>>
    fun removeArticle(id: String)
    fun restoreArticle(item: Article)
    fun keepArticle(id: String)
    fun getFbIds(): List<String>
    fun deleteById(id: String)
    fun insertAll(list: List<Article>)
}