package project.movein.fragment

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import project.movein.R
import project.movein.databinding.FragmentFormBinding
import project.movein.backend.SendReceiveData

class FormFragment : Fragment() {
    private val TAG = "FormFragment"
    var delim = ","
    private lateinit var binding: FragmentFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val sendReceiveData = SendReceiveData()
     //   var qrval = ""



        binding.idDestination.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.idDestination.setBackgroundColor(Color.WHITE)
            }
        }

        binding.idPosition.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.idPosition.setBackgroundColor(Color.WHITE)
            }
        }

        binding.btnscanner.setOnClickListener {
            val dest = binding.idDestination.text.toString()
            if (dest.isNotEmpty()) {
                findNavController().navigate(R.id.action_formFragment_to_qrcodeFragment)

            } else {
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Veuillez entrer une destination.")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    })
                val dialog: AlertDialog = builder.create()
                dialog.show()
                binding.idDestination.setBackgroundColor(Color.RED)
            }
        }

        binding.btndemarrer.setOnClickListener {
            val position = binding.idPosition.text.toString()
            val dest = binding.idDestination.text.toString()
            if (position.isNotEmpty() && dest.isNotEmpty()) {

                val message = ",$position,$dest"
                val bundle = Bundle()
                val formFragment = FormFragment()


              /*  bundle.putString("message_key", message)

// Créer une action de navigation qui inclut le Bundle
               // val action = R.id.action_formFragment_to_resultFragment
                formFragment.arguments = bundle

// Naviguer vers le fragment de destination avec l'action
                findNavController().navigate(R.id.action_formFragment_to_resultFragment)*/
                // Créer une instance de l'action menant à la destination de destination
                val action = FormFragmentDirections.actionFormFragmentToResultFragment(message)

// Naviguer vers la destination de destination en utilisant l'instance de l'action


               /* sendReceiveData.sendData(message,
                    onSuccess = { response ->
                        Log.d(TAG, "Data sent to server: $message")

                        val nodes = response.split("|")

                        // Parcourez chaque nœud et extrayez ses coordonnées x et y
                        for (node in nodes) {
                            val coordinates = node.split(",")
                            val x = coordinates[1].toInt()
                            val y = coordinates[2].toInt()
                            println("Coordonnées du nœud : ($x, $y)")
                        }
                        Log.d(TAG, "Received response from server: $response")
                    },
                    onError = { error ->
                        Log.e(TAG, "Error sending data: $error")
                    }
                )*/
                findNavController().navigate(action)
               // findNavController().navigate(R.id.action_formFragment_to_resultFragment)
            }
            else {
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Veuillez remplir les champs manquants.")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    })
                val dialog: AlertDialog = builder.create()
                dialog.show()
                if (position.isEmpty()) {
                    binding.idPosition.setBackgroundColor(Color.RED)
                }
                if (dest.isEmpty()) {
                    binding.idDestination.setBackgroundColor(Color.RED)
                }
            }

        }

        val desthelpButton: Button = view.findViewById(R.id.dest_btn_help)
        desthelpButton.setOnClickListener {
            onHelpClick(view)
        }
        val positionhelpButton: Button = view.findViewById(R.id.position_btn_help)
        positionhelpButton.setOnClickListener {
            onHelpClick(view)
        }
    }


    fun onHelpClick(view: View) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Aide")
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
            })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}