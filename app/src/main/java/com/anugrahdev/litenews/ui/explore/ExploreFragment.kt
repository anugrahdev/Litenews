package com.anugrahdev.litenews.ui.explore

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.ui.NewsActivity
import com.anugrahdev.litenews.ui.NewsViewModel
import com.anugrahdev.litenews.ui.home.News_Adapter
import com.anugrahdev.litenews.utils.Constants.Companion.NEWS_SEARCH_DELAY
import com.anugrahdev.litenews.utils.Resource
import kotlinx.android.synthetic.main.explore_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExploreFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: News_Adapter
    private val TAG="ExplorFragment"
    companion object {
        fun newInstance() = ExploreFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

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
                    response.message?.let {
                        Log.d(TAG,"Error occured $it")
                    }
                }
                is Resource.Loading->{
                    layout_recommendedsearch.visibility = View.VISIBLE

                }
            }
        })


    }

    private fun setupRecyclerView(){
        newsAdapter = News_Adapter()
        recycler_view_newssearched.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }





}
