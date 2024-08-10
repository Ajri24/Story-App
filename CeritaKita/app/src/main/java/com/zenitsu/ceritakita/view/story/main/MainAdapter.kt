package com.zenitsu.ceritakita.view.story.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zenitsu.ceritakita.data.remote.response.ListStoryItem
import com.zenitsu.ceritakita.databinding.ItemMainBinding
import com.zenitsu.ceritakita.view.story.detail.DetailActivity

class MainAdapter : ListAdapter<ListStoryItem, MainAdapter.MainViewHolder>(DIFF_CALLBACK) {

    class MainViewHolder(private val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            binding.userName.text = story.name
            binding.descUserStory.text = story.description

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.imageUserStory)

            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.imageUserStory, "imgStory"),
                    Pair(binding.descUserStory, "tvUsernameDesc")
                )
                val intent = Intent(itemView.context, DetailActivity::class.java)
                val listStoryItems = ArrayList<ListStoryItem>()
                listStoryItems.add(story)
                intent.putParcelableArrayListExtra("list_story_item", listStoryItems)
                itemView.context.startActivity(intent,
                    optionsCompat.toBundle()
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
