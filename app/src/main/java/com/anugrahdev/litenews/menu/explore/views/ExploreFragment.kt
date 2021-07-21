package com.anugrahdev.litenews.menu.explore.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.databinding.ExploreFragmentBinding
import com.anugrahdev.litenews.databinding.FragmentSavedBinding
import com.anugrahdev.litenews.menu.home.adapters.NewsAdapter
import com.anugrahdev.litenews.menu.about.viewmodels.NewsViewModel
import com.anugrahdev.litenews.utils.NewsViewModelFactory
import com.anugrahdev.litenews.utils.Constants.Companion.NEWS_SEARCH_DELAY
import com.anugrahdev.litenews.utils.Resource
import com.anugrahdev.litenews.utils.toast
import kotlinx.android.synthetic.main.explore_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ExploreFragment : Fragment(), KodeinAware {

    private var _binding: ExploreFragmentBinding?=null
    private val binding get() = _binding!!

    private lateinit var newsAdapter: NewsAdapter
    override val kodein by closestKodein()
    private val factory: NewsViewModelFactory by instance()
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this,factory).get(NewsViewModel::class.java)
        _binding = ExploreFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        setupRecyclerView()

        var job: Job? = null
        binding.etSearch.addTextChangedListener {editable->
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
                    response.data?.let {
                        newsAdapter.submitList(it.articles)
                        layout_recommendedsearch.visibility = View.GONE

                    }
                }
                is Resource.Error->{
                    response.message?.let{
                        requireContext().toast(it)
                    }
                }
                is Resource.Loading->{
                }
            }
        })

        binding.recommend1.setOnClickListener {
            et_search.setText(tv_recommend_1.text.toString())
        }

        binding.recommend2.setOnClickListener {
            et_search.setText(tv_recommend_2.text.toString())
        }

        binding.recommend3.setOnClickListener {
            et_search.setText(tv_recommend_3.text.toString())
        }

    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter{
            val bundle = Bundle().apply {
                putParcelable("article",it)
            }
            findNavController().navigate(
                R.id.action_exploreFragment_to_newsDetailActivity,
                bundle
            )
        }
        recycler_view_newssearched.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


}
