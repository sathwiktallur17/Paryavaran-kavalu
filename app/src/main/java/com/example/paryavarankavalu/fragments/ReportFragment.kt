package com.example.paryavarankavalu.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.paryavarankavalu.R
import com.example.paryavarankavalu.databinding.FragmentReportBinding
import com.example.paryavarankavalu.models.Report
import com.example.paryavarankavalu.utils.ImageUtils
import com.example.paryavarankavalu.utils.LocationUtils
import com.example.paryavarankavalu.utils.PermissionUtils
import com.example.paryavarankavalu.viewmodels.ReportViewModel
import com.example.paryavarankavalu.viewmodels.ReportViewModelFactory
import kotlinx.coroutines.launch
import java.io.File

class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationUtils: LocationUtils
    private lateinit var imageUtils: ImageUtils
    private var photoUri: Uri? = null
    private var compressedPhotoPath: String? = null

    private val viewModel: ReportViewModel by viewModels {
        ReportViewModelFactory(requireActivity().application)
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            // Location granted
        } else {
            Toast.makeText(requireContext(), getString(R.string.location_permission_required), Toast.LENGTH_SHORT).show()
        }
        if (permissions[Manifest.permission.CAMERA] == true) {
            // Camera granted
        } else {
            Toast.makeText(requireContext(), getString(R.string.camera_permission_required), Toast.LENGTH_SHORT).show()
        }
        if (permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true &&
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
            // Storage granted
        } else {
            Toast.makeText(requireContext(), getString(R.string.storage_permission_required), Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            photoUri?.let { uri ->
                compressedPhotoPath = imageUtils.compressImage(uri)
                binding.photoPreview.setImageURI(uri)
                binding.photoPreview.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationUtils = LocationUtils(requireContext())
        imageUtils = ImageUtils(requireContext())

        setupWasteTypeSpinner()
        setupButtons()
    }

    private fun setupWasteTypeSpinner() {
        val wasteTypes = arrayOf("Plastic", "Organic", "Electronic", "Construction", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, wasteTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.wasteTypeSpinner.adapter = adapter
    }

    private fun setupButtons() {
        binding.takePhotoButton.setOnClickListener {
            if (PermissionUtils.hasCameraPermission(requireContext()) &&
                PermissionUtils.hasStoragePermission(requireContext())) {
                takePhoto()
            } else {
                requestPermissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            }
        }

        binding.submitButton.setOnClickListener {
            submitReport()
        }
    }

    private fun takePhoto() {
        val photoFile = File(requireContext().cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photoFile
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        takePictureLauncher.launch(intent)
    }

    private fun submitReport() {
        lifecycleScope.launch {
            val location = locationUtils.getCurrentLocation()
            if (location == null) {
                Toast.makeText(requireContext(), "Unable to get location. Please check permissions.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val wasteType = binding.wasteTypeSpinner.selectedItem.toString()
            if (compressedPhotoPath == null) {
                Toast.makeText(requireContext(), "Please take a photo.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val report = Report(
                latitude = location.latitude,
                longitude = location.longitude,
                wasteType = wasteType,
                photoPath = compressedPhotoPath!!
            )

            viewModel.insertReport(report)
            Toast.makeText(requireContext(), "Report submitted!", Toast.LENGTH_SHORT).show()

            // Reset form
            binding.photoPreview.visibility = View.GONE
            compressedPhotoPath = null
            photoUri = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}