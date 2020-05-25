package com.anugrahdev.litenews.ui.home.health

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.ui.NewsActivity
import com.anugrahdev.litenews.ui.NewsViewModel
import com.anugrahdev.litenews.ui.NewsViewModelFactory
import com.anugrahdev.litenews.ui.home.NewsAdapter
import com.anugrahdev.litenews.ui.home.News_Adapter
import com.anugrahdev.litenews.utils.Resource
import kotlinx.android.synthetic.main.health_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein


class HealthFragment : Fragment() {

    lateinit var newsAdapter: News_Adapter
    private lateinit var viewModel: NewsViewModel
    private val TAG ="HealthFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.health_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("article",it)
            }
            findNavController().navigate(R.id.action_healthFragment_to_newsDetailActivity,bundle)
        }
        viewModel.getNews("id","health")
        viewModel.news.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{

                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                }
                is Resource.Error->{
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    response.message?.let {
                        Log.d(TAG,"Error occured $it")
                    }
                }
                is Resource.Loading->{
                    shimmerFrameLayout.startShimmer()
                    shimmerFrameLayout.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setupRecyclerView(){
        newsAdapter = News_Adapter()
        recycler_view.apply {
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
