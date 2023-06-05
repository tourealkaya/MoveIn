package project.movein.fragment
import android.content.DialogInterface
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import project.movein.R
import project.movein.databinding.FragmentFormBinding
import project.movein.backend.SendReceiveData
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


        val sendReceiveData = SendReceiveData()

        var inputStream = resources.openRawResource(R.raw.room)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String? = reader.readLine()
        while (line != null) {
            roomList.add(line)
            line = reader.readLine()
        }
        reader.close()
        val args : FormFragmentArgs by navArgs()
        var mError = args.mError
        val positionValue = args.position
        val destinationValue = args.destination
        positionValue.let {
            binding.idPosition.setText(it)
        }
        destinationValue.let {
            binding.idDestination.setText(it)
        }

        var position = positionValue
        var dest = destinationValue


        binding.idDestination.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (roomList.contains(binding.idDestination.text.toString())) {
                    binding.idDestination.setBackgroundResource(R.drawable.custom_edittext_green_border)
                }
                else{
                    binding.idDestination.setBackgroundResource(R.drawable.custom_edittext_border)
                    isValidDestination = false
                }
            }
        })

        binding.idPosition.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (roomList.contains(binding.idPosition.text.toString())) {
                    binding.idPosition.setBackgroundResource(R.drawable.custom_edittext_green_border)
                }
                else{
                    binding.idPosition.setBackgroundResource(R.drawable.custom_edittext_border)
                    isValidPosition = false
                }
            }
        })

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, roomList)
        binding.idPosition.setAdapter(adapter)
        binding.idDestination.setAdapter(adapter)

        binding.btnscanner.setOnClickListener {
            val destination=binding.idDestination.text.toString()
            val action= FormFragmentDirections.actionFormFragmentToQrcodeFragment(destination)
            findNavController().navigate(action)
        }

        binding.btndemarrer.setOnClickListener {
             position = binding.idPosition.text.toString()
             dest = binding.idDestination.text.toString()
            val colorStateList = ColorStateList.valueOf(Color.RED)
            if (position.isNotEmpty() && roomList.contains(position)) {
                isValidPosition = true

                //val message = ",$position,$dest"
            }else {
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
                    builder.setMessage("Les salles '$dest' et '$position' n'existent pas !!!")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        })
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else if (!isValidPosition) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("La salle '$position' n'existe pas !!!")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        })
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else if (!isValidDestination) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("La salle '$dest' n'existe pas !!!")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        })
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }
        }
       binding.destBtnHelp.setOnClickListener {
            onHelpClick()
        }
        binding.positionBtnHelp.setOnClickListener {
            onHelpClick()
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