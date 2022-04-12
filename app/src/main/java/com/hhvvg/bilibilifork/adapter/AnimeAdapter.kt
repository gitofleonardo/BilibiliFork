package com.hhvvg.bilibilifork.adapter

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hhvvg.bilibilifork.databinding.AnimeListItemLayoutBinding
import com.hhvvg.bilibilifork.network.resp.AnimeEntity

/**
 * @author hhvvg
 */
class AnimeAdapter(private val items: List<AnimeItemWrapper>) : RecyclerView.Adapter<Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = AnimeListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding.root)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}

class Holder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: AnimeItemWrapper) {
        val binding = AnimeListItemLayoutBinding.bind(itemView)
        binding.animeTitle.text = SpannableString(item.animeItem.title)
        binding.animeDesc.text = SpannableString(item.animeItem.evaluate)
        binding.selectCheckbox.isChecked = item.selected
        binding.selectCheckbox.setOnCheckedChangeListener { _, checked ->
            item.selected = checked
        }
        Glide.with(itemView.context).load(item.animeItem.cover).into(binding.animeCover)
    }
}

class AnimeItemWrapper(
    val animeItem: AnimeEntity,
    var selected: Boolean
)
