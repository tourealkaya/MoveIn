package project.movein.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.*
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.fragment.app.Fragment
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import project.movein.databinding.FragmentQrcodeBinding
import project.movein.viewmodel.QrcodeViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QrcodeFragment : Fragment() {
    private lateinit var binding: FragmentQrcodeBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var analyzer: MyImageAnalyzer
    private val qrcodeViewModel: QrcodeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQrcodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideStatusBars()

        analyzer = MyImageAnalyzer(childFragmentManager, qrcodeViewModel)
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(
            Runnable { bindPreview(cameraProviderFuture.get()) },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    private fun hideStatusBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder()
            .build()
        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalysis.setAnalyzer(cameraExecutor, analyzer)
        //val qalwa =  imageAnalysis.setAnalyzer(cameraExecutor, analyzer)
        //Log.d("TTT", "Data sent to server:$qalwa")
        cameraProvider.bindToLifecycle(
            this as LifecycleOwner,
            cameraSelector,
            imageAnalysis,
            preview
        )
    }


    class MyImageAnalyzer(childFragmentManager: FragmentManager,private val qrcodeViewModel: QrcodeViewModel) : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            scanBarcode(imageProxy)
        }

        @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
        private fun scanBarcode(imageProxy: ImageProxy) {
            imageProxy.image?.let { image ->
                val inputImage =
                    InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
                val scanner = BarcodeScanning.getClient()
                scanner.process(inputImage)
                    .addOnCompleteListener {
                        imageProxy.close()
                        if (it.isSuccessful) {
                            readBarcodeData(it.result as List<Barcode>)
                        } else {
                            it.exception?.printStackTrace()
                        }
                    }
            }
        }
        var text= ""
        private fun readBarcodeData(barcodes: List<Barcode>) {

            for (barcode in barcodes) {
                text = barcode.rawValue.toString()
                qrcodeViewModel.setBarcodeText(text)
            }
           // qrcodeViewModel.sendData(text)
        }
        //qrcodeViewModel.sendData(text)
    }
}

