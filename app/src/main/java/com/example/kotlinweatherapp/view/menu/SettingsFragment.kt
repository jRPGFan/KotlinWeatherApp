package com.example.kotlinweatherapp.view.menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlinweatherapp.databinding.SettingsFragmentBinding

const val IS_CELSIUS = "IS_CELSIUS"

class SettingsFragment : Fragment() {
    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            binding.cFSwitch.isChecked =
                it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_CELSIUS, true)
        }

        binding.cFSwitch.setOnClickListener {
            activity?.let {
                with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                    putBoolean(IS_CELSIUS, binding.cFSwitch.isChecked)
                    apply()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}