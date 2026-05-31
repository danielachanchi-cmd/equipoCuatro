package com.example.equipocuatro.ui.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.equipocuatro.R
import com.example.equipocuatro.databinding.FragmentHomePrincipalBinding
import com.example.equipocuatro.viewmodel.HomeViewModel
import android.os.CountDownTimer


class home_principal : Fragment() {

    private var _binding: FragmentHomePrincipalBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePrincipalBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var countdownTimer: CountDownTimer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMediaPlayer()
        setupObservers()
        setupClickListeners()
        startCountdown()
    }

    private fun setupMediaPlayer() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.game_music)
        mediaPlayer?.isLooping = true
    }

    private fun setupObservers() {
        viewModel.isMusicEnabled.observe(viewLifecycleOwner) { isEnabled ->
            updateMusicState(isEnabled)
        }
    }

    private fun startCountdown() {

        countdownTimer = object : CountDownTimer(4000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                if (_binding != null) {
                    val seconds = millisUntilFinished / 1000
                    binding.txtNumero.text = seconds.toString()
                }
            }

            override fun onFinish() {
                if (_binding != null) {
                    binding.txtNumero.text = "0"
                }
            }

        }.start()
    }


    private fun updateMusicState(isEnabled: Boolean) {
        mediaPlayer?.let { player ->
            if (isEnabled) {
                if (!player.isPlaying) {
                    try {
                        player.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                binding.toolbarHome.volumeUpButton.setImageResource(R.drawable.volume_up)
            } else {
                if (player.isPlaying) {
                    try {
                        player.pause()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                binding.toolbarHome.volumeUpButton.setImageResource(R.drawable.volume_off)
            }
        }
    }

    private fun View.startTouchAnimation(onAnimationEnd: () -> Unit) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.scale_touch)
        animation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                onAnimationEnd()
            }
        })
        this.startAnimation(animation)
    }

    private fun setupClickListeners() {
        // Criterio Audio: Interruptor de sonido
        binding.toolbarHome.volumeUpButton.setOnClickListener {
            it.startTouchAnimation {
                viewModel.toggleMusic()
            }
        }

        // Criterio 2 y HU-4.0: Calificar la app (Redirigir a Nequi en Play Store)
        binding.toolbarHome.starButton.setOnClickListener {
            it.startTouchAnimation {
                val playStoreUrl = "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUrl))
                startActivity(intent)
            }
        }

        // Criterio 4 y HU-5.0: Instrucciones del juego
        binding.toolbarHome.controlButton.setOnClickListener {
            it.startTouchAnimation {
                findNavController().navigate(R.id.action_home_principal2_to_instrucciones)
            }
        }
        
        // Navegación a Retos
        binding.toolbarHome.addButton.setOnClickListener {
            it.startTouchAnimation {
                findNavController().navigate(R.id.action_home_principal2_to_fragment_retos2)
            }
        }

        // Botón Compartir
        binding.toolbarHome.shareButton.setOnClickListener {
            it.startTouchAnimation {
                val shareMessage = """
                    ¡App pico botella!
                    Solo los valientes lo juegan !!
                    Descárgala aquí: https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es
                """.trimIndent()

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Compartir mediante")
                startActivity(shareIntent)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        updateMusicState(viewModel.isMusicEnabled.value ?: true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        countdownTimer = null
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }
}
