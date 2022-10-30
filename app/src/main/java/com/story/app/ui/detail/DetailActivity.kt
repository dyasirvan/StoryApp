package com.story.app.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.story.app.core.data.local.entity.StoryEntity
import com.story.app.databinding.ActivityDetailBinding
import com.story.app.ui.home.HomeAdapter

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        val data = extras?.getParcelable<StoryEntity>(HomeAdapter.EXTRA_STORY)

        with(binding) {
            data?.apply {
                tvName.text = name
                tvDesc.text = description
                Glide.with(this@DetailActivity)
                    .load(photoUrl)
                    .into(ivStory)
            }
        }
    }
}