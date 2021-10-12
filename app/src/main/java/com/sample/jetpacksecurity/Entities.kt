package com.sample.jetpacksecurity

import androidx.recyclerview.widget.DiffUtil
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * A data object which represents a locally encrypted file.
 */
data class FileEntity(
    val title: String,
    val path: String
)

object FileEntityDiff : DiffUtil.ItemCallback<FileEntity>() {
    override fun areItemsTheSame(oldItem: FileEntity, newItem: FileEntity) = oldItem == newItem
    override fun areContentsTheSame(oldItem: FileEntity, newItem: FileEntity) = oldItem == newItem
}

/**
 * Extension method to decode a URL encoded a string.
 */
fun String.urlDecode():String = URLDecoder.decode(this, "UTF-8")

/**
 * Extension method to URL encode a string.
 */
fun String.urlEncode():String = URLEncoder.encode(this, "UTF-8")