package com.example.toytodoapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.toytodoapplication.databinding.RvTodoItemBinding
import com.example.toytodoapplication.db.TodoData

class TodoAdapter (private val list: MutableList<TodoData>):
RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){

    private var listener:TaskAdapterClicksInterface? = null
    fun setListenner(listener:TaskAdapterClicksInterface){
        this.listener = listener
    }

    inner class TodoViewHolder(val binding: RvTodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = RvTodoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TodoViewHolder(binding)
    }


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.todoTask.text = this.task

                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteTaskBtnClicked(this)
                }

                binding.editTask.setOnClickListener {
                    listener?.onEditTaskBtnClicked(this)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    interface TaskAdapterClicksInterface{
        fun onDeleteTaskBtnClicked(todoData: TodoData)
        fun onEditTaskBtnClicked(todoData: TodoData)
    }
}