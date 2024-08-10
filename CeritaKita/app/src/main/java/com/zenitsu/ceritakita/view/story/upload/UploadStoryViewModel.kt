package com.zenitsu.ceritakita.view.story.upload

import androidx.lifecycle.ViewModel
import com.zenitsu.ceritakita.data.UserRepository
import java.io.File

class UploadStoryViewModel(private val repository: UserRepository): ViewModel() {
    fun uploadImage(file: File, description: String) = repository.uploadStory(file, description)

}