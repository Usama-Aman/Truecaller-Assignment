package com.truecaller.assignment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.truecaller.assignment.R
import com.truecaller.assignment.databinding.ItemWordCountBinding

class WordCountRecyclerAdapter(
    private val words: ArrayList<String>,
    private val wordsCount: ArrayList<Int>
) : RecyclerView
.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Item(
            ItemWordCountBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_word_count,
                    parent,
                    false
                )
            )
        )
    }

    override fun getItemCount(): Int = words.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Item)
            holder.onBind(position)
    }

    inner class Item(private val binding: ItemWordCountBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            binding.tvWord.text = words[position]
            binding.tvWordCount.text = wordsCount[position].toString()
        }
    }
}