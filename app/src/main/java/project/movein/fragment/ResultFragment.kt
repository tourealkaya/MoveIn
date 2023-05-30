package project.movein.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.*
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
import project.movein.fragment.ResultFragmentDirections

class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private lateinit var imageView: ZoomageView
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f
    private var lastX = 0f
    private var lastY = 0f
    private lateinit var loadingProgressBar: ProgressBar


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
        //val mError = arguments?.getString("mError").toString()
        val sendReceiveData = SendReceiveData()
        var i = 0

        imageView = ZoomageView(requireContext())

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        imageView.layoutParams = layoutParams

        // Chargement dynamique de l'image
        val drawableResourceId = R.drawable.plann
        val imageDrawable = ContextCompat.getDrawable(requireContext(), drawableResourceId)
        imageView.setImageDrawable(imageDrawable)
        binding.root.addView(imageView)

        var TAG = "ResultFragement"
        loadingProgressBar = binding.loadingProgressBar
        loadingProgressBar.visibility = View.GONE
        loadingProgressBar.visibility = View.VISIBLE
        imageView.visibility = View.GONE

        sendReceiveData.sendData(message,
            onSuccess = { response ->
                Log.d(TAG, "Data sent to server: $message")
               //val responser = "Michem404"
               if(response == "Michem404"){

                  // runOnUiThread pour exécuter le code qui crée et affiche le pop-up sur le thread principal

                   requireActivity().runOnUiThread {
                       val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                       builder.setMessage("Erreur Serveur")
                           .setPositiveButton("OK") { dialog, _ ->
                               dialog.dismiss()
                               val action = ResultFragmentDirections.actionResultFragmentToFormFragment()
                               findNavController().navigate(action)
                           }
                       val dialog: AlertDialog = builder.create()
                       dialog.show()

                   }

                }
                else {
                    val nodes = response.split("|")

                    // Parcourez chaque nœud et extrayez ses coordonnées x et y
                    i = 0
                    val nodeWithoutLastChar = nodes.dropLast(2)
                    var bitmap = imageView.drawable.toBitmap().copy(Bitmap.Config.ARGB_8888, true)
                    var lastXa = 0
                    var lastYa = 0
                    var canvas = Canvas(bitmap)
                    var paint = Paint().apply {
                        color = Color.parseColor("#45858f")
                        strokeWidth = 25f
                        style = Paint.Style.FILL
                    }
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
                        canvas = Canvas(bitmap)
                        val w = bitmap.width
                        val h = bitmap.height


                        // Définir les coordonnées de la ligne à dessiner
                        val startX = (xd * w / 1353).toFloat()
                        val startY = (yd * h / 1003).toFloat()
                        val endX = (xa * w / 1353).toFloat()
                        val endY = (ya * h / 1003).toFloat()

                        // Dessiner le logo depart sur le Canvas
                        if (i == 1) {
                            val squareSize = 50
                            val squareLeft = (startX - squareSize / 2).toInt()
                            val squareTop = (startY - squareSize / 2).toInt()
                            val squareRight = (startX + squareSize / 2).toInt()
                            val squareBottom = (startY + squareSize / 2).toInt()
                            val squareRect = Rect(squareLeft, squareTop, squareRight, squareBottom)
                            canvas.drawRect(squareRect, paint)

                        }

                        // Dessiner la ligne sur le Canvas
                        canvas.drawLine(startX, startY, endX, endY, paint)
                        coordinates = nodes[i].split(",")
                        lastXa = coordinates[1].toInt()
                        lastYa = coordinates[2].toInt()
                    }
                    val logoDrawable =
                        ContextCompat.getDrawable(requireContext(), R.drawable.destination)
                    val logoBitmap = logoDrawable?.toBitmap()
                    val logoWidth = 100
                    val logoHeight = 100
                    val w = bitmap.width
                    val h = bitmap.height
                    val logoLeft = (lastXa * w / 1353 - logoWidth / 2).toInt()
                    val logoTop = (lastYa * h / 1003 - logoHeight).toInt()
                    val logoRect =
                        Rect(logoLeft, logoTop, logoLeft + logoWidth, logoTop + logoHeight)
                    canvas.drawBitmap(logoBitmap!!, null, logoRect, paint)


                    // imageView.invalidate()
                    // Effectuer la modification de l'image sur le thread principal de manière sûre et asynchrone.
                imageView.post {
                    loadingProgressBar.visibility = View.GONE
                    imageView.setImageBitmap(bitmap)
                    imageView.visibility = View.VISIBLE
                    }

                }
                //  Log.d(TAG, "Received response from server: $response")

            },
            onError = { error ->
                Log.e(TAG, "Error sending data: $error")
            }

        )
    }



}