package com.anugrahdev.litenews.menu.home.views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.menu.home.adapters.CategoryAdapter
import com.anugrahdev.litenews.menu.home.adapters.SliderAdapter
import com.anugrahdev.litenews.menu.home.models.Article
import com.anugrahdev.litenews.menu.home.models.Category
import com.anugrahdev.litenews.databinding.HomeFragmentBinding
import com.anugrahdev.litenews.menu.home.adapters.NewsAdapter
import com.anugrahdev.litenews.menu.about.viewmodels.NewsViewModel
import com.anugrahdev.litenews.utils.NewsViewModelFactory
import com.anugrahdev.litenews.utils.Resource
import com.anugrahdev.litenews.utils.start
import com.anugrahdev.litenews.utils.stop
import com.anugrahdev.litenews.utils.toast
import com.smarteist.autoimageslider.IndicatorAnimations
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.shimmer.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber


class HomeFragment : Fragment(), KodeinAware {

    private var _binding: HomeFragmentBinding?=null
    private val binding get() = _binding!!

    override val kodein by closestKodein()
    private lateinit var viewModel: NewsViewModel
    private val factory: NewsViewModelFactory by instance()
    private var sliderAdapter = SliderAdapter(emptyList())
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this,factory).get(NewsViewModel::class.java)
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHeadlineNews(viewModel.country,"")
        swipeRefresh()
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

        )


        recycler_view_category.also {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter =
                CategoryAdapter(categoryList)
        }

        setupRecyclerView()

        viewModel.headlineNews.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        newsAdapter.submitList(it.articles.subList(3, it.articles.size - 1))
                        Timber.d("length ${it.articles.subList(3, it.articles.size - 1).size}")
                        sliderAdapter = SliderAdapter(it.articles.subList(0, 3)).apply {
                            setOnSliderClickListener { article->
                                navigateToDetail(article)
                            }
                        }
                        imageSlider.setSliderAdapter(sliderAdapter)
                        imageSlider.startAutoCycle()
                        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
                    }
                    shimmerFrameLayout.stop()
                    imageSlider.visibility = View.VISIBLE

                }
                is Resource.Error -> {
                    response.message?.let {
                        requireContext().toast("Error occured : $it")
                    }
                    shimmerFrameLayout.stop()

                }
                is Resource.Loading -> {
                    shimmerFrameLayout.start()
                    imageSlider.visibility = View.GONE
                }
            }
        })
        viewModel.msg.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.bottom_nav?.visibility = View.VISIBLE
        activity?.toolbar_line?.visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter {
            navigateToDetail(it)
        }
        recycler_view.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun navigateToDetail(it: Article){
        val bundle = Bundle().apply {
            putParcelable("article", it)
        }
        findNavController().navigate(
            R.id.action_homeFragment_to_newsDetailActivity,
            bundle
        )
    }

    private fun swipeRefresh(){
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.main_blue))
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE)

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getHeadlineNews(viewModel.country," ")
            swipeRefreshLayout.isRefreshing = false
        }
    }


}


