package project.movein.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import project.movein.R
import project.movein.databinding.FragmentResultBinding
import project.movein.backend.SendReceiveData
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.jsibbold.zoomage.ZoomageView
import project.movein.backend.michem
import project.movein.fragment.ResultFragmentDirections

class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private lateinit var imageView: ZoomageView
    private lateinit var imageList: List<Int>
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f
    private var lastX = 0f
    private var lastY = 0f
    private lateinit var loadingProgressBar: ProgressBar
    private var currentIndex: Int = 0
    private var response: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var message = ""
        message = arguments?.getString("message").toString()
        val sendReceiveData = SendReceiveData()
        var i = 0

        imageView = ZoomageView(requireContext())

        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        imageList = listOf(
            R.drawable.plann,
            R.drawable.secondplan,
            R.drawable.thirdplan)

        imageView.layoutParams = layoutParams
        binding.frame.addView(imageView)

        var TAG = "ResultFragement"
        loadingProgressBar = binding.loadingProgressBar
        loadingProgressBar.visibility = View.GONE
        imageView.visibility = View.GONE

        val imgLoadButton = binding.imgLoadButton
        val imgLoadPrecButton = binding.imgLoadPrecButton

        imgLoadButton.setOnClickListener {
            if (currentIndex < imageList.size - 1) {
                loadingProgressBar.visibility = View.VISIBLE
                currentIndex++
                updateButtonVisibility()
                loadImage()
            }
        }

        val Michem = michem()
        println(Michem.getData(""))

        imgLoadPrecButton.setOnClickListener {
//            loadingProgressBar.visibility = View.VISIBLE
            if (currentIndex > 0) {
                currentIndex--
                updateButtonVisibility()
                loadImage()
            }
        }

        sendReceiveData.sendData(message,

            onSuccess = { response ->
                //loadingProgressBar.visibility = View.VISIBLE

                Log.d(TAG, "Data sent to server: $message")
               // val responsetest = "Michem404"
                if (response == "Michem404") {
                    requireActivity().runOnUiThread {
                        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                        builder.setMessage("Erreur Serveur")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                val action =
                                    ResultFragmentDirections.actionResultFragmentToFormFragment()
                                findNavController().navigate(action)
                            }
                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }
                }
                else {
                    this.response = response
                    loadImage()
                }
            },
            onError = { error ->
                Log.e(TAG, "Error sending data: $error")
            }
        )
    }

    private fun loadImage() {
        val bitmap = BitmapFactory.decodeResource(resources, imageList[currentIndex])
        drawOnImage(bitmap)
    }

    private fun drawOnImage(bitmap: Bitmap) {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint().apply {
            color = Color.parseColor("#45858f")
            strokeWidth = 25f
            style = Paint.Style.FILL
        }

        val nodes = response.split("|")
        val nodeWithoutLastChar = nodes.dropLast(2)

        var lastXa = 0
        var lastYa = 0
        val w = bitmap.width
        val h = bitmap.height
        for (nodeIndex in 0 until nodeWithoutLastChar.size) {
            val coordinates = nodeWithoutLastChar[nodeIndex].split(",")
            val xd = coordinates[1].toInt()
            val yd = coordinates[2].toInt()
            println("Coordonnées du nœud : ($xd, $yd)")
            val nodeCoordinates = nodes[nodeIndex + 1].split(",")
            val xa = nodeCoordinates[1].toInt()
            val ya = nodeCoordinates[2].toInt()
            println("Coordonnées du nœud : ($xa, $ya)")

            // Dessiner une ligne sur l'image
            val startX = (xd * w / 1353).toFloat()
            val startY = (yd * h / 1003).toFloat()
            val endX = (xa * w / 1353).toFloat()
            val endY = (ya * h / 1003).toFloat()

            if (nodeIndex == 0) {
                val squareSize = 50
                val squareLeft = (startX - squareSize / 2).toInt()
                val squareTop = (startY - squareSize / 2).toInt()
                val squareRight = (startX + squareSize / 2).toInt()
                val squareBottom = (startY + squareSize / 2).toInt()
                val squareRect = Rect(squareLeft, squareTop, squareRight, squareBottom)
                canvas.drawRect(squareRect, paint)
            }

            canvas.drawLine(startX, startY, endX, endY, paint)
            val lastCoordinates = nodes[nodeIndex + 1].split(",")
            lastXa = lastCoordinates[1].toInt()
            lastYa = lastCoordinates[2].toInt()
        }

        val logoDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.destination)
        val logoBitmap = logoDrawable?.toBitmap()
        val logoWidth = 100
        val logoHeight = 100
        val logoLeft = (lastXa * w / 1353 - logoWidth / 2).toInt()
        val logoTop = (lastYa * h / 1003 - logoHeight).toInt()
        val logoRect = Rect(logoLeft, logoTop, logoLeft + logoWidth, logoTop + logoHeight)
        canvas.drawBitmap(logoBitmap!!, null, logoRect, paint)

        imageView.post {
            val imageDrawable = mutableBitmap.toDrawable(resources)
            imageDrawable.callback = imageView
            imageView.setImageDrawable(imageDrawable)
            updateButtonVisibility()
        }

        imageView.post {
            loadingProgressBar.visibility = View.GONE
            imageView.visibility = View.VISIBLE
        }
    }

    private fun updateButtonVisibility() {
        val imgLoadButton = binding.imgLoadButton
        val imgLoadPrecButton = binding.imgLoadPrecButton

        imgLoadButton.visibility = if (currentIndex < imageList.size - 1) View.VISIBLE else View.GONE

        imgLoadPrecButton.visibility = if (currentIndex > 0) View.VISIBLE else View.GONE
    }
}
