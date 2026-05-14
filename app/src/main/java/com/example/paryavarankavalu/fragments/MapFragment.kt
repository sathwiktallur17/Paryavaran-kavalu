package com.example.paryavarankavalu.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.paryavarankavalu.R
import com.example.paryavarankavalu.databinding.FragmentMapBinding
import com.example.paryavarankavalu.models.Report
import com.example.paryavarankavalu.utils.PermissionUtils
import com.example.paryavarankavalu.viewmodels.ReportViewModel
import com.example.paryavarankavalu.viewmodels.ReportViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.coroutines.launch

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private val markers = mutableMapOf<Long, com.google.android.gms.maps.model.Marker>()
    private var selectedReport: Report? = null

    private val viewModel: ReportViewModel by viewModels {
        ReportViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.markCleanedFab.setOnClickListener {
            selectedReport?.let { report ->
                if (report.status == "Pending") {
                    val updatedReport = report.copy(status = "Cleaned")
                    viewModel.updateReport(updatedReport)
                    Toast.makeText(requireContext(), "Marked as cleaned!", Toast.LENGTH_SHORT).show()
                    selectedReport = null
                }
            } ?: Toast.makeText(requireContext(), "Select a pending report first", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setOnMarkerClickListener(this)

        if (PermissionUtils.hasLocationPermission(requireContext())) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.isMyLocationEnabled = true
            }
        }

        // Default location (e.g., India)
        val defaultLocation = LatLng(20.5937, 78.9629)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 5f))

        observeReports()
    }

    override fun onMarkerClick(marker: com.google.android.gms.maps.model.Marker): Boolean {
        selectedReport = marker.tag as? Report
        return false
    }

    private fun observeReports() {
        lifecycleScope.launch {
            viewModel.allReports.collect { reports ->
                updateMarkers(reports)
            }
        }
    }

    private fun updateMarkers(reports: List<Report>) {
        // Clear existing markers
        markers.values.forEach { it.remove() }
        markers.clear()

        reports.forEach { report ->
            val position = LatLng(report.latitude, report.longitude)
            val markerColor = if (report.status == "Cleaned") BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_RED
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title("Waste: ${report.wasteType}")
                    .snippet("Status: ${report.status}")
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
            )
            marker?.let { markers[report.id] = it }
            marker?.tag = report
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}