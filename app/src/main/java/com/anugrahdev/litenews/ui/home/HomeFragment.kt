package com.anugrahdev.litenews.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.data.db.entities.Category
import com.anugrahdev.litenews.data.network.ApiService
import com.anugrahdev.litenews.data.repositories.HeadlinesRepository
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
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
        viewModel = ViewModelProviders.of(this,factory).get(HomeViewModel::class.java)
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
        val mAdapter = GroupAdapter<ViewHolder>()

        categoryList.map {
            mAdapter.add(CategoryItem(it))
        }

        recycler_view_category.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }

        mAdapter.setOnItemClickListener { item, view ->
            Toast.makeText(context, item.itemCount.toString(), Toast.LENGTH_SHORT).show()


            when(item.id){
                -2L-> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_sportsFragment)
            }

        }

        viewModel.getHeadlines()
        viewModel.headlines.observe(viewLifecycleOwner, Observer {news->
            recycler_view.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = NewsAdapter(news)
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
