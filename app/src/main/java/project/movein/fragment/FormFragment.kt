package project.movein.fragment

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.zxing.integration.android.IntentIntegrator
import project.movein.R
import project.movein.backend.SendReceiveData
import project.movein.databinding.FragmentFormBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class FormFragment : Fragment() {
    private val TAG = "FormFragment"
    private lateinit var binding: FragmentFormBinding
    private var roomList: MutableList<String> = mutableListOf()
    private var isValidPosition = false
    private var isValidDestination = false

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
        var inputStream = resources.openRawResource(R.raw.room)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String? = reader.readLine()
        while (line != null) {
            roomList.add(line)
            line = reader.readLine()
        }
        reader.close()

        val args: FormFragmentArgs by navArgs()
        val positionValue = args.position
        val destinationValue = args.destination

        positionValue.let {
            binding.idPosition.setText(it)
        }
        destinationValue.let {
            binding.idDestination.setText(it)
        }

        binding.idPosition.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.idPosition.setBackgroundResource(R.drawable.custom_edittext_border)
                isValidPosition = false
            }
        }

        binding.idDestination.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.idDestination.setBackgroundResource(R.drawable.custom_edittext_border)
                isValidDestination = false
            }
        }


        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, roomList)
        binding.idPosition.setAdapter(adapter)
        binding.idDestination.setAdapter(adapter)


        binding.btnscanner.setOnClickListener {
            val destination=binding.idDestination.text.toString()
            val action= FormFragmentDirections.actionFormFragmentToQrcodeFragment(destination)
            findNavController().navigate(action)
        }

        binding.btndemarrer.setOnClickListener {
            val position = binding.idPosition.text.toString().uppercase()
            val dest = binding.idDestination.text.toString().uppercase()
//            binding.idPosition.setText(position.uppercase())
//            binding.idDestination.setText(dest.uppercase())
            if (position.isNotEmpty() && roomList.contains(position)) {
                isValidPosition = true
            } else {
                isValidPosition = false
                binding.idPosition.setBackgroundResource(R.drawable.custom_edittext_border)
            }

            if (dest.isNotEmpty() && roomList.contains(dest)) {
                isValidDestination = true
            } else {
                isValidDestination = false
                binding.idDestination.setBackgroundResource(R.drawable.custom_edittext_border)
            }

            if (isValidPosition && isValidDestination) {
                val message = (",$position,$dest")
                val action = FormFragmentDirections.actionFormFragmentToResultFragment(message)
                findNavController().navigate(action)
            } else {
                if (!isValidPosition && !isValidDestination) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("Les salles '$dest' et '$position' n'existent pas.")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        })
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else if (!isValidPosition) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("La salle '$position' n'existe pas.")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        })
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else if (!isValidDestination) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("La salle '$dest' n'existe pas.")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        })
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // If QRCode has no data.
            if (result.contents == null) {
                binding.idPosition.setText(result.contents)
            } else {
                binding.idPosition.setText(result.contents)
                isValidPosition = false
                isValidDestination = false
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
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