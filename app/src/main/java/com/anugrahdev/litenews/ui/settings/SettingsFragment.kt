package com.anugrahdev.litenews.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.anugrahdev.litenews.R
import com.anugrahdev.litenews.preferences.PreferenceProvider
import com.anugrahdev.litenews.ui.NewsActivity
import com.anugrahdev.litenews.ui.NewsViewModel
import com.neovisionaries.i18n.CountryCode
import kotlinx.android.synthetic.main.settings_fragment.*
import java.util.*

const val TAG = "SettingsFragment"
class SettingsFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    lateinit var prefs: PreferenceProvider
    lateinit var countryCode:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        viewModel = (activity as NewsActivity).viewModel
        prefs = (activity as NewsActivity).prefs

        if (prefs.getDarkMode()){
            switch_darkmode.isChecked = true
        }

        switch_darkmode.setOnCheckedChangeListener { buttonView, isChecked ->
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
        Log.d(TAG, CountryCode.findByName("United Kingdom").toString())
        tv_selectedCountry.setText(country)
        settingCountry.setOnClickListener {
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

    }

}
