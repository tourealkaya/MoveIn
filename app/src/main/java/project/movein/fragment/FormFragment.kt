package project.movein.fragment
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import project.movein.R
import project.movein.databinding.FragmentFormBinding

class FormFragment : Fragment() {
    private lateinit var binding: FragmentFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentFormBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnscanner.setOnClickListener {
            val destination=binding.idDestination.text.toString()
            val action= FormFragmentDirections
                .actionFormFragmentToQrcodeFragment(destination)
            findNavController().navigate(action)
        }

        binding.destBtnHelp.setOnClickListener {
            onHelpClick()
        }

        binding.positionBtnHelp.setOnClickListener {
            onHelpClick()
        }

        val args: FormFragmentArgs by navArgs()
        val positionValue = args.position
        val destinationValue = args.destination

        positionValue.let {
            binding.idPosition.setText(it)
        }
        destinationValue.let {
            binding.idDestination.setText(it)
        }

        binding.btndemarrer.setOnClickListener {
            val enterPosition = binding.idPosition.text.toString()
            val enterDestination = binding.idDestination.text.toString()
            findNavController().navigate(R.id.action_formFragment_to_resultFragment)
        }
    }

    private fun onHelpClick(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Aide")
               .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id -> })
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

}






