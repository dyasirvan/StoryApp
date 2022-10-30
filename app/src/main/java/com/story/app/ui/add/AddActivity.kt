package com.story.app.ui.add

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.story.app.R
import com.story.app.common.CameraUtility
import com.story.app.common.SharedPreferenceProvider
import com.story.app.core.Resource
import com.story.app.databinding.ActivityAddBinding
import com.story.app.ui.custom.CustomTextInput
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.FileOutputStream

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private val viewModel: AddViewModel by viewModel()
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            binding.ivStory.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.ivStory.setImageURI(selectedImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Add Story"
        }

        with(binding) {
            btCamera.setOnClickListener {
                openCamera()
            }

            btnGallery.setOnClickListener {
                openGallery()
            }

            etDescription.globalChange()
            btnSubmit.setOnClickListener {
                val token = SharedPreferenceProvider(applicationContext).getToken()!!
                val file = getFile as File
                val description = etDescription.text.toString()
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                viewModel.setStoryParam(token, imageMultipart, description)
                viewModel.postStory().observe(this@AddActivity) {
                    when (it) {
                        is Resource.Loading -> {
                            viewLoading.root.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            viewLoading.root.visibility = View.GONE
                            Toast.makeText(
                                this@AddActivity,
                                resources.getString(R.string.success_add_new_story),
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackPressed()
                        }
                        is Resource.Error -> {
                            viewLoading.root.visibility = View.GONE
                            Toast.makeText(this@AddActivity, it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        CameraUtility.createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.story.app",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)

        }
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = CameraUtility.createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun CustomTextInput.globalChange() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                with(binding) {
                    btnSubmit.isEnabled = etDescription.isValid == true && getFile != null
                }
            }

        })
    }
}