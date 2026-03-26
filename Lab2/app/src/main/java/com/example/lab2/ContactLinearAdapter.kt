package com.example.lab2

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.databinding.ContactListItemLinearBinding
import java.io.File

class ContactLinearAdapter(
    private val onEditClick: (ContactModel) -> Unit,
    private val onDeleteClick: (ContactModel) -> Unit
) : ListAdapter<ContactModel, ContactLinearAdapter.ContactViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ContactListItemLinearBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
    }

    inner class ContactViewHolder(private val binding: ContactListItemLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: ContactModel) {
            binding.apply {
                contactName.text = contact.name
                contactPhone.text = contact.phone

                // Load image if available
                if (!contact.imagePath.isNullOrEmpty() && File(contact.imagePath).exists()) {
                    contactImage.setImageURI(Uri.fromFile(File(contact.imagePath)))
                } else {
                    contactImage.setImageResource(R.drawable.ic_contact_placeholder)
                }

                editButton.setOnClickListener {
                    onEditClick(contact)
                }

                deleteButton.setOnClickListener {
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



