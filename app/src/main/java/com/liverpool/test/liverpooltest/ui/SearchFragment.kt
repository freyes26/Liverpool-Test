package com.liverpool.test.liverpooltest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.liverpool.test.liverpool.Constants
import com.liverpool.test.liverpooltest.R
import com.liverpool.test.liverpooltest.databinding.FragmentSearchBinding
import com.liverpool.test.liverpooltest.repository.network.json.response.Records
import com.liverpool.test.liverpooltest.ui.adapter.ItemRecordListener
import com.liverpool.test.liverpooltest.ui.adapter.SearcherAdapter
import com.liverpool.test.liverpooltest.viewModel.SearcherViewModel

class SearchFragment : Fragment() {
    private lateinit var _binding: FragmentSearchBinding
    val binding: FragmentSearchBinding get() = _binding

    private val searcherViewModel: SearcherViewModel by viewModels()

    private lateinit var adapter: SearcherAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner

        adapter = SearcherAdapter(ItemRecordListener { records -> actionOnClick(records) })
        makeApiCall()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searcherRecycler.adapter = adapter
        binding.searcherRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.searcherRecycler.setHasFixedSize(false)
    }

    private fun actionOnClick(records: Records) {
        Log.d(Constants.LOG_TAG, "Click")
    }

    fun makeApiCall() {
        adapter.submitList(searcherViewModel.allRecords)
    }
}