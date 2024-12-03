@file:Suppress("unused", "RedundantSuppression")

package com.example.projectadvocata.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.projectadvocata.ui.detail.DetailViewModel
import com.example.projectadvocata.R
import com.example.projectadvocata.data.local.entity.EventEntity
import com.example.projectadvocata.data.repo.Result
import com.example.projectadvocata.databinding.ActivityDetailBinding
import com.example.projectadvocata.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var isBookmarked = false
    private var currentEvent: EventEntity? = null
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.overflowIcon?.setTint(ContextCompat.getColor(this, R.color.white))

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(
                ContextCompat.getDrawable(
                    this@DetailActivity,
                    R.drawable.arrow_back
                )
            )
            title = getString(R.string.detail_event)
        }

        val eventId = intent.getStringExtra(EXTRA_EVENT_ID)

        if (eventId != null) {
            observeEventDetails(eventId)
        }

        binding.floatingActionButton.setOnClickListener {
            toggleBookmark()
        }
    }

    private fun observeEventDetails(eventId: String) {
        detailViewModel.getDetailEvent(eventId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val event = result.data
                    currentEvent = event
                    isBookmarked = event.isBookmarked
                    updateFabIcon(isBookmarked)
                    populateEventDetails(event)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun populateEventDetails(event: EventEntity) {
        val remainingQuota = event.quota - event.registrants
        binding.eventName.text = event.name
        binding.eventDescription.text =
            HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.eventCategory.text = event.category
        binding.eventOwner.text = HtmlCompat.fromHtml(
            getString(R.string.diselenggarakan_oleh, event.ownerName),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.eventCity.text = event.cityName
        binding.eventSummary.text = event.summary
        binding.eventQuota.text = HtmlCompat.fromHtml(
            getString(R.string.sisa_quota, remainingQuota.toString()),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.eventBeginTime.text = event.beginTime

        binding.linkButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(event.link)
            startActivity(intent)
        }

        Glide.with(this)
            .load(event.mediaCover)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fitCenter()
            )
            .into(binding.imageView)
    }

    private fun toggleBookmark() {
        isBookmarked = !isBookmarked
        currentEvent?.let { event ->
            updateFabIcon(isBookmarked)
            if (isBookmarked) {
                detailViewModel.saveEvent(event)
                Toast.makeText(this, getString(R.string.added_to_favorite), Toast.LENGTH_SHORT)
                    .show()
            } else {
                detailViewModel.deleteEvent(event)
                Toast.makeText(this, getString(R.string.removed_from_favorite), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun updateFabIcon(isBookmarked: Boolean) {
        val iconRes = if (isBookmarked) R.drawable.favorite else R.drawable.favorite_border
        binding.floatingActionButton.setImageResource(iconRes)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
