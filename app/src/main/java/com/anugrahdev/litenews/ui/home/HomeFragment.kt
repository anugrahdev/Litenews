package com.anugrahdev.litenews.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.data.db.entities.Category
import kotlinx.android.synthetic.main.home_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class HomeFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val factory:HomeViewModelFactory by instance<HomeViewModelFactory>()
    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel

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



        viewModel.getHeadlines()
        viewModel.headlines.observe(viewLifecycleOwner, Observer {news->
            recycler_view.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = context?.let { it1 -> NewsAdapter(news, it1) }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

}
