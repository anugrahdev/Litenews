package com.anugrahdev.litenews.ui.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.ui.NewsActivity
import com.anugrahdev.litenews.ui.NewsViewModel
import com.anugrahdev.litenews.ui.home.News_Adapter
import com.anugrahdev.litenews.utils.snackbarlong
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.fragment_saved.*
import kotlinx.android.synthetic.main.fragment_saved.shimmerFrameLayout
import kotlinx.android.synthetic.main.home_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class SavedFragment : Fragment() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: News_Adapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        (activity as AppCompatActivity).supportActionBar?.hide()
        viewModel = (activity as NewsActivity).viewModel
        (activity as NewsActivity).supportActionBar?.show()

        setupRecyclerView()
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("article",it)
            }
            findNavController().navigate(
                R.id.action_savedFragment_to_newsDetailActivity,
                bundle
            )
        }

        viewModel.getSavedArticle().observe(viewLifecycleOwner, Observer {
            newsAdapter.differ.submitList(it)
            shimmerFrameLayout.stopShimmer()
            shimmerFrameLayout.visibility = View.GONE
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteSavedArticle(article)
                view?.rootView?.let { Snackbar.make(it, "Successfully Delete Article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }.show()
                } }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recycler_view_saved)
        }

    }

    private fun setupRecyclerView(){
        newsAdapter = News_Adapter()
        recycler_view_saved.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

        override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmer()
    }

    override fun onPause() {
        shimmerFrameLayout.stopShimmer()
        super.onPause()
    }
}
