package com.liverpool.test.liverpooltest.viewModel

import androidx.lifecycle.ViewModel
import com.liverpool.test.liverpooltest.repository.network.json.response.Records
import com.liverpool.test.liverpooltest.repository.network.json.response.VariantsColor

class SearcherViewModel : ViewModel() {
    val allRecords = listOf<Records>(
            Records("1", "1", "Esto es una prueba", "prueba", 1, 2.0, 1234.0, 123.0,
                    123.0, 0.0, 0.0, 0.0, true, true, true, "prueba", "prueba",
                    "prueba", "prueba", "prueba", "prueba", "prueba", listOf<VariantsColor>())
    )
}