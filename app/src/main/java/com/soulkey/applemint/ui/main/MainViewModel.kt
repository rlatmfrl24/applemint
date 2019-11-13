package com.soulkey.applemint.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.data.BookmarkRepository
import com.soulkey.applemint.model.Article
import com.soulkey.applemint.model.Bookmark

class MainViewModel(private val articleRepo: ArticleRepository, private val bookmarkRepo: BookmarkRepository) : ViewModel() {
    val db = FirebaseFirestore.getInstance()
    val filters: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }
    val isFilterOpen: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun bookmarkArticle(category: String, item:Article){
        articleRepo.bookmarkArticle(category, item)
        bookmarkRepo.insert(Bookmark(item, category))
    }

    fun getInitialData(): List<Article> {
        return articleRepo.getArticlesSingle()
    }

    fun getNewArticles(): LiveData<List<Article>>{
        return articleRepo.getNewArticles()
    }

    fun getReadLaters(): LiveData<List<Article>> {
        return articleRepo.getReadLater()
    }

    fun getCategories(): List<String> {
        return bookmarkRepo.getCategories()
    }

    fun getBookmarks(): LiveData<List<Bookmark>> {
        return bookmarkRepo.getBookmarks()
    }

    fun removeArticle(fb_id: String) {
        articleRepo.removeArticle(fb_id)
    }

    fun restoreArticle(item: Article) {
        articleRepo.restoreArticle(item)
    }

    fun keepArticle(fb_id: String) {
        articleRepo.keepArticle(fb_id)
    }
}
