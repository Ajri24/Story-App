package com.zenitsu.ceritakita.view.story.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.zenitsu.ceritakita.R
import com.zenitsu.ceritakita.data.remote.response.ListStoryItem
import com.zenitsu.ceritakita.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listStoryItems = intent.getParcelableArrayListExtra<ListStoryItem>("list_story_item")

        if (!listStoryItems.isNullOrEmpty()) {
            val story = listStoryItems[0]
            binding.tvUsername.text = story.name
            binding.tvUsernameDescription.text = story.description

            Glide.with(this)
                .load(story.photoUrl)
                .into(binding.imgDetailStory)
        }
    }

}