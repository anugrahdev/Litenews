package com.anugrahdev.litenews.ui.home.business

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.ui.home.HomeViewModel
import com.anugrahdev.litenews.ui.home.HomeViewModelFactory
import com.anugrahdev.litenews.ui.home.NewsAdapter
import kotlinx.android.synthetic.main.business_fragment.*
import kotlinx.android.synthetic.main.business_fragment.recycler_view
import kotlinx.android.synthetic.main.business_fragment.shimmerFrameLayout
import kotlinx.android.synthetic.main.home_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class BusinessFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    companion object {
        fun newInstance() = BusinessFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private val factory:HomeViewModelFactory by instance<HomeViewModelFactory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.business_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()

        viewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)
        viewModel.getHeadlinesBusiness()
        viewModel.headlines.observe(viewLifecycleOwner, Observer {news->
            recycler_view.also{
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = NewsAdapter(news,requireContext())
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
