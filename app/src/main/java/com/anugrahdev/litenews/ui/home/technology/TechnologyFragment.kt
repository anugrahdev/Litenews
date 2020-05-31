package com.anugrahdev.litenews.ui.home.technology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.ui.NewsActivity
import com.anugrahdev.litenews.ui.NewsViewModel
import com.anugrahdev.litenews.adapter.NewsAdapter
import com.anugrahdev.litenews.utils.Resource
import com.anugrahdev.litenews.utils.toast
import kotlinx.android.synthetic.main.technology_fragment.*

class TechnologyFragment : Fragment(){
    lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.technology_fragment, container, false)
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
            findNavController().navigate(R.id.action_technologyFragment_to_newsDetailActivity,bundle)
        }
        viewModel.getTechNews(viewModel.country,"technology")
        viewModel.techNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                        stopLoading()
                    }
                }
                is Resource.Error->{
                    stopLoading()
                    response.message?.let {
                        requireContext().toast("Error occured : $it")
                    }
                }
                is Resource.Loading->{
                    startLoading()
                }
            }
        })



    }
    private fun startLoading(){
        shimmerFrameLayout.startShimmer()
        shimmerFrameLayout.visibility = View.VISIBLE
    }

    private fun stopLoading(){
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
    }


    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        recycler_view.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


}