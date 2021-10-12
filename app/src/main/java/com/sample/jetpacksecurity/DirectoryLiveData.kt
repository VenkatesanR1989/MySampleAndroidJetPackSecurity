package com.sample.jetpacksecurity

import android.os.FileObserver
import androidx.lifecycle.LiveData
import java.io.File

/**
 * A LiveData which observes and emits values when the list of files in [observationDir] changes.
 */
class DirectoryLiveData(
    private val observationDir: File
) : LiveData<List<FileEntity>>() {

    @Suppress("deprecation")
    private val observer = object : FileObserver(observationDir.path) {
        override fun onEvent(event: Int, path: String?) {
            dispatchFilesChanged()
        }
    }

    private fun dispatchFilesChanged() {
        postValue(observationDir.listFiles()?.map { FileEntity(it.name.urlDecode(), it.path) }
            ?: emptyList())
    }

    override fun onActive() {
        super.onActive()
        dispatchFilesChanged()
        observer.startWatching()
    }

    override fun onInactive() {
        super.onInactive()
        observer.stopWatching()
    }
}