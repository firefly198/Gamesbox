package com.example.movieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.data.GameDatabase
import com.example.movieapp.databinding.FragmentFavoritesBinding
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        val dao = GameDatabase
            .getDatabase(requireContext())
            .gameDao()

        binding.recyclerViewFavorites.layoutManager =
            LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {


            val favorites = dao.getFavoriteGames()

            binding.recyclerViewFavorites.adapter =
                GameAdapter(favorites) { game ->

                }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



