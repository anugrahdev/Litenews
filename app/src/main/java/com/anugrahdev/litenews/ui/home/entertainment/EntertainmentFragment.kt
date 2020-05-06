package com.anugrahdev.litenews.ui.home.entertainment

import androidx.lifecycle.ViewModelProviders
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
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class EntertainmentFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance<HomeViewModelFactory>()
    companion object {
        fun newInstance() = EntertainmentFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.entertainment_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()

        viewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)
        viewModel.getHeadlinesEntertainment()
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
