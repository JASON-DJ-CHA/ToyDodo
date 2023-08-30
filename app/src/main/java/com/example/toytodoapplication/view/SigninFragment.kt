package com.example.toytodoapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.toytodoapplication.R
import com.example.toytodoapplication.databinding.FragmentSignUpBinding
import com.example.toytodoapplication.databinding.FragmentSigninBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class SigninFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSigninBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }



    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()

    }

    private fun registerEvents() {

        binding.authTextView.setOnClickListener{
            navController.navigate(R.id.action_signinFragment_to_signUpFragment)
        }

        binding.nextBtn.setOnClickListener{
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()

            //파이어베이스 가 작동하지않아 임시로 사용
                        navController.navigate(R.id.action_signinFragment_to_homeFragment)
            binding.progressBar.visibility = View.VISIBLE
//            if(email.isNotEmpty() && pass.isNotEmpty()){
//
//                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
//                    if (it.isSuccessful)
//                        navController.navigate(R.id.action_signinFragment_to_homeFragment)
//                    else{
//                        Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
//                        }
//            binding.progressBar.visibility = View.GONE

//                }
//
//             }else {
//                Toast.makeText(context, "빈칸이 있는지 확인해주세요", Toast.LENGTH_SHORT).show()
//             }
        }
    }

}