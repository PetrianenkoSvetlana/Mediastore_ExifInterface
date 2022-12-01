package com.example.mediastore_exifinterface

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mediastore_exifinterface.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageSelectionBtn.setOnClickListener {
            pickImage()
        }
//
//        binding.editBtn.setOnClickListener {
//            findNavController().navigate(FirstFragment.action_FirstFragment_to_SecondFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun pickImage() {
        // Показываем все программы для запуска
//        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*";
        requestUri.launch(intent)
    }

    private var requestUri = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val contentResolver = requireContext().contentResolver

        if (result != null && result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                intent.data?.let { fileUri ->
                    binding.constraintLayout.visibility = View.VISIBLE
                    binding.imageView.setImageURI(fileUri)
                    contentResolver.openInputStream(fileUri)?.use { stream ->
                        val exif = ExifInterface(stream)
                        binding.apply {
                            creationDateTv.text = exif.getAttribute(ExifInterface.TAG_DATETIME)
                            latitudeTv.text = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) + ' ' + exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
                            longitudeTv.text = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) + ' ' + exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)
                            creationDeviceTv.text = exif.getAttribute(ExifInterface.TAG_MAKE)
                            modelCreationDeviceTv.text = exif.getAttribute(ExifInterface.TAG_MODEL)
                        }
                    }
                }
            }
        }
    }
}