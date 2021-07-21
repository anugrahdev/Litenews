package com.anugrahdev.litenews.menu.sources.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.databinding.FragmentSourcesBinding
import com.anugrahdev.litenews.menu.sources.adapters.SourceAdapter
import com.anugrahdev.litenews.menu.sources.models.SourceModel
import com.anugrahdev.litenews.menu.sources.viewmodels.SourceViewModel
import com.anugrahdev.litenews.utils.NewsViewModelFactory
import com.anugrahdev.litenews.utils.Resource
import com.anugrahdev.litenews.utils.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class SourcesFragment : Fragment(), KodeinAware {

    private var _binding: FragmentSourcesBinding?=null
    private val binding get() = _binding!!

    override val kodein by closestKodein()
    private lateinit var viewModel: SourceViewModel
    private val factory: NewsViewModelFactory by instance()
    private val args: SourcesFragmentArgs by navArgs()
    private lateinit var sourceAdapter: SourceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this,factory).get(SourceViewModel::class.java)
        _binding = FragmentSourcesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = args.category
        setupRecyclerView()

        viewModel.getNewsSources(country = viewModel.country, category = args.category)
        viewModel.sources.observe(viewLifecycleOwner, { response ->
            when(response){
                is Resource.Success->{
                    response.data?.let {
                        if (it.isEmpty()){
                            binding.llSourceEmpty.visibility = View.VISIBLE
                        }else {
                            val allSource = SourceModel(category = args.category, country = "", description = "", id = "all", language = "", name = "All Source", url = "")
                            sourceAdapter.submitList(mutableListOf(allSource) + it)

                        }
                        stopLoading()
                    }
                }
                is Resource.Error->{
                    stopLoading()
                    response.message?.let {
                        requireContext().toast("Error occured : $it")
                    }
                }
                is Resource.Loading->{
                    startLoading()
                }
            }
        })

    }

    private fun startLoading(){
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.rvSources.visibility = View.GONE
    }

    private fun stopLoading(){
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.rvSources.visibility = View.VISIBLE
    }


    private fun setupRecyclerView(){
        sourceAdapter = SourceAdapter {
            val bundle = Bundle().apply {
                putString("sourceId", it.id)
                putString("sourceName", it.name)
                putString("category", args.category)
            }
            findNavController().navigate(R.id.action_sourcesFragment_to_technologyFragment, bundle)

        }
        binding.rvSources.apply {
            adapter = sourceAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}