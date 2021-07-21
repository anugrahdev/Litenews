package com.anugrahdev.litenews.menu.settings.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.databinding.FragmentSourcesBinding
import com.anugrahdev.litenews.databinding.SettingsFragmentBinding
import com.anugrahdev.litenews.preferences.PreferenceProvider
import com.anugrahdev.litenews.menu.about.views.AboutActivity
import com.anugrahdev.litenews.menu.home.views.NewsActivity
import com.neovisionaries.i18n.CountryCode
import kotlinx.android.synthetic.main.settings_fragment.*
import java.util.*

class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding?=null
    private val binding get() = _binding!!

    lateinit var prefs: PreferenceProvider
    lateinit var countryCode:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = (activity as NewsActivity).prefs

        if (prefs.getDarkMode()){
            binding.switchDarkmode.isChecked = true
        }

        binding.switchDarkmode.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                prefs.setDarkMode(true)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                prefs.setDarkMode(false)
            }
        }

        val countryList = listOf("France","United Kingdom","Indonesia","Japan","United States","Singapore")
        val country :String?
        country = CountryCode.getByCode(prefs.getCountry().toUpperCase(Locale.ROOT)).getName().toString()
        binding.tvSelectedCountry.setText(country)
        binding.settingCountry.setOnClickListener {
            MaterialDialog(requireContext()).show {
                var init = 0
                for (i in countryList.indices){
                    countryCode = CountryCode.findByName(countryList[i]).first().toString()
                    if (countryCode == prefs.getCountry()){
                        init = i
                    }
                }
                listItemsSingleChoice(items = countryList, initialSelection = init){ dialog, index, text ->
                    activity?.tv_selectedCountry?.setText(countryList[index])
                    prefs.setCountry(CountryCode.findByName(countryList[index]).first().toString())
                    activity?.recreate()
                    activity?.viewModelStore?.clear()
                }
            }
        }
        binding.settingAbout.setOnClickListener {
            Intent(requireContext(), AboutActivity::class.java).also {
                startActivity(it)
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

}
