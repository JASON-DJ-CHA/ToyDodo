package com.example.toytodoapplication.view.main

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.toytodoapplication.R
import com.example.toytodoapplication.databinding.FragmentSignUpBinding
import com.example.toytodoapplication.databinding.FragmentTimeBinding
import com.google.firebase.auth.FirebaseAuth

class TimeFragment : Fragment() {

    private lateinit var auth:FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentTimeBinding


    var running: Boolean = false
    var pauseTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTimeBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()

    }

    private fun registerEvents() {

        viewMode("stop")

        binding.moveHome.setOnClickListener{
            navControl.navigate(R.id.action_timeFragment_to_homeFragment)
        }



        binding.startBtn.setOnClickListener {
            if (!running){
                binding.chronometer.base = SystemClock.elapsedRealtime() - pauseTime

                binding.chronometer.start()

                viewMode("Start")
            }
        }

        binding.stopBtn.setOnClickListener {
            if (running){
                binding.chronometer.stop()

                pauseTime = SystemClock.elapsedRealtime() - binding.chronometer.base

                viewMode("stop")
            }
        }

        binding.resetBtn.setOnClickListener {
            binding.chronometer.base = SystemClock.elapsedRealtime()

            pauseTime = 0L

            binding.chronometer.stop()

            viewMode("stop")
        }

    }

    private fun viewMode(mode:String) {
        if (mode =="Start"){
            binding.startBtn.isEnabled = false
            binding.stopBtn.isEnabled = true
            binding.resetBtn.isEnabled = true
            running = true
        }else{
            binding.startBtn.isEnabled = true
            binding.stopBtn.isEnabled = false
            binding.resetBtn.isEnabled = false
            running = false
        }


    }

}