package com.s26.personlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.s26.personlist.databinding.ItemViewBinding

class MyAdapter(
    val context: Context,
    val persons: ArrayList<Person>,
    private val callback: Callback
):RecyclerView.Adapter<MyAdapter.ViewHolder>(){
    interface  Callback{
        fun onItemClick(person: Person)
    }
    private val inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemViewBinding.inflate(inflater, parent, false), callback)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(context, persons[position])
    override fun getItemCount(): Int = persons.size
    class  ViewHolder(
        private val binding: ItemViewBinding,
        private val callback: Callback
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(context: Context,person: Person){
            binding.run {
                root.setOnClickListener {
                    callback.onItemClick(person)
                }
                name.text = person.name
                age.text = person.age.toString()
                isMarried.text = context.getText(if(person.isMarried) R.string.radio1 else R.string.radio2)
            }
        }
    }
}

