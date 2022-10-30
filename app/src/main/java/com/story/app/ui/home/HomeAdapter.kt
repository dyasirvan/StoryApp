package com.story.app.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.story.app.core.data.local.entity.StoryEntity
import com.story.app.core.data.remote.model.ListStoryItem
import com.story.app.databinding.ItemHomeBinding
import com.story.app.ui.detail.DetailActivity

class HomeAdapter : PagingDataAdapter<StoryEntity, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        const val EXTRA_STORY = "extra_story"
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
    }

    class ViewHolder(private val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: StoryEntity?) {
            with(binding) {
                result?.apply {
                    tvStory.text = name
                    Glide.with(binding.root)
                        .load(photoUrl)
                        .into(ivStory)

                    itemView.setOnClickListener {
                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                itemView.context as Activity,
                                Pair(ivStory, "image"),
                                Pair(tvStory, "name")
                            )
                        Intent(itemView.context, DetailActivity::class.java)
                            .apply {
                                putExtra(EXTRA_STORY, result)
                                itemView.context.startActivity(
                                    this,
                                    optionsCompat.toBundle()
                                )
                            }
                    }
                }
            }
        }

    }
}