package ru.netology.cryptotrackercoingecko.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.cryptotrackercoingecko.R
import ru.netology.cryptotrackercoingecko.databinding.ActivityCoinDetailBinding
import ru.netology.cryptotrackercoingecko.domain.CoinInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CoinDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoinDetailBinding
    private val viewModel: CoinDetailViewModel by viewModels()
    private var coinId: String = ""

    companion object {
        private const val EXTRA_COIN_ID = "coin_id"

        fun newIntent(context: Context, coinId: String): Intent {
            return Intent(context, CoinDetailActivity::class.java).apply {
                putExtra(EXTRA_COIN_ID, coinId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCoinDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coinId = intent.getStringExtra(EXTRA_COIN_ID) ?: ""
        if (coinId.isEmpty()) finish()

        setupObservers()
        viewModel.loadCoinDetails(coinId)

    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.coinDetails.collectLatest { coin ->
                coin?.let { updateUI(it) }
            }
        }

        lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                error?.let { showError(it) }
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun updateUI(coin: CoinInfo) {
        with(binding) {
            tvSymbol.text = if (coin.toSymbol?.isNotEmpty() == true) {
                "${coin.fromSymbol}/${coin.toSymbol}"
            } else {
                coin.fromSymbol
            }

            tvPrice.text = if (!coin.price.isNullOrEmpty()) {
                "$${coin.price}"
            } else {
                "N/A"
            }

            tvLastMarket.text = coin.lastMarket ?: "Unknown"

            tvHighDay.text = if (!coin.highDay.isNullOrEmpty()) {
                "$${coin.highDay}"
            } else {
                "N/A"
            }

            tvLowDay.text = if (!coin.lowDay.isNullOrEmpty()) {
                "$${coin.lowDay}"
            } else {
                "N/A"
            }

            val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val date = coin.lastUpdate?.let { Date(it) } ?: Date()
            tvLastUpdate.text = "Updated: ${dateFormat.format(date)}"

            if (!coin.imageUrl.isNullOrEmpty()) {
                Glide.with(this@CoinDetailActivity)
                    .load(coin.imageUrl)
                    .placeholder(R.drawable.ic_coin_placeholder)
                    .error(R.drawable.ic_coin_error)
                    .into(ivLogo)

                ivLogo.contentDescription = "Logo ${coin.fromSymbol}"
            } else {
                ivLogo.setImageResource(R.drawable.ic_coin_error)
                ivLogo.contentDescription = "Logo is unavailable"
            }
        }
    }


    private fun showError(message: String) {
        Snackbar.make(
            binding.root,
            "ERROR: $message",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Retry") { viewModel.loadCoinDetails(coinId) }
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
