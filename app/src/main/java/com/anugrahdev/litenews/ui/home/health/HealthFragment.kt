package com.anugrahdev.litenews.ui.home.health

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.ui.home.HomeViewModel
import com.anugrahdev.litenews.ui.home.HomeViewModelFactory
import com.anugrahdev.litenews.ui.home.NewsAdapter
import kotlinx.android.synthetic.main.home_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein


class HealthFragment : Fragment(), KodeinAware {

    lateinit var viewModel:HomeViewModel
    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance<HomeViewModelFactory>()

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

        viewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)
        viewModel.getHeadlinesHealth()
        viewModel.headlines.observe(viewLifecycleOwner, Observer { news->
            recycler_view.apply {
                this.layoutManager = LinearLayoutManager(requireContext())
                this.adapter = NewsAdapter(news, requireContext())
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                this.visibility = View.VISIBLE
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
