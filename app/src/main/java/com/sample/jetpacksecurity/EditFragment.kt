package com.sample.jetpacksecurity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.google.android.material.snackbar.Snackbar
import com.sample.jetpacksecurity.databinding.FragmentEditBinding
import java.io.File

class EditFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: FragmentEditBinding
    private val existingFileTitle get() = navArgs<EditFragmentArgs>().value.fileTitle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            // Encrypt and save the file when the user clicks the navigate up icon.
            toolbar.setNavigationOnClickListener {
                encryptFile()
                findNavController().navigateUp()
            }
            toolbar.inflateMenu(R.menu.toolbar_edit_menu)
            toolbar.setOnMenuItemClickListener(this@EditFragment)

            if (existingFileTitle.isNotBlank()) {
                binding.titleEditText.setText(existingFileTitle)
                binding.bodyEditText.setText(decryptFile(existingFileTitle))
            }
        }

        // Encrypt and save the file when the user uses the system back button.
        requireActivity().onBackPressedDispatcher.addCallback(this, true) {
            encryptFile()
            findNavController().popBackStack()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_edit_done -> {
                // Encrypt and save the file when the user clicks the 'done' icon.
                encryptFile()
                findNavController().navigateUp()
                true
            }
            R.id.menu_edit_delete -> {
                deleteFile(existingFileTitle)
                findNavController().navigateUp()
                true
            }
            else -> false
        }
    }

    /**
     * Encrypt a file using the title and body of this fragment's text fields.
     *
     * If an existing file is currently being edited, delete and replace it.
     */
    private fun encryptFile() {
        val title = binding.titleEditText.text.toString()
        val body = binding.bodyEditText.text.toString()

        if (title.isBlank()) return

        try {
            deleteFile(existingFileTitle)
            val encryptedFile = getEncryptedFile(title)
            encryptedFile.openFileOutput().use { output ->
                output.write(body.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showSnackbar(R.string.error_unable_to_save_file)
        }
    }


    /**
     * Decrypt an encrypted file's body and return the plain text String.
     */
    private fun decryptFile(title: String): String {
        val encryptedFile = getEncryptedFile(title)

        try {
            encryptedFile.openFileInput().use { input ->
                return String(input.readBytes(), Charsets.UTF_8)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showSnackbar(R.string.error_unable_to_decrypt)
            return ""
        }
    }

    /**
     * Delete a file from the directory.
     */
    private fun deleteFile(title: String) {
        if (title.isBlank()) return
        val file = File(requireContext().filesDir, title.urlEncode())
        if (file.exists()) file.delete()
    }

    /**
     * Get an [EncryptedFile], used to encrypt and decrypt files using Jetpack Security
     * // Although you can define your own key generation parameter specification, it's
        // recommended that you use the value specified here.
    // Create a file with this name, or replace an entire existing file
    // that has the same name. Note that you cannot append to an existing file,
    // and the file name cannot contain path separators.
     */
    private fun getEncryptedFile(name: String): EncryptedFile {
        return EncryptedFile.Builder(
            File(requireContext().filesDir, name.urlEncode()),
            requireContext(),
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),/*//Algorithm: AES
                                                                * Block Mode: GCM
                                                                * Padding: No Padding
                                                                * Key Size: 256*/
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB//The file content is encrypted using StreamingAead
                // with AES-GCM, with the file name as associated data
        ).build()
    }

//    For use cases requiring additional security, complete the following steps:
//
//    Create a KeyGenParameterSpec.Builder object, passing true into
//    setUserAuthenticationRequired() and a value greater than 0 into
//    setUserAuthenticationValidityDurationSeconds().
//    Prompt the user to enter credentials using createConfirmDeviceCredentialIntent().
//    Learn more about how to request user authentication for key use.
//
//    Note: The Security library doesn't support BiometricPrompt at the cryptographic-operation level.
//    Override onActivityResult() to get the confirmed credential callback.




    private fun showSnackbar(@StringRes messageRes: Int) {
        Snackbar.make(binding.coordinator, messageRes, Snackbar.LENGTH_LONG).show()
    }
}