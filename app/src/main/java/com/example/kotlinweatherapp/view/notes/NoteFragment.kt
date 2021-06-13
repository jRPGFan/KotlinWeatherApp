package com.example.kotlinweatherapp.view.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.CityNoteFragmentBinding
import com.example.kotlinweatherapp.room.NoteEntity
import com.example.kotlinweatherapp.utilities.showSnackbarNoAction
import com.example.kotlinweatherapp.viewmodel.AppState
import com.example.kotlinweatherapp.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar

class NoteFragment : DialogFragment() {
    private var _binding: CityNoteFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this).get(NoteViewModel::class.java)
    }
    private val cityBundle: String by lazy {
        arguments?.getString(CITY_BUNDLE_EXTRA) ?: "Moscow"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CityNoteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.noteLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getNoteByCity(cityBundle)

        binding.saveNote.setOnClickListener {
            saveNote(
                NoteEntity(cityBundle, binding.cityNote.text.toString())
            )
            dismiss()
        }

        binding.deleteNote.setOnClickListener {
            deleteNote(
                NoteEntity(cityBundle, binding.cityNote.text.toString())
            )
            dismiss()
        }

        binding.closeNote.setOnClickListener {
            dismiss()
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.NoteLoaded -> {
                setNote(appState.note)
            }
            else -> {
                binding.cityNoteFragment.showSnackbarNoAction(
                    getString(R.string.Unknown_Error),
                    Snackbar.LENGTH_SHORT
                )
            }
        }
    }

    private fun setNote(note: String?) {
        note?.apply { binding.cityNote.setText(note) }
    }

    private fun saveNote(note: NoteEntity) {
        viewModel.saveNoteToDB(note)
    }

    private fun deleteNote(note: NoteEntity) {
        viewModel.deleteNoteFromDB(note)
    }

    companion object {
        const val CITY_BUNDLE_EXTRA = "city"

        @JvmStatic
        fun newInstance(bundle: Bundle): NoteFragment {
            val fragment = NoteFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}