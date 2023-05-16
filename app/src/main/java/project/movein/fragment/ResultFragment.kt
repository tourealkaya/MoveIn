package project.movein.fragment

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import project.movein.R
import project.movein.databinding.FragmentResultBinding
import project.movein.backend.SendReceiveData
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private lateinit var imageView: ImageView
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f
    private var lastX = 0f
    private var lastY = 0f

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
        imageView = view.findViewById(R.id.plan)
        scaleGestureDetector = context?.let { ScaleGestureDetector(it, ScaleListener()) }
        var TAG = "ResultFragement"

       /* imageView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.x
                    lastY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.x - lastX
                    val deltaY = event.y - lastY
                    imageView.translationX += deltaX
                    imageView.translationY += deltaY
                    lastX = event.x
                    lastY = event.y
                }
                else -> return@setOnTouchListener false
            }
            scaleGestureDetector?.onTouchEvent(event)
            true
        }*/
        imageView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.x
                    lastY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.x - lastX
                    val deltaY = event.y - lastY

                    // Obtain the current translation of the image
                    val currentTranslationX = imageView.translationX
                    val currentTranslationY = imageView.translationY

                    // Calculate the updated translation coordinates
                    val newTranslationX = currentTranslationX + deltaX
                    val newTranslationY = currentTranslationY + deltaY

                    imageView.translationX = newTranslationX
                    imageView.translationY = newTranslationY

                    lastX = event.x
                    lastY = event.y
                }
                else -> {}
            }
            scaleGestureDetector?.onTouchEvent(event)
            true
        }







        sendReceiveData.sendData(message,
            onSuccess = { response ->
                Log.d(TAG, "Data sent to server: $message")
                val test ="M.1.35,146,68|1,160,296|2,806,311|U2.1.18,801,515|"

                val nodes = test.split("|")

                // Parcourez chaque nœud et extrayez ses coordonnées x et y
                i = 0
                val nodeWithoutLastChar = nodes.dropLast(2)
                var bitmap = imageView.drawable.toBitmap().copy(Bitmap.Config.ARGB_8888, true)

                for (node in nodeWithoutLastChar) {
                    i++
                    var coordinates = node.split(",")
                    val xd = coordinates[1].toInt()
                    val yd = coordinates[2].toInt()
                    println("Coordonnées du nœud : ($xd, $yd)")
                    coordinates = nodes[i].split(",")
                    val xa = coordinates[1].toInt()
                    val ya = coordinates[2].toInt()
                    println("Coordonnées du nœud : ($xa, $ya)")
                    // Dessiner une ligne sur l'image
                    val canvas = Canvas(bitmap)
                    val w = bitmap.width
                    val h = bitmap.height
                    val paint = Paint().apply {
                        color = Color.RED
                        strokeWidth = 10f
                        style = Paint.Style.STROKE
                    }

                    // Définir les coordonnées de la ligne à dessiner
                    val startX = (xd * w / 833).toFloat()
                    val startY = (yd * h / 899).toFloat()
                    val endX = (xa * w / 833).toFloat()
                    val endY = (ya * h / 899).toFloat()

                    // Dessiner la ligne sur le Canvas
                    canvas.drawLine(startX, startY, endX, endY, paint)

                    // Dessiner le logo depart sur le Canvas
                    /*
                    var logoDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.depart)
                    var logoBitmap = logoDrawable?.toBitmap()
                    var logoWidth = 100
                    var logoHeight = 100
                    var logoLeft = (startX - logoWidth / 2).toInt()
                    var logoTop = (startY - logoHeight).toInt()
                    var logoRect = Rect(logoLeft, logoTop, logoLeft + logoWidth, logoTop + logoHeight)
                    canvas.drawBitmap(logoBitmap!!, null, logoRect, paint)

                    Log.d("TAG", "$message")

                    // Dessiner le logo dest sur le Canvas
                    logoDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.destination)
                    logoBitmap = logoDrawable?.toBitmap()
                    logoWidth = 100
                    logoHeight = 100
                    logoLeft = (endX - logoWidth / 2).toInt()
                    logoTop = (endY - logoHeight).toInt()
                    logoRect = Rect(logoLeft, logoTop, logoLeft + logoWidth, logoTop + logoHeight)
                    canvas.drawBitmap(logoBitmap!!, null, logoRect, paint)
*/

                }
               // imageView.invalidate()
                // Effectuer la modification de l'image sur le thread principal de manière sûre et asynchrone.
                imageView.post { imageView.setImageBitmap(bitmap) }
                //imageView.setImageDrawable(bitmap.toDrawable(resources))
                //imageView.setImageBitmap(bitmap)
                Log.d(TAG, "Received response from server: $response")

            },
            onError = { error ->
                Log.e(TAG, "Error sending data: $error")
            }



        )
    }
    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private val maxScaleFactor = 2.0f
        private val minScaleFactor = 1.0f

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor

            scaleFactor = scaleFactor.coerceIn(minScaleFactor, maxScaleFactor)

            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor
            return true
        }
    }


}
