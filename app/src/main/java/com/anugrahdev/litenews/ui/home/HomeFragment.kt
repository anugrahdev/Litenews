package com.anugrahdev.litenews.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.data.db.entities.Category
import com.anugrahdev.litenews.ui.NewsActivity
import com.anugrahdev.litenews.ui.NewsViewModel
import com.anugrahdev.litenews.ui.NewsViewModelFactory
import com.anugrahdev.litenews.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.home_fragment.shimmerFrameLayout
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class HomeFragment : Fragment() {
    lateinit var viewModel:NewsViewModel
    lateinit var newsAdapter:News_Adapter
    private val TAG="HomeFragment"
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.bottom_nav?.visibility = View.VISIBLE

        (activity as AppCompatActivity).supportActionBar?.hide()

        viewModel = (activity as NewsActivity).viewModel

        val categoryList = listOf(
            Category(
                R.drawable.technology,
                "Technology"
            ),
            Category(
                R.drawable.sport,
                "Sports"
            ),
            Category(
                R.drawable.business,
                "Business"
            ),
            Category(
                R.drawable.science,
                "Science"
            ),
            Category(
                R.drawable.health,
                "Health"
            ),
            Category(
                R.drawable.entertainment,
                "Entertainment"
            )

        );


        recycler_view_category.also{
            it.layoutManager =LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = CategoryAdapter(categoryList)
        }

        setupRecyclerView()
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("article",it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_newsDetailActivity,
                bundle
            )
        }
        viewModel.getNews("id"," ")
        viewModel.news.observe(viewLifecycleOwner, Observer {response->
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
                        Log.d(TAG,"Error occured $it")
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
        newsAdapter = News_Adapter()
        recycler_view.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }



}