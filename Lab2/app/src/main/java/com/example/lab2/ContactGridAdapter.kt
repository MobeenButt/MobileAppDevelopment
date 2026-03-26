package com.example.lab2

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.databinding.ContactListItemGridBinding
import java.io.File

class ContactGridAdapter(
    private val onEditClick: (ContactModel) -> Unit,
    private val onDeleteClick: (ContactModel) -> Unit
) : ListAdapter<ContactModel, ContactGridAdapter.ContactGridViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactGridViewHolder {
        val binding = ContactListItemGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactGridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactGridViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
    }

    inner class ContactGridViewHolder(private val binding: ContactListItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: ContactModel) {
            binding.apply {
                contactGridName.text = contact.name
                contactGridPhone.text = contact.phone

                // Load image if available
                if (!contact.imagePath.isNullOrEmpty() && File(contact.imagePath).exists()) {
                    contactGridImage.setImageURI(Uri.fromFile(File(contact.imagePath)))
                } else {
                    contactGridImage.setImageResource(R.drawable.ic_contact_placeholder)
                }

                editButtonGrid.setOnClickListener {
                    onEditClick(contact)
                }

                deleteButtonGrid.setOnClickListener {
                    onDeleteClick(contact)
                }
            }
        }
    }

    private class ContactDiffCallback : DiffUtil.ItemCallback<ContactModel>() {
        override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
            return oldItem == newItem
        }
    }
}



