package ru.netology.cryptotrackercoingecko.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.cryptotrackercoingecko.R
import ru.netology.cryptotrackercoingecko.domain.CoinInfo

class CoinAdapter(
    private var coins: List<CoinInfo>,
    private val onItemClick: (CoinInfo) -> Unit

) : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    fun updateList(newList: List<CoinInfo>) {
        val diffCallback = CoinDiffCallback(coins, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        coins = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coin_info, parent, false)
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = coins[position]
        holder.bind(coin)
        holder.itemView.setOnClickListener { onItemClick(coin) }
    }

    override fun getItemCount(): Int = coins.size

    inner class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val logo: ImageView = itemView.findViewById(R.id.ivLogo)
        private val symbol: TextView = itemView.findViewById(R.id.tvSymbol)
        private val lastMarket: TextView = itemView.findViewById(R.id.tvLastMarket)
        private val price: TextView = itemView.findViewById(R.id.tvPrice)
        private val highDay: TextView = itemView.findViewById(R.id.tvHighDay)
        private val lowDay: TextView = itemView.findViewById(R.id.tvLowDay)

        fun bind(coin: CoinInfo) {

            symbol.text = if (coin.toSymbol != null) {
                "${coin.fromSymbol}/${coin.toSymbol}"
            } else {
                coin.fromSymbol
            }

            price.text = coin.price?.let {
                "$${"%.2f".format(it.toDouble())}"
            } ?: "N/A"

            lastMarket.text = coin.lastMarket ?: "Unknown market"

            highDay.text = coin.highDay ?.let {
                "$${"%.2f".format(it.toDouble())}"
            } ?: "N/A"

            lowDay.text = coin.lowDay ?.let {
                "$${"%.2f".format(it.toDouble())}"
            } ?: "N/A"

            when {
                coin.imageUrl.isNullOrEmpty() -> {
                    logo.setImageResource(R.drawable.ic_coin_error)
                    logo.contentDescription = "Logo is unavailable"
                }

                else -> {

                    Glide.with(itemView.context)
                        .load(coin.imageUrl)
                        .placeholder(R.drawable.ic_coin_placeholder)
                        .error(R.drawable.ic_coin_error)
                        .fallback(R.drawable.ic_coin_error) // Если null URL (дополнительная защита)
                        .into(logo)

                    logo.contentDescription = "Logo ${coin.fromSymbol}"
                }
            }
        }
    }

    private class CoinDiffCallback(
        private val oldList: List<CoinInfo>,
        private val newList: List<CoinInfo>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}