package ru.netology.cryptotrackercoingecko.presentation

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.cryptotrackercoingecko.R
import ru.netology.cryptotrackercoingecko.data.settings.LocaleHelper
import ru.netology.cryptotrackercoingecko.data.settings.SettingsManager
import ru.netology.cryptotrackercoingecko.databinding.ActivityCoinPriceListBinding

class CoinPriceListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoinPriceListBinding
    private val viewModel: CoinListViewModel by viewModels()
    private lateinit var adapter: CoinAdapter
    private lateinit var searchAdapter: CoinAdapter
    private var searchJob: Job? = null
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinPriceListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingsManager = SettingsManager(this)

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

        AlertDialog.Builder(this)
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

    private fun setupRecyclerView(){
        adapter = CoinAdapter(emptyList()) { coin ->
            startActivity(CoinDetailActivity.newIntent(this, coin.id))
        }
        binding.rvCoinPriceList.layoutManager = LinearLayoutManager(this)
        binding.rvCoinPriceList.adapter = adapter
        binding.rvCoinPriceList.itemAnimator = null  // отключает анимации элементов в RecyclerView.
        searchAdapter = CoinAdapter(emptyList()) { coin ->
            startActivity(CoinDetailActivity.newIntent(this, coin.id))
        }
        binding.rvSearchResults.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResults.adapter = searchAdapter
        binding.settingsButton.setOnClickListener {
            startActivity(SettingsActivity.newIntent(this))
        }
        binding.filterButton.setColorFilter(
            ContextCompat.getColor(this, R.color.white)
        )

    }

    private fun setupSearchView() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
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
        lifecycleScope.launch {
            viewModel.filteredCoinList.collectLatest { coins ->
                adapter.updateList(coins)
            }
        }

        lifecycleScope.launch {
            viewModel.searchResults.collectLatest { coins ->
                searchAdapter.updateList(coins)
            }
        }

        lifecycleScope.launch {
            viewModel.showSearchResults.collectLatest { showSearch ->
                binding.rvSearchResults.visibility = if (showSearch) View.VISIBLE else View.GONE
                binding.rvCoinPriceList.visibility = if (showSearch) View.GONE else View.VISIBLE
            }
        }

        lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                error?.let { showError(it) }
            }
        }

        lifecycleScope.launch {
            viewModel.currentFilter.collect { filter ->
                val color = if (filter != "all") {
                    ContextCompat.getColor(this@CoinPriceListActivity, R.color.colorForFilter)
                } else {
                    ContextCompat.getColor(this@CoinPriceListActivity, R.color.white)
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

    override fun attachBaseContext(newBase: Context) {
        val settingsManager = SettingsManager(newBase)
        super.attachBaseContext(LocaleHelper.setLocale(newBase, settingsManager.currentLanguage))
    }


    private fun showKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CoinPriceListActivity::class.java)
        }
    }
}