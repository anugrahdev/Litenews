package com.anugrahdev.litenews.ui.home.technology

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.anugrahdev.litenews.R

class TechnologyFragment : Fragment() {

    companion object {
        fun newInstance() = TechnologyFragment()
    }

    private lateinit var viewModel: TechnologyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.technology_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TechnologyViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
