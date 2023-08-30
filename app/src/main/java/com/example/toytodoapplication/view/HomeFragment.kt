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
import com.example.toytodoapplication.databinding.FragmentHomeBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class HomeFragment : Fragment(), AddTodoFragment.DialogNextBtnClickListener {

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseRef :DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding : FragmentHomeBinding
    private lateinit var popUpFragment : AddTodoFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        registerEvents()
    }

    private fun registerEvents() {
        binding.addBtnHome.setOnClickListener {
            popUpFragment = AddTodoFragment()
            popUpFragment.setListener(this)
            popUpFragment.show(
                childFragmentManager,
                "AddTodoFragment"
            )
        }
    }

    private fun init(view: View) {

        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Tasks").child(auth.currentUser?.uid.toString())

    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {

        databaseRef.push().setValue(todo).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context, "입력이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                todoEt.text = null

            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }

            popUpFragment.dismiss()
        }
    }

}