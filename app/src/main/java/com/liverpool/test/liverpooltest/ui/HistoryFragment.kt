package com.liverpool.test.liverpooltest.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.liverpool.test.liverpool.Constants
import com.liverpool.test.liverpooltest.R
import com.liverpool.test.liverpooltest.databinding.FragmentHistoryBinding
import com.liverpool.test.liverpooltest.repository.database.model.Search
import com.liverpool.test.liverpooltest.ui.adapter.HistoryAdapter
import com.liverpool.test.liverpooltest.ui.adapter.ItemHistoryListener
import com.liverpool.test.liverpooltest.viewModel.HistoryViewModel

class HistoryFragment : Fragment() {
    private lateinit var _binding: FragmentHistoryBinding
    val binding: FragmentHistoryBinding get() = _binding

    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner

        adapter = HistoryAdapter(ItemHistoryListener { search -> actionOnClick(search) })
        makeApiCall()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
        binding.historyRecycler.adapter = adapter
        binding.historyRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecycler.setHasFixedSize(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_menu, menu)
    }

    private fun actionOnClick(search: Search) {
        Log.d(Constants.LOG_TAG, "click")
    }

    fun makeApiCall() {
        adapter.submitList(historyViewModel.allSearch)
    }
}