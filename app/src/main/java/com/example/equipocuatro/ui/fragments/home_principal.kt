package com.example.equipocuatro.ui.fragments

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.equipocuatro.R
import com.example.equipocuatro.databinding.FragmentHomePrincipalBinding
import com.example.equipocuatro.ui.dialogs.MostrarRetoAleatorio
import com.example.equipocuatro.viewmodel.HomeViewModel
import android.os.CountDownTimer


class home_principal : Fragment() {

    private var _binding: FragmentHomePrincipalBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private var mediaPlayer: MediaPlayer? = null
    private var spinPlayer: MediaPlayer? = null
    private var toneGenerator: ToneGenerator? = null
    private val spinToneHandler = Handler(Looper.getMainLooper())
    private var spinToneRunnable: Runnable? = null

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
        binding.txtNumero.visibility = View.GONE
        binding.imgBotella.rotation = viewModel.bottleRotation.value ?: 0f
        setupMediaPlayer()
        setupObservers()
        setupClickListeners()
        setupSpinButton()

        startCountdown()
    }

    private fun setupMediaPlayer() {
        try {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.game_music)?.apply {
                isLooping = true
            }
        } catch (_: Exception) {
            mediaPlayer = null
        }
    }

    private fun setupSpinButton() {
        binding.lottieButton.isClickable = true
        binding.lottieButton.isFocusable = true
        binding.txtPresioname.isClickable = true
        binding.txtPresioname.isFocusable = true

        val spinClickListener = View.OnClickListener {
            it.startTouchAnimation {
                viewModel.startBottleSpin()
            }
        }
        binding.lottieButton.setOnClickListener(spinClickListener)
        binding.txtPresioname.setOnClickListener(spinClickListener)
    }

    private fun setupObservers() {
        viewModel.isMusicEnabled.observe(viewLifecycleOwner) { isEnabled ->
            if (viewModel.pauseBackgroundMusicForGame.value == true) return@observe
            updateMusicState(isEnabled)
        }

        viewModel.pauseBackgroundMusicForGame.observe(viewLifecycleOwner) { shouldPause ->
            if (shouldPause) {
                mediaPlayer?.pause()
            } else if (viewModel.isMusicEnabled.value == true) {
                try {
                    mediaPlayer?.start()
                } catch (_: Exception) {
                }
            }
        }

        viewModel.showSpinButton.observe(viewLifecycleOwner) { show ->
            val visibility = if (show) View.VISIBLE else View.INVISIBLE
            binding.lottieButton.visibility = visibility
            binding.txtPresioname.visibility = visibility
            binding.lottieButton.isClickable = show
            binding.txtPresioname.isClickable = show
        }

        viewModel.countdownValue.observe(viewLifecycleOwner) { value ->
            if (value == null) {
                binding.txtNumero.visibility = View.GONE
            } else {
                binding.txtNumero.visibility = View.VISIBLE
                binding.txtNumero.text = value.toString()
            }
        }

        viewModel.spinDegrees.observe(viewLifecycleOwner) { degrees ->
            if (degrees > 0f && viewModel.isSpinning.value == true) {
                animateBottleRotation(degrees)
            }
        }

        viewModel.isSpinning.observe(viewLifecycleOwner) { spinning ->
            if (spinning) {
                startSpinSound()
            } else {
                stopSpinSound()
            }
        }

        viewModel.challengeDialog.observe(viewLifecycleOwner) { data ->
            data?.let {
                MostrarRetoAleatorio.showDialogoRetoAleatorio(requireContext(), it)
            }
        }
    }

    private fun animateBottleRotation(degrees: Float) {
        binding.imgBotella.clearAnimation()
        val startRotation = binding.imgBotella.rotation

        val rotate = RotateAnimation(
            0f,
            degrees,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            duration = HomeViewModel.SPIN_DURATION_MS
            interpolator = DecelerateInterpolator()
            fillAfter = true
        }

        rotate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                binding.imgBotella.rotation = startRotation + degrees
                binding.imgBotella.clearAnimation()
            }
        })

        binding.imgBotella.startAnimation(rotate)
    }
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

    private fun startSpinSound() {
        stopSpinSound()
        val resId = resources.getIdentifier("bottle_spin", "raw", requireContext().packageName)
        if (resId != 0) {
            spinPlayer = MediaPlayer.create(requireContext(), resId)?.apply {
                isLooping = true
                start()
            }
            return
        }
        toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        spinToneRunnable = object : Runnable {
            override fun run() {
                if (viewModel.isSpinning.value == true) {
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 120)
                    spinToneHandler.postDelayed(this, 180)
                }
            }
        }
        spinToneHandler.post(spinToneRunnable!!)
    }

    private fun stopSpinSound() {
        spinPlayer?.stop()
        spinPlayer?.release()
        spinPlayer = null
        spinToneRunnable?.let { spinToneHandler.removeCallbacks(it) }
        spinToneRunnable = null
        toneGenerator?.release()
        toneGenerator = null
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
        binding.toolbarHome.volumeUpButton.setOnClickListener {
            it.startTouchAnimation {
                viewModel.toggleMusic()
            }
        }

        binding.toolbarHome.starButton.setOnClickListener {
            it.startTouchAnimation {
                val playStoreUrl =
                    "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUrl))
                startActivity(intent)
            }
        }

        binding.toolbarHome.controlButton.setOnClickListener {
            it.startTouchAnimation {
                findNavController().navigate(R.id.action_home_principal2_to_instrucciones)
            }
        }

        binding.toolbarHome.addButton.setOnClickListener {
            it.startTouchAnimation {
                findNavController().navigate(R.id.action_home_principal2_to_fragment_retos2)
            }
        }

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
        viewModel.cancelActiveGame()
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
        if (spinPlayer?.isPlaying == true) {
            spinPlayer?.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        updateMusicState(viewModel.isMusicEnabled.value ?: true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.imgBotella.clearAnimation()
        stopSpinSound()

        countdownTimer?.cancel()
        countdownTimer = null
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }
}
