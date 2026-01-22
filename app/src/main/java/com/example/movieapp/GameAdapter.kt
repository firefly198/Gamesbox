package com.example.movieapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movieapp.data.GameEntity
import com.example.movieapp.databinding.ItemGameBinding

class GameAdapter(
    private val games: List<GameEntity>,
    private val onClick: (GameEntity) -> Unit
) : RecyclerView.Adapter<GameAdapter.VH>() {

    inner class VH(
        private val binding: ItemGameBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(game: GameEntity) {
            binding.textGameName.text = game.name
            binding.textGameDescription.text = game.description

            // ✔ Coil გამოიყენება (ლოკალური drawable)
            binding.imageGame.load(game.imageRes)

            binding.root.setOnClickListener {
                onClick(game)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemGameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(games[position])
    }

    override fun getItemCount(): Int = games.size
}


