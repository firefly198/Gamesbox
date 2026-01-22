package com.example.movieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.movieapp.data.GameDatabase
import com.example.movieapp.databinding.FragmentGameDetailsBinding
import kotlinx.coroutines.launch

class GameDetailsFragment : Fragment() {

    private var _binding: FragmentGameDetailsBinding? = null
    private var currentRating = 0
    private val binding get() = _binding!!

    private var gameId: Int = -1
    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGameDetailsBinding.inflate(inflater, container, false)

        gameId = arguments?.getInt("gameId") ?: return binding.root
        val dao = GameDatabase.getDatabase(requireContext()).gameDao()

        viewLifecycleOwner.lifecycleScope.launch {

            val game = dao.getGameById(gameId)
            isFavorite = game.isFavorite

            currentRating = game.rating
            updateStars(currentRating)

            binding.textTitle.text = game.name
            binding.textDescription.text = game.fullDescription
            binding.imageGame.load(game.imageBig)

            binding.textLink.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(game.link)
                )
                startActivity(intent)
            }

            updateButton()

            binding.btnFavorite.setOnClickListener {
                isFavorite = !isFavorite

                viewLifecycleOwner.lifecycleScope.launch {
                    dao.updateFavorite(gameId, isFavorite)
                    updateButton()
                }
            }
            val stars = listOf(
                binding.star1,
                binding.star2,
                binding.star3,
                binding.star4,
                binding.star5
            )

            stars.forEachIndexed { index, imageView ->
                imageView.setOnClickListener {
                    val selectedRating = index + 1

                    currentRating =
                        if (currentRating == selectedRating) 0 else selectedRating

                    viewLifecycleOwner.lifecycleScope.launch {
                        dao.updateRating(gameId, currentRating)
                    }

                    updateStars(currentRating)
                }
            }

        }

        return binding.root
    }

    private fun updateButton() {
        binding.btnFavorite.text =
            if (isFavorite) "Remove from Favorites" else "Add to Favorites"
    }

    private fun updateStars(rating: Int) {
        val stars = listOf(
            binding.star1,
            binding.star2,
            binding.star3,
            binding.star4,
            binding.star5
        )

        stars.forEachIndexed { index, imageView ->
            if (index < rating) {
                imageView.setImageResource(R.drawable.star_rate_filled)
            } else {
                imageView.setImageResource(R.drawable.star_rate_empty)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
