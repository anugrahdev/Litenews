package com.anugrahdev.litenews.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.adapter.CategoryAdapter
import com.anugrahdev.litenews.adapter.NewsAdapter
import com.anugrahdev.litenews.adapter.SliderAdapter
import com.anugrahdev.litenews.data.entities.Article
import com.anugrahdev.litenews.data.entities.Category
import com.anugrahdev.litenews.preferences.PreferenceProvider
import com.anugrahdev.litenews.ui.NewsActivity
import com.anugrahdev.litenews.ui.NewsViewModel
import com.anugrahdev.litenews.utils.Resource
import com.anugrahdev.litenews.utils.start
import com.anugrahdev.litenews.utils.stop
import com.anugrahdev.litenews.utils.toast
import com.smarteist.autoimageslider.IndicatorAnimations
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.shimmer.*


class HomeFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private var sliderAdapter = SliderAdapter(emptyList())
    private val newsAdapter by lazy { NewsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.bottom_nav?.visibility = View.VISIBLE
        activity?.toolbar_line?.visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.hide()
        viewModel = (activity as NewsActivity).viewModel
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
        newsAdapter.setOnItemClickListener {
            navigateToDetail(it)
        }
        viewModel.headlineNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.subList(3, it.articles.size - 1))
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

    }

    private fun setupRecyclerView() {
        recycler_view.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun navigateToDetail(it:Article){
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


