package ru.netology.cryptotrackercoingecko.presentation

import android.app.AlertDialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.cryptotrackercoingecko.R
import ru.netology.cryptotrackercoingecko.data.settings.SettingsManager
import ru.netology.cryptotrackercoingecko.databinding.FragmentCoinPriceListBinding

class CoinPriceListFragment : Fragment() {

    private var _binding: FragmentCoinPriceListBinding? = null
    private val binding: FragmentCoinPriceListBinding
        get() =_binding ?: throw RuntimeException("FragmentCoinPriceListBinding is null")
    private val viewModel: CoinListViewModel by viewModels()
    private lateinit var adapter: CoinAdapter
    private lateinit var searchAdapter: CoinAdapter
    private var searchJob: Job? = null
    private lateinit var settingsManager: SettingsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinPriceListBinding.inflate(inflater, container, false)
        settingsManager = SettingsManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()

        binding.filterButton.setOnClickListener {
            showFilter()
        }

        viewModel.loadCoinList()
    }

    private fun showFilter() {
        val filters = arrayOf(
            getString(R.string.filter_all),
            getString(R.string.filter_top_3),
            getString(R.string.filter_top_5),
            getString(R.string.filter_top_10),
            getString(R.string.filter_top_50),
            getString(R.string.filter_top_100)
        )

        var selectedFilter = viewModel.currentFilter.value
        val checkedItem = when (selectedFilter) {
            "all" -> 0
            "top3" -> 1
            "top5" -> 2
            "top10" -> 3
            "top50" -> 4
            "top100" -> 5
            else -> 0
        }

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.filter_title)
            .setSingleChoiceItems(filters, checkedItem) { _, which ->
                selectedFilter = when (which) {
                    0 -> "all"
                    1 -> "top3"
                    2 -> "top5"
                    3 -> "top10"
                    4 -> "top50"
                    5 -> "top100"
                    else -> "all"
                }
            }
            .setPositiveButton(R.string.apply) { _, _ ->
                viewModel.applyFilter(selectedFilter)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun setupRecyclerView() {
        adapter = CoinAdapter(emptyList()) { coin ->
            findNavController().navigate(
                R.id.action_coinPriceListFragment_to_coinDetailFragment,
                bundleOf("coin_id" to coin.id)
            )
        }

        binding.rvCoinPriceList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCoinPriceList.adapter = adapter
        binding.rvCoinPriceList.itemAnimator = null

        searchAdapter = CoinAdapter(emptyList()) { coin ->
            findNavController().navigate(
                R.id.action_coinPriceListFragment_to_coinDetailFragment,
                bundleOf("coin_id" to coin.id)
            )
        }

        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResults.adapter = searchAdapter

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_coinPriceListFragment_to_settingsFragment
            )
        }

        binding.filterButton.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
    }

    private fun setupSearchView() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    val name = s?.toString()?.trim() ?: ""
                    if (name.isNotEmpty()) {
                        viewModel.searchCoins(name)
                    } else {
                        viewModel.clearSearch()
                    }
                }
            }
        })

        binding.searchCloseButton.setOnClickListener {
            binding.searchEditText.text.clear()
            hideKeyboard()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredCoinList.collectLatest { coins ->
                adapter.updateList(coins)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchResults.collectLatest { coins ->
                searchAdapter.updateList(coins)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.showSearchResults.collectLatest { showSearch ->
                binding.rvSearchResults.visibility = if (showSearch) View.VISIBLE else View.GONE
                binding.rvCoinPriceList.visibility = if (showSearch) View.GONE else View.VISIBLE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                error?.let { showError(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentFilter.collect { filter ->
                val color = if (filter != "all") {
                    ContextCompat.getColor(requireContext(), R.color.colorForFilter)
                } else {
                    ContextCompat.getColor(requireContext(), R.color.white)
                }
                binding.filterButton.setColorFilter(color)
            }
        }
    }

    private fun showError(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        )
            .setAction("Retry") { viewModel.loadCoinList() }
            .show()
    }

    private fun showKeyboard() {
        val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}