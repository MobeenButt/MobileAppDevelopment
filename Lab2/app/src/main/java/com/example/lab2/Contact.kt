package com.example.lab2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.databinding.ActivityContactBinding
import com.example.lab2.databinding.ContactItemBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.Serializable
import java.util.UUID

data class ContactModel(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var phone: String,
    var imagePath: String? = null
) : Serializable

class Contact : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding
    private val contactList = mutableListOf<ContactModel>()
    private val adapter by lazy { ContactAdapter(::onEditClick, ::onDeleteClick) }
    private val gson = Gson()
    private val prefs by lazy { getSharedPreferences("contacts_app", MODE_PRIVATE) }
    
    private var editingContact: ContactModel? = null
    private var currentImagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupClickListeners()
        loadContacts()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.addEditContainer.visibility == View.VISIBLE) {
                    showList()
                } else {
                    finish()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.fabAdd.setOnClickListener { showForm(null) }
        binding.cancelBtn.setOnClickListener { showList() }
        binding.saveBtn.setOnClickListener { saveContact() }
        binding.addPhotoBtn.setOnClickListener { openImagePicker() }
    }

    private fun showForm(contact: ContactModel?) {
        editingContact = contact
        binding.listContainer.visibility = View.GONE
        binding.fabAdd.visibility = View.GONE
        binding.addEditContainer.visibility = View.VISIBLE
        
        binding.formTitle.text = if (contact == null) "Add Contact" else "Edit Contact"
        binding.nameInput.setText(contact?.name ?: "")
        binding.phoneInput.setText(contact?.phone ?: "")
        currentImagePath = contact?.imagePath
        
        updateProfileImage(currentImagePath)
    }

    private fun showList() {
        binding.listContainer.visibility = View.VISIBLE
        binding.fabAdd.visibility = View.VISIBLE
        binding.addEditContainer.visibility = View.GONE
        editingContact = null
    }

    private fun updateProfileImage(path: String?) {
        if (!path.isNullOrEmpty() && File(path).exists()) {
            binding.profileImage.setImageURI(Uri.fromFile(File(path)))
        } else {
            binding.profileImage.setImageResource(R.drawable.ic_contact_placeholder)
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                binding.profileImage.setImageURI(uri)
                currentImagePath = saveImageLocally(uri)
            }
        }
    }

    private fun saveImageLocally(uri: Uri): String? {
        return try {
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
            val file = File(filesDir, "contact_${UUID.randomUUID()}.jpg")
            FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
            file.absolutePath
        } catch (e: Exception) { null }
    }

    private fun saveContact() {
        val name = binding.nameInput.text.toString().trim()
        val phone = binding.phoneInput.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (editingContact == null) {
            contactList.add(ContactModel(name = name, phone = phone, imagePath = currentImagePath))
        } else {
            val index = contactList.indexOfFirst { it.id == editingContact?.id }
            if (index != -1) {
                contactList[index] = editingContact!!.copy(name = name, phone = phone, imagePath = currentImagePath)
            }
        }
        
        persistAndRefresh()
        showList()
    }

    private fun onEditClick(contact: ContactModel) = showForm(contact)

    private fun onDeleteClick(contact: ContactModel) {
        contactList.remove(contact)
        persistAndRefresh()
    }

    private fun loadContacts() {
        val json = prefs.getString("contacts_list", null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<ContactModel>>() {}.type
            val saved: List<ContactModel> = gson.fromJson(json, type)
            contactList.clear()
            contactList.addAll(saved)
        }
        refreshList()
    }

    private fun persistAndRefresh() {
        prefs.edit().putString("contacts_list", gson.toJson(contactList)).apply()
        refreshList()
    }

    private fun refreshList() {
        adapter.submitList(contactList.toList().sortedBy { it.name })
        binding.emptyStateText.visibility = if (contactList.isEmpty()) View.VISIBLE else View.GONE
    }

    class ContactAdapter(
        private val onEdit: (ContactModel) -> Unit,
        private val onDelete: (ContactModel) -> Unit
    ) : ListAdapter<ContactModel, ContactAdapter.VH>(Diff) {
        
        inner class VH(val b: ContactItemBinding) : RecyclerView.ViewHolder(b.root)
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
            ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = getItem(position)
            holder.b.apply {
                contactName.text = item.name
                contactPhone.text = item.phone
                if (!item.imagePath.isNullOrEmpty() && File(item.imagePath!!).exists()) {
                    contactImage.setImageURI(Uri.fromFile(File(item.imagePath!!)))
                } else {
                    contactImage.setImageResource(R.drawable.ic_contact_placeholder)
                }
                editButton.setOnClickListener { onEdit(item) }
                deleteButton.setOnClickListener { onDelete(item) }
            }
        }

        object Diff : DiffUtil.ItemCallback<ContactModel>() {
            override fun areItemsTheSame(o: ContactModel, n: ContactModel) = o.id == n.id
            override fun areContentsTheSame(o: ContactModel, n: ContactModel) = o == n
        }
    }
}
