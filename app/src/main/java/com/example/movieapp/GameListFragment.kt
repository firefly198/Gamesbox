package com.example.movieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.data.GameDatabase
import com.example.movieapp.data.GameEntity
import com.example.movieapp.databinding.FragmentGameListBinding
import kotlinx.coroutines.launch

class GameListFragment : Fragment() {

    private var _binding: FragmentGameListBinding? = null
    private val binding get() = _binding!!

    private var showFavorites = false


    private val fakeGames = listOf(
        GameEntity(
            name = "Call of Duty: MW2",
            description = "Epic sequel with cinematic missions",
            fullDescription = "Call of Duty: Modern Warfare 2 delivers an intense and cinematic first-person shooter experience that continues the story of the Modern Warfare series. The game follows elite special forces units as they attempt to stop a global terrorist threat across multiple countries. It focuses on teamwork, sacrifice, and the brutal realities of modern warfare.",
            link = "https://store.steampowered.com/app/10180/Call_of_Duty_Modern_Warfare_2_2009/",
            imageRes = R.drawable.modernwarfare2,
            imageBig = R.drawable.mw2big
        ),
        GameEntity(
            name = "Call of Duty: MW3",
            description = "Final chapter of the trilogy",
            fullDescription = "Modern Warfare 3 concludes the Modern Warfare trilogy with a full-scale global conflict. Players participate in massive battles across the world, experiencing dramatic and emotional story moments that bring closure to the saga.",
            link = "https://store.steampowered.com/app/115300/Call_of_Duty_Modern_Warfare_3_2011/",
            imageRes = R.drawable.mw3,
            imageBig = R.drawable.mw3big
        ),
        GameEntity(
            name = "Black Ops",
            description = "Cold War era shooter",
            fullDescription = "Call of Duty: Black Ops is set during the Cold War and presents a psychological narrative involving covert operations, conspiracies, and mind control. Its story-driven campaign is known for its twists and dark tone.",
            link = "https://store.steampowered.com/app/42700/Call_of_Duty_Black_Ops/",
            imageRes = R.drawable.blackops,
            imageBig = R.drawable.blackopsbig
        ),
        GameEntity(
            name = "Cyberpunk 2077",
            description = "Futuristic open-world RPG",
            fullDescription = "Cyberpunk 2077 is an open-world RPG set in Night City, a futuristic metropolis obsessed with power and technology. Players shape their story through choices, exploring themes of identity and freedom.",
            link = "https://store.steampowered.com/app/1091500/Cyberpunk_2077/",
            imageRes = R.drawable.cyberpunk,
            imageBig = R.drawable.cyberpunk2077big
        ),
        GameEntity(
            name = "Bioshock",
            description = "Underwater dystopian story",
            fullDescription = "BioShock is set in the underwater city of Rapture, where extreme individualism leads to collapse. The game blends action with philosophical storytelling and unforgettable atmosphere.",
            link = "https://store.steampowered.com/app/7670/BioShock/",
            imageRes = R.drawable.bioshock,
            imageBig = R.drawable.bioshockbig
        ),
        GameEntity(
            name = "Spec Ops: The Line",
            description = "Psychological military shooter",
            fullDescription = "Spec Ops: The Line challenges traditional war narratives by focusing on the psychological impact of combat. Set in Dubai, it forces players to confront moral ambiguity and responsibility.",
            link = "https://store.steampowered.com/app/50300/Spec_Ops_The_Line/",
            imageRes = R.drawable.theline,
            imageBig = R.drawable.specopsthelinebig
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGameListBinding.inflate(inflater, container, false)

        val dao = GameDatabase.getDatabase(requireContext()).gameDao()

        binding.recyclerViewGames.layoutManager =
            LinearLayoutManager(requireContext())


        lifecycleScope.launch {
            if (dao.getAllGames().isEmpty()) {
                dao.insertGames(fakeGames)
            }
            loadGames(dao)
        }


        binding.textAllGames.setOnClickListener {
            showFavorites = false
            updateTabs()
            lifecycleScope.launch { loadGames(dao) }
        }


        binding.textFavorites.setOnClickListener {
            showFavorites = true
            updateTabs()
            lifecycleScope.launch { loadGames(dao) }
        }

        updateTabs()

        return binding.root
    }

    private suspend fun loadGames(dao: com.example.movieapp.data.GameDao) {
        val games = if (showFavorites) {
            dao.getFavoriteGames()
        } else {
            dao.getAllGames()
        }

        binding.recyclerViewGames.adapter =
            GameAdapter(games) { game ->
                val bundle = Bundle().apply {
                    putInt("gameId", game.id)
                }
                findNavController()
                    .navigate(R.id.gameDetailsFragment, bundle)
            }
    }

    private fun updateTabs() {
        val activeColor = ContextCompat.getColor(requireContext(), R.color.black)
        val inactiveColor = ContextCompat.getColor(requireContext(), android.R.color.darker_gray)

        binding.textAllGames.setTextColor(if (!showFavorites) activeColor else inactiveColor)
        binding.textFavorites.setTextColor(if (showFavorites) activeColor else inactiveColor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

