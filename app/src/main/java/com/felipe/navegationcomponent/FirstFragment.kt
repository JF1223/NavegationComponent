package com.felipe.navegationcomponent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class FirstFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_first,container, false)

        val btnNavigate = root.findViewById<Button>(R.id.btnNavigate)

        btnNavigate.setOnClickListener{
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment2)
        }
        return root
    }


}