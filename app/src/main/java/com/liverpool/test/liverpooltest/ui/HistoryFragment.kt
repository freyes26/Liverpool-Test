package com.liverpool.test.liverpooltest.ui

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
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

    private val historyViewModel: HistoryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }



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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.delete_all -> {
                historyViewModel.deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun actionOnClick(search: Search) {
        historyViewModel.delete(search)
    }

    fun makeApiCall() {
        historyViewModel.allSearch.observe(requireActivity(), {
            it.let {
                adapter.submitList(it as MutableList<Search>)
            }
        })
    }
}