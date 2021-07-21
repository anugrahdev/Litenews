package com.anugrahdev.litenews.menu.saved.views

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.databinding.FragmentSavedBinding
import com.anugrahdev.litenews.databinding.HomeFragmentBinding
import com.anugrahdev.litenews.menu.home.adapters.NewsAdapter
import com.anugrahdev.litenews.menu.about.viewmodels.NewsViewModel
import com.anugrahdev.litenews.utils.NewsViewModelFactory
import com.anugrahdev.litenews.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class SavedFragment : Fragment(), KodeinAware {

    private var _binding: FragmentSavedBinding?=null
    private val binding get() = _binding!!

    override val kodein by closestKodein()
    private val factory: NewsViewModelFactory by instance()
    private lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this,factory).get(NewsViewModel::class.java)
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        setupRecyclerView()
        viewModel.getSavedArticle().observe(viewLifecycleOwner, Observer {
            newsAdapter.submitList(it.reversed())
        })

        binding.etSearch.text.isNotEmpty().let {
            var job: Job? = null
            binding.etSearch.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(Constants.NEWS_SEARCH_DELAY)
                    editable?.let {
                        viewModel.searchSavedArticle(editable.toString())
                            .observe(viewLifecycleOwner, Observer {
                                it.isNotEmpty().apply {
                                    newsAdapter.submitList(it.reversed())
                                }
                            })

                    }
                }

            }
        }




        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.currentList[position]
                viewModel.deleteSavedArticle(article)
                view?.rootView?.let {
                    Snackbar.make(it, "Successfully Delete Article", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo") {
                            viewModel.saveArticle(article)
                        }.show()
                    }
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recycler_view_saved)
        }
    }
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter {
            val bundle = Bundle().apply {
                putParcelable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedFragment_to_newsDetailActivity,
                bundle
            )
        }
        binding.recyclerViewSaved.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}
