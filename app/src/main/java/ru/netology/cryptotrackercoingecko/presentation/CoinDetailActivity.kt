package ru.netology.cryptotrackercoingecko.presentation

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ru.netology.cryptotrackercoingecko.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CoinDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)

        val fromSymbol = intent.getStringExtra("FROM_SYMBOL") ?: ""
        val toSymbol = intent.getStringExtra("TO_SYMBOL") ?: ""
        val price = intent.getStringExtra("PRICE") ?: ""
        val lastUpdate = intent.getLongExtra("LAST_UPDATE", 0)
        val highDay = intent.getStringExtra("HIGH_DAY") ?: ""
        val lowDay = intent.getStringExtra("LOW_DAY") ?: ""
        val lastMarket = intent.getStringExtra("LAST_MARKET") ?: ""
        val imageUrl = intent.getStringExtra("IMAGE_URL") ?: ""

        val tvSymbol = findViewById<TextView>(R.id.tvSymbol)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val tvLastUpdate = findViewById<TextView>(R.id.tvLastUpdate)
        val tvLastMarket = findViewById<TextView>(R.id.tvLastMarket)
        val tvHighDay = findViewById<TextView>(R.id.tvHighDay)
        val tvLowDay = findViewById<TextView>(R.id.tvLowDay)
        val ivLogo = findViewById<ImageView>(R.id.ivLogo)

        tvSymbol.text = if (toSymbol.isNotEmpty()) {
            "$fromSymbol/$toSymbol"
        } else {
            fromSymbol
        }

        tvPrice.text = if (price.isNotEmpty()) {
            "$$price"
        } else {
            "N/A"
        }

        tvLastMarket.text = lastMarket.ifEmpty { "Unknown" }

        tvHighDay.text = if (highDay.isNotEmpty()) {
            "$$highDay"
        } else {
            "N/A"
        }

        tvLowDay.text = if (lowDay.isNotEmpty()) {
            "$$lowDay"
        } else {
            "N/A"
        }

        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date = Date(lastUpdate)
        tvLastUpdate.text = "Updated: ${dateFormat.format(date)}"

        if (imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_coin_placeholder)
                .error(R.drawable.ic_coin_error)
                .into(ivLogo)

            ivLogo.contentDescription = "Логотип $fromSymbol"
        } else {
            ivLogo.setImageResource(R.drawable.ic_coin_error)
            ivLogo.contentDescription = "Логотип недоступен"
        }
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