package com.liverpool.test.liverpooltest.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.liverpool.test.liverpool.Constants
import com.liverpool.test.liverpooltest.R
import com.liverpool.test.liverpooltest.databinding.FragmentSearchBinding
import com.liverpool.test.liverpooltest.repository.network.json.response.Records
import com.liverpool.test.liverpooltest.ui.Dialog.CloseListener
import com.liverpool.test.liverpooltest.ui.Dialog.DialogMessage
import com.liverpool.test.liverpooltest.ui.adapter.ItemRecordListener
import com.liverpool.test.liverpooltest.ui.adapter.SearcherAdapter
import com.liverpool.test.liverpooltest.viewModel.SearcherViewModel

class SearchFragment : Fragment() {
    private lateinit var _binding: FragmentSearchBinding
    val binding: FragmentSearchBinding get() = _binding

    private val searcherViewModel: SearcherViewModel by activityViewModels()

    private lateinit var adapter: SearcherAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner

        adapter = SearcherAdapter(ItemRecordListener { records,imageView -> actionOnClick(records,imageView) })
        makeApiCall()
        event()
        return binding.root
    }

    fun event(){
        binding.searchImageview.setOnClickListener {
            getAlLRecords()
        }
        binding.searcher.setOnKeyListener{ view, keyCode,_  ->handleKeyEvent(view,keyCode)}
    }

    fun getAlLRecords(){
        val serachString = binding.searcher.text.toString()
        if( serachString != ""){
            searcherViewModel.saveSearch(serachString)
            searcherViewModel.getPlpStates(serachString)
        }
        else{
            val dialog = DialogMessage(getString(R.string.invalidString), CloseListener {restartStatus()})
            dialog.show(requireActivity().supportFragmentManager, Constants.DIALOG_TAG)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searcherRecycler.adapter = adapter
        binding.searcherRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.searcherRecycler.setHasFixedSize(false)
    }

    private fun actionOnClick(records: Records, imageView: ImageView) {
        Log.d(Constants.LOG_TAG, "Click")
    }



    fun makeApiCall() {
        searcherViewModel.allRecords.observe(requireActivity(),{
            it.let {
                adapter.submitList(it)
            }
        })
        searcherViewModel.pettionstatus.observe(requireActivity(),{
            it.let {
                showDialog(it)
            }
        })
    }

    private fun showDialog(status : Int){
        when(status){
            Constants.pettionStatus.ERROR_REQUEST -> {
                val dialog = DialogMessage(getString(R.string.error_message), CloseListener {restartStatus()})
                dialog.show(requireActivity().supportFragmentManager, Constants.DIALOG_TAG)
            }
            Constants.pettionStatus.WITHOUTINTERNET -> {
                val dialog = DialogMessage(getString(R.string.without__internet), CloseListener {restartStatus()})
                dialog.show(requireActivity().supportFragmentManager, Constants.DIALOG_TAG)
            }
            Constants.pettionStatus.WITHOUT_RESULT -> {
                val dialog = DialogMessage(getString(R.string.without_result), CloseListener {restartStatus()})
                dialog.show(requireActivity().supportFragmentManager, Constants.DIALOG_TAG)
            }
            else -> {}
        }

    }

    fun restartStatus(){
        searcherViewModel.resetPetitionStatus()
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}