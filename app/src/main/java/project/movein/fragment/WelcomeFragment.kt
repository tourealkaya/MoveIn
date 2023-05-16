package project.movein.fragment

import androidx.appcompat.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import project.movein.R
import project.movein.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {

private lateinit var binding: FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentWelcomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btncommencer.setOnClickListener{
            findNavController().navigate(R.id.action_welcomeFragment_to_formFragment)
        }
        val helpButton: Button = view.findViewById(R.id.btn_help)
        helpButton.setOnClickListener {
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