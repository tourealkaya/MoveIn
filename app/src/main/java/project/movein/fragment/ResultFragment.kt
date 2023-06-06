package project.movein.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jsibbold.zoomage.ZoomageView
import project.movein.backend.michem
import project.movein.fragment.ResultFragmentDirections
import project.movein.viewmodel.michemResult
import project.movein.viewmodel.michemViewmodel
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

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
    private lateinit var mutableBitmap : Bitmap
    private lateinit var backmutableBitmap : Bitmap
    var ifZoom : Boolean = true
    var z : Boolean = true
    var degzoum : Int = 0
    lateinit var options : BitmapFactory.Options

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var message = ""
        message = arguments?.getString("message").toString()
        val sendReceiveData = SendReceiveData()
        var i = 0
        val myviewmodel : michemViewmodel by viewModels ()

        val list_message = message.split(",")
        println(list_message)
        myviewmodel.calculer(resources.openRawResource(R.raw.plan),list_message[0],list_message[1])
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


        scaleGestureDetector = ScaleGestureDetector(requireContext(), object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val scaleFactor = detector.scaleFactor
                val matrixValues = FloatArray(9)
                imageView.imageMatrix.getValues(matrixValues)
                var clampedScale = matrixValues[Matrix.MSCALE_X]
                println(clampedScale)

                if (scaleFactor > 1) {
                    degzoum++

                    if (clampedScale >= 0.5f && ifZoom) {
                        println("Dessiner")
                        ifZoom = false
                        onZoomInDetected(resources.openRawResource(R.raw.plan), false)
                    }
                } else {
                    degzoum--
                    if(clampedScale < 0.5f && degzoum % 5 == 0 ){
                        degzoum = 0
                        onZoomInDetected(resources.openRawResource(R.raw.plan), true)
                        ifZoom = true

                    }
                }



                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                return true
            }
            override fun onScaleEnd(detector: ScaleGestureDetector) {
                // Réinitialiser le niveau de zoom lorsque le geste de zoom est terminé
                println(degzoum)


            }
        })



        imageView.setOnTouchListener { _, event ->
            scaleGestureDetector!!.onTouchEvent(event)
            false
        }



        imgLoadPrecButton.setOnClickListener {
//            loadingProgressBar.visibility = View.VISIBLE
            if (currentIndex > 0) {
                currentIndex--
                updateButtonVisibility()
                loadImage()
            }
        }

        myviewmodel.Result.observe( viewLifecycleOwner ) { value ->
            when ( value ) {
                is michemResult.EmptyResult ->
                    requireActivity().runOnUiThread {
                        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                        builder.setMessage("Erreur !!")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                val action =
                                    ResultFragmentDirections.actionResultFragmentToFormFragment()
                                findNavController().navigate(action)
                            }
                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }
                is michemResult.Calc -> {
                    this.response = value.result
                    println(value.result)
                    loadImage()

                }
            }
        }





    }

    private fun onZoomInDetected(inputStream: InputStream ,clear: Boolean = false) {
        if(clear){
            loadImage()
        }else {
            val paint: Paint = Paint().apply {
                color = Color.BLACK
                textSize = 30f
                typeface = Typeface.DEFAULT_BOLD
            }
            val w = mutableBitmap.width
            val h = mutableBitmap.height
            backmutableBitmap = mutableBitmap
            val canvas = Canvas(mutableBitmap)

            val reader = BufferedReader(InputStreamReader(inputStream))
            while (true) {
                val line = reader.readLine()?.trim() ?: break
                if (line == "------------------") {
                    break
                }
                val data = line.split(",")
                canvas.drawText(
                    data[0],
                    (data[1].toFloat() * w / options.outWidth) - 40,
                    data[2].toFloat() * h / options.outHeight,
                    paint
                )
            }

            imageView.setImageBitmap(mutableBitmap)


        }

    }

    private fun loadImage() {
        val bitmap = BitmapFactory.decodeResource(resources, imageList[currentIndex])
        options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(resources, imageList[currentIndex], options)
        drawOnImage(bitmap,options.outWidth,options.outHeight)
    }



    private fun drawOnImage(bitmap: Bitmap,width : Int,height : Int) {

        mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint().apply {
            color = Color.parseColor("#c80a50")
            strokeWidth = 30f
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
            val startX = (xd * w / width).toFloat()
            val startY = (yd * h / height).toFloat()
            val endX = (xa * w / width).toFloat()
            val endY = (ya * h / height).toFloat()


            paint.color = Color.parseColor("#fab400")
            if (nodeIndex == 0) {
                val squareSize = 50
                val squareLeft = (startX - squareSize / 2).toInt()
                val squareTop = (startY - squareSize / 2).toInt()
                val squareRight = (startX + squareSize / 2).toInt()
                val squareBottom = (startY + squareSize / 2).toInt()
                val squareRect = Rect(squareLeft, squareTop, squareRight, squareBottom)
                paint.color = Color.parseColor("#ec1c24")
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
        val logoLeft = (lastXa * w / width - logoWidth / 2)
        val logoTop = (lastYa * h / height - logoHeight) - 5
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
