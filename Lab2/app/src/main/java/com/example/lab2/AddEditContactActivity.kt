package com.example.lab2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab2.databinding.ActivityAddEditContactBinding
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class AddEditContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditContactBinding
    private var currentContact: ContactModel? = null
    private var imageUri: Uri? = null
    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        currentContact = intent.getSerializableExtra("contact") as? ContactModel

        setupUI()
        if (currentContact != null) {
            loadContactData()
        }
    }

    private fun setupUI() {
        binding.apply {
            if (currentContact != null) {
                toolbarTitle.text = getString(R.string.edit_contact)
                deleteBtn.text = getString(R.string.delete_contact)
            } else {
                toolbarTitle.text = getString(R.string.add_contact)
                deleteBtn.text = getString(R.string.cancel)
            }

            addPhotoBtn.setOnClickListener {
                openImagePicker()
            }

            saveBtn.setOnClickListener {
                saveContact()
            }

            deleteBtn.setOnClickListener {
                if (currentContact != null) {
                    deleteCurrentContact()
                } else {
                    finish()
                }
            }
        }
    }

    private fun loadContactData() {
        currentContact?.let { contact ->
            binding.apply {
                nameInput.setText(contact.name)
                phoneInput.setText(contact.phone)
                imagePath = contact.imagePath

                if (!contact.imagePath.isNullOrEmpty()) {
                    val file = File(contact.imagePath)
                    if (file.exists()) {
                        imageUri = Uri.fromFile(file)
                        profileImage.setImageURI(imageUri)
                    }
                }
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 200)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            if (selectedImageUri != null) {
                imageUri = selectedImageUri
                binding.profileImage.setImageURI(imageUri)
                imagePath = saveImageToAppDirectory(selectedImageUri)
            }
        }
    }

    private fun saveImageToAppDirectory(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val imagesDir = File(filesDir, "contact_images")
            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }

            val fileName = "${UUID.randomUUID()}.jpg"
            val imageFile = File(imagesDir, fileName)

            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            outputStream.close()
            inputStream.close()

            imageFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveContact() {
        val name = binding.nameInput.text.toString().trim()
        val phone = binding.phoneInput.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter contact name", Toast.LENGTH_SHORT).show()
            return
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show()
            return
        }

        val contact = if (currentContact != null) {
            currentContact!!.apply {
                this.name = name
                this.phone = phone
                this.imagePath = imagePath ?: this.imagePath
            }
        } else {
            ContactModel(name = name, phone = phone, imagePath = imagePath)
        }

        val intent = Intent()
        intent.putExtra("contact", contact)
        intent.putExtra("isEdit", currentContact != null)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun deleteCurrentContact() {
        if (currentContact != null) {
            val intent = Intent()
            intent.putExtra("contact", currentContact)
            intent.putExtra("isDelete", true)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}



