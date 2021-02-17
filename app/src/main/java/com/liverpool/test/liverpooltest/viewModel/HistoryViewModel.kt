package com.liverpool.test.liverpooltest.viewModel

import androidx.lifecycle.ViewModel
import com.liverpool.test.liverpooltest.repository.database.model.Search

class HistoryViewModel : ViewModel() {
    val allSearch: List<Search> = listOf(
            Search(1, "Esto es una prueba"),
            Search(2, "Esto es una prueba"),
            Search(3, "Esto es una prueba")
    )

}