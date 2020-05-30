package com.anugrahdev.litenews.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.ui.NewsActivity
import com.anugrahdev.litenews.ui.NewsViewModel
import com.anugrahdev.litenews.ui.home.NewsAdapter
import com.anugrahdev.litenews.utils.Constants.Companion.NEWS_SEARCH_DELAY
import com.anugrahdev.litenews.utils.Resource
import com.anugrahdev.litenews.utils.toast
import kotlinx.android.synthetic.main.explore_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExploreFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.explore_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("article",it)
            }
            findNavController().navigate(
                R.id.action_exploreFragment_to_newsDetailActivity,
                bundle
            )
        }

        var job: Job? = null
        et_search.addTextChangedListener {editable->
            job?.cancel()
            job = MainScope().launch {
                delay(NEWS_SEARCH_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{
                    layout_recommendedsearch.visibility = View.GONE
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error->{
                    response.message?.let{
                        requireContext().toast(it)
                    }
                }
                is Resource.Loading->{
                    layout_recommendedsearch.visibility = View.VISIBLE
                }
            }
        })

        recommend_1.setOnClickListener {
            et_search.setText(tv_recommend_1.text.toString())
        }

        recommend_2.setOnClickListener {
            et_search.setText(tv_recommend_2.text.toString())
        }

        recommend_3.setOnClickListener {
            et_search.setText(tv_recommend_3.text.toString())
        }



    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        recycler_view_newssearched.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


}
