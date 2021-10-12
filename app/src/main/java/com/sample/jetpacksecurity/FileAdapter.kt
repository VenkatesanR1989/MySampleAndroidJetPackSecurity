package com.sample.jetpacksecurity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.jetpacksecurity.databinding.FileItemLayoutBinding


/**
 * A simple list adapter which displays a list of [FileEntity]s.
 */
class FileAdapter(
    private val listener: FileAdapterListener
) : ListAdapter<FileEntity, FileAdapter.FilesObjViewHolder>(FileEntityDiff) {

    interface FileAdapterListener {
        fun onFileClicked(file: FileEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesObjViewHolder {
        return FilesObjViewHolder(
            FileItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: FilesObjViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FilesObjViewHolder(
        private val binding: FileItemLayoutBinding,
        private val listener: FileAdapterListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(file: FileEntity) {
            binding.apply {
                fileEntity = file
                handler = listener
                executePendingBindings()
            }
        }
    }

}