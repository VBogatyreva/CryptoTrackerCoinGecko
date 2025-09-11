package ru.netology.cryptotrackercoingecko.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.cryptotrackercoingecko.R
import ru.netology.cryptotrackercoingecko.databinding.FragmentCoinDetailBinding
import ru.netology.cryptotrackercoingecko.domain.CoinInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CoinDetailFragment : Fragment() {

    private var _binding: FragmentCoinDetailBinding? = null
    private val binding: FragmentCoinDetailBinding
        get() =_binding ?: throw RuntimeException("FragmentCoinDetailBinding is null")
    private val viewModel: CoinDetailViewModel by viewModels()
    private var coinId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coinId = arguments?.getString("coin_id") ?: ""
        if (coinId.isEmpty()) {
            findNavController().popBackStack()
            return
        }

        setupObservers()
        viewModel.loadCoinDetails(coinId)
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.coinDetails.collectLatest { coin ->
                coin?.let { updateUI(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                error?.let { showError(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
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
                Glide.with(this@CoinDetailFragment)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}