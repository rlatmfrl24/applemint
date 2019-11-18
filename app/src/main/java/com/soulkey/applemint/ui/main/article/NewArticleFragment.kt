package com.soulkey.applemint.ui.main.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.soulkey.applemint.R
import kotlinx.android.synthetic.main.fragment_articles.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import androidx.core.view.children
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.soulkey.applemint.config.getFilters
import com.soulkey.applemint.ui.main.*
import kotlinx.android.synthetic.main.item_article_foreground.view.*
import kotlinx.android.synthetic.main.view_chip_group_type.*

class NewArticleFragment : Fragment() {
    internal val articleViewModel by sharedViewModel<ArticleViewModel>()
    private val mainViewModel by sharedViewModel<MainViewModel>()
    lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleAdapter = ArticleAdapter(articleViewModel)
        articleViewModel.newArticles.observe(this, Observer {
            articleAdapter.submitList(it)
        })
        articleAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                recycler_article.scrollToPosition(0)
            }
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                recycler_article.scrollToPosition(0)
            }
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                recycler_article.scrollToPosition(0)
            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recycler_article.scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                recycler_article.scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                recycler_article.scrollToPosition(0)
            }
        })
        recycler_article.adapter = articleAdapter

        articleViewModel.typeFilter.value = listOf()
        articleViewModel.typeFilter.observe(this, Observer {filter->
            mainViewModel.isFilterApply.value = !filter.isNullOrEmpty()
        })
        recycler_article.setOnTouchListener { _, _ ->
            mainViewModel.isFilterOpen.value = false
            false
        }

        mainViewModel.isFilterOpen.value = false
        mainViewModel.isFilterOpen.observe(this, Observer {
            if (it) container_el_chip_filter.expand()
            else container_el_chip_filter.collapse()
        })
        for (chip in chip_group_filter_article.children){
            chip.setOnClickListener {
                articleViewModel.typeFilter.value = getFilters(chip_group_filter_article)
            }
        }

        //Swipe Function
        val leftSwipeCallback = ArticleItemTouchHelper(0, ItemTouchHelper.LEFT,
            object : ArticleItemTouchHelper.ArticleItemTouchHelperListener {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
                    val removeItem = (viewHolder as ArticleAdapter.ArticleViewHolder).itemData
                    val removeItemTitle = viewHolder.itemView.tv_article_title.text
                    Snackbar.make(layout_fragment_article, "'$removeItemTitle' is Deleted", Snackbar.LENGTH_LONG).apply {
                        setAction("UNDO") {articleViewModel.restoreArticle(removeItem)}
                    }.show()
                    articleViewModel.removeArticle(removeItem.fb_id)
                }
            })
        val rightSwipeCallback = ArticleItemTouchHelper(0, ItemTouchHelper.RIGHT,
            object : ArticleItemTouchHelper.ArticleItemTouchHelperListener {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
                    val keepId = (viewHolder as ArticleAdapter.ArticleViewHolder).itemData.fb_id
                    articleViewModel.keepArticle(keepId)
                }
            })
        ItemTouchHelper(leftSwipeCallback).attachToRecyclerView(recycler_article)
        ItemTouchHelper(rightSwipeCallback).attachToRecyclerView(recycler_article)
    }
}

