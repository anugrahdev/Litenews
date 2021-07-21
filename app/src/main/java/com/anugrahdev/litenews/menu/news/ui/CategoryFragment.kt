package com.anugrahdev.litenews.menu.news.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.anugrahdev.litenews.databinding.FragmentCategoryBinding
import com.anugrahdev.litenews.menu.news.viewmodels.NewsArticleViewModel
import com.anugrahdev.litenews.menu.news.adapters.NewsPagingAdapter2
import com.anugrahdev.litenews.utils.NewsViewModelFactory
import com.anugrahdev.litenews.utils.LoadingState
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class CategoryFragment : Fragment(), KodeinAware {

    private var _binding: FragmentCategoryBinding?=null
    private val binding get() = _binding!!

    override val kodein by closestKodein()
    private lateinit var newsAdapter: NewsPagingAdapter2
    private lateinit var viewModel: NewsArticleViewModel
    private val args: CategoryFragmentArgs by navArgs()
    private val factory: NewsViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this,factory).get(NewsArticleViewModel::class.java)
        if (args.sourceId == "all"){
            viewModel.category = args.category
        }else {
            viewModel.source = args.sourceId
        }
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = args.sourceName
        viewModel.init()
        setupRecyclerView()
        observeData()

    }

    private fun observeData() {
        viewModel.listData.observe(viewLifecycleOwner, {
            newsAdapter.notifyDataSetChanged()
            newsAdapter.submitList(it)
        })
        viewModel.getLoadingState().observe(viewLifecycleOwner, {
            if (it == LoadingState.NO_DATA) {
                newsAdapter.noMoreData()
            }
        })
        viewModel.msg.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
        viewModel.isLoading().observe(viewLifecycleOwner, {
            if (it){
                startLoading()
            }else{
                stopLoading()
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startLoading(){
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
    }

    private fun stopLoading(){
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
    }


    private fun setupRecyclerView(){
        newsAdapter = NewsPagingAdapter2()
        binding.recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)

        }

    }


}