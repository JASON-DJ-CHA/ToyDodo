package com.example.toytodoapplication.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toytodoapplication.R
import com.example.toytodoapplication.databinding.FragmentHomeBinding
import com.example.toytodoapplication.db.TodoData
import com.example.toytodoapplication.view.adapter.TodoAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment(), AddTodoFragment.DialogNextBtnClickListener,
    TodoAdapter.TaskAdapterClicksInterface {

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseRef :DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding : FragmentHomeBinding
    private var popUpFragment : AddTodoFragment? = null
    private lateinit var adapter : TodoAdapter
    private lateinit var mList: MutableList<TodoData>



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
        getDataFromFirebase()
        registerEvents()
    }

    private fun registerEvents() {

        binding.logout.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_signinFragment)
            auth.signOut()
        }

        binding.moveTimer.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_timeFragment)

        }

        binding.addBtnHome.setOnClickListener {
            if(popUpFragment != null){
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            }
            popUpFragment = AddTodoFragment()
            popUpFragment!!.setListener(this)
            popUpFragment!!.show(
                childFragmentManager,
                AddTodoFragment.TAG
            )
        }
    }

    private fun init(view: View) {

        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Tasks").child(auth.currentUser?.uid.toString())

        binding.todoRV.setHasFixedSize(true)
        binding.todoRV.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = TodoAdapter(mList)
        adapter.setListenner(this)
        binding.todoRV.adapter =adapter
    }

    // todolist저장하기
    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {

        databaseRef.push().setValue(todo).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "입력이 완료되었습니다.", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null

            popUpFragment!!.dismiss()
        }
    }

    // todolist 수정하기
    override fun onUpdateTask(todoData: TodoData, todoEt: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[todoData.taskID] = todoData.task
        databaseRef.updateChildren(map).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context, "성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null

            popUpFragment!!.dismiss()
        }
    }

    private fun getDataFromFirebase() {
        databaseRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (taskSnapshot in snapshot.children){
                    val todoTask = taskSnapshot.key?.let {
                        TodoData(it, taskSnapshot.value.toString())
                    }

                    if (todoTask != null){
                        mList.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDeleteTaskBtnClicked(todoData: TodoData) {
        databaseRef.child(todoData.taskID).removeValue().addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context, "성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onEditTaskBtnClicked(todoData: TodoData) {
        if (popUpFragment != null){
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
        }
        popUpFragment = AddTodoFragment.newInstance(todoData.taskID, todoData.task)
        popUpFragment!!.setListener(this)
        popUpFragment!!.show(childFragmentManager, AddTodoFragment.TAG)
    }

}