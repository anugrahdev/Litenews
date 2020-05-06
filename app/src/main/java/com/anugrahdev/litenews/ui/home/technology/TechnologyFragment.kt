package com.anugrahdev.litenews.ui.home.technology

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.ui.home.HomeViewModel
import com.anugrahdev.litenews.ui.home.HomeViewModelFactory
import com.anugrahdev.litenews.ui.home.NewsAdapter
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.technology_fragment.*
import kotlinx.android.synthetic.main.technology_fragment.recycler_view
import kotlinx.android.synthetic.main.technology_fragment.shimmerFrameLayout
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class TechnologyFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance<HomeViewModelFactory>()
    var isShow = true
    var scrollRange: Int = -1
    companion object {
        fun newInstance() = TechnologyFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.technology_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()

//        appbar.addOnOffsetChangedListener(this)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        viewModel.getHeadlinesTechnology()
        viewModel.headlines.observe(viewLifecycleOwner, Observer {news->
            recycler_view.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = context?.let { it1 -> NewsAdapter(news, it1) }
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                it.visibility = View.VISIBLE
            }
        })
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
