package com.example.lab2

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startAnimationSequence()
    }

    private fun startAnimationSequence() {
        // ===== PHASE 1: Background orbs breathing (immediate) =====
        animateOrbs()

        // ===== PHASE 2: Particles float in (200ms) =====
        handler.postDelayed({ animateParticles() }, 200)

        // ===== PHASE 3: Logo ring spins in (300ms) =====
        handler.postDelayed({ animateLogoRing() }, 300)

        // ===== PHASE 4: Logo circle + icon pop in (600ms) =====
        handler.postDelayed({ animateLogoCenter() }, 600)

        // ===== PHASE 5: Title text rises up (1000ms) =====
        handler.postDelayed({ animateTitleText() }, 1000)

        // ===== PHASE 6: Subtitle fades in (1400ms) =====
        handler.postDelayed({ animateSubtitle() }, 1400)

        // ===== PHASE 7: Progress bar appears (1800ms) =====
        handler.postDelayed({ animateProgressArea() }, 1800)

        // ===== PHASE 8: Footer fades in (2100ms) =====
        handler.postDelayed({ animateFooter() }, 2100)

        // ===== PHASE 9: Start progress bar fill (2400ms) =====
        handler.postDelayed({ startProgressAnimation() }, 2400)
    }

    // ---------- Orb breathing animation ----------
    private fun animateOrbs() {
        val orbs = listOf(
            findViewById<View>(R.id.orbCyan),
            findViewById<View>(R.id.orbMint),
            findViewById<View>(R.id.orbAmber)
        )
        orbs.forEachIndexed { index, orb ->
            val scaleX = ObjectAnimator.ofFloat(orb, "scaleX", 0.8f, 1.2f).apply {
                duration = 4000L + index * 500L
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                interpolator = AccelerateDecelerateInterpolator()
            }
            val scaleY = ObjectAnimator.ofFloat(orb, "scaleY", 0.8f, 1.2f).apply {
                duration = 4000L + index * 500L
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                interpolator = AccelerateDecelerateInterpolator()
            }
            val alpha = ObjectAnimator.ofFloat(orb, "alpha", 0.4f, 1f).apply {
                duration = 3000L + index * 400L
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
            }
            AnimatorSet().apply {
                playTogether(scaleX, scaleY, alpha)
                startDelay = index * 300L
                start()
            }
        }
    }

    // ---------- Particle floating animation ----------
    private fun animateParticles() {
        val particles = listOf(
            findViewById<View>(R.id.particle1),
            findViewById<View>(R.id.particle2),
            findViewById<View>(R.id.particle3),
            findViewById<View>(R.id.particle4)
        )
        particles.forEachIndexed { index, particle ->
            // Fade in
            val fadeIn = ObjectAnimator.ofFloat(particle, "alpha", 0f, 0.7f).apply {
                duration = 800
                startDelay = index * 150L
            }
            // Continuous float up/down
            val floatY = ObjectAnimator.ofFloat(particle, "translationY", 0f, -30f).apply {
                duration = 2000L + index * 300L
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                interpolator = AccelerateDecelerateInterpolator()
            }
            // Continuous drift left/right
            val driftX = ObjectAnimator.ofFloat(particle, "translationX", -15f, 15f).apply {
                duration = 3000L + index * 400L
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                interpolator = AccelerateDecelerateInterpolator()
            }
            // Pulse scale
            val pulse = ObjectAnimator.ofFloat(particle, "scaleX", 1f, 2f).apply {
                duration = 1500L + index * 200L
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
            }
            val pulseY = ObjectAnimator.ofFloat(particle, "scaleY", 1f, 2f).apply {
                duration = 1500L + index * 200L
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
            }
            AnimatorSet().apply {
                playTogether(fadeIn, floatY, driftX, pulse, pulseY)
                start()
            }
        }
    }

    // ---------- Logo ring spin-in ----------
    private fun animateLogoRing() {
        val ring = findViewById<View>(R.id.logoRing)

        // Appear with scale
        val scaleX = ObjectAnimator.ofFloat(ring, "scaleX", 0.3f, 1f).apply {
            duration = 900
            interpolator = DecelerateInterpolator()
        }
        val scaleY = ObjectAnimator.ofFloat(ring, "scaleY", 0.3f, 1f).apply {
            duration = 900
            interpolator = DecelerateInterpolator()
        }
        val alpha = ObjectAnimator.ofFloat(ring, "alpha", 0f, 0.5f).apply {
            duration = 900
        }

        // Continuous slow rotation
        val rotation = ObjectAnimator.ofFloat(ring, "rotation", 0f, 360f).apply {
            duration = 15000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
        }

        // Continuous pulse
        val pulseX = ObjectAnimator.ofFloat(ring, "scaleX", 1f, 1.08f).apply {
            duration = 2500
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            startDelay = 900
        }
        val pulseY = ObjectAnimator.ofFloat(ring, "scaleY", 1f, 1.08f).apply {
            duration = 2500
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            startDelay = 900
        }

        AnimatorSet().apply {
            playTogether(scaleX, scaleY, alpha, rotation, pulseX, pulseY)
            start()
        }
    }

    // ---------- Logo circle + icon pop ----------
    private fun animateLogoCenter() {
        val circle = findViewById<View>(R.id.logoCircle)
        val icon = findViewById<View>(R.id.logoIcon)

        // Circle pop in
        val circleScaleX = ObjectAnimator.ofFloat(circle, "scaleX", 0f, 1f).apply {
            duration = 700
            interpolator = OvershootInterpolator(2f)
        }
        val circleScaleY = ObjectAnimator.ofFloat(circle, "scaleY", 0f, 1f).apply {
            duration = 700
            interpolator = OvershootInterpolator(2f)
        }
        val circleAlpha = ObjectAnimator.ofFloat(circle, "alpha", 0f, 1f).apply {
            duration = 500
        }

        // Icon pop in (slight delay after circle)
        val iconScaleX = ObjectAnimator.ofFloat(icon, "scaleX", 0f, 1f).apply {
            duration = 600
            startDelay = 200
            interpolator = OvershootInterpolator(3f)
        }
        val iconScaleY = ObjectAnimator.ofFloat(icon, "scaleY", 0f, 1f).apply {
            duration = 600
            startDelay = 200
            interpolator = OvershootInterpolator(3f)
        }
        val iconAlpha = ObjectAnimator.ofFloat(icon, "alpha", 0f, 1f).apply {
            duration = 400
            startDelay = 200
        }

        AnimatorSet().apply {
            playTogether(circleScaleX, circleScaleY, circleAlpha, iconScaleX, iconScaleY, iconAlpha)
            start()
        }
    }

    // ---------- Title text rise ----------
    private fun animateTitleText() {
        val title = findViewById<View>(R.id.splashTitle)
        val transY = ObjectAnimator.ofFloat(title, "translationY", 60f, 0f).apply {
            duration = 700
            interpolator = DecelerateInterpolator(2f)
        }
        val alpha = ObjectAnimator.ofFloat(title, "alpha", 0f, 1f).apply {
            duration = 700
        }
        AnimatorSet().apply {
            playTogether(transY, alpha)
            start()
        }
    }

    // ---------- Subtitle fade ----------
    private fun animateSubtitle() {
        val subtitle = findViewById<View>(R.id.splashSubtitle)
        val transY = ObjectAnimator.ofFloat(subtitle, "translationY", 40f, 0f).apply {
            duration = 600
            interpolator = DecelerateInterpolator()
        }
        val alpha = ObjectAnimator.ofFloat(subtitle, "alpha", 0f, 0.7f).apply {
            duration = 600
        }
        AnimatorSet().apply {
            playTogether(transY, alpha)
            start()
        }
    }

    // ---------- Progress area appear ----------
    private fun animateProgressArea() {
        val area = findViewById<View>(R.id.progressArea)
        val transY = ObjectAnimator.ofFloat(area, "translationY", 30f, 0f).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
        }
        val alpha = ObjectAnimator.ofFloat(area, "alpha", 0f, 1f).apply {
            duration = 500
        }
        AnimatorSet().apply {
            playTogether(transY, alpha)
            start()
        }
    }

    // ---------- Footer fade ----------
    private fun animateFooter() {
        val footer = findViewById<View>(R.id.footerArea)
        ObjectAnimator.ofFloat(footer, "alpha", 0f, 0.6f).apply {
            duration = 400
            start()
        }
    }

    // ---------- Progress bar fill (the main loading bar) ----------
    private fun startProgressAnimation() {
        val progressFill = findViewById<View>(R.id.progressFill)
        val progressPercent = findViewById<TextView>(R.id.progressPercent)
        val progressTrack = findViewById<View>(R.id.progressTrack)
        val loadingText = findViewById<TextView>(R.id.loadingText)

        // Get total width of the track
        progressTrack.post {
            val totalWidth = progressTrack.width

            // Animate the fill width from 0 to totalWidth over 3 seconds
            val widthAnimator = ValueAnimator.ofInt(0, totalWidth).apply {
                duration = 3000
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    val params = progressFill.layoutParams
                    params.width = value
                    progressFill.layoutParams = params

                    // Update percentage text
                    val percent = ((value.toFloat() / totalWidth) * 100).toInt()
                    progressPercent.text = "$percent%"

                    // Update loading text at milestones
                    loadingText.text = when {
                        percent < 25 -> "INITIALIZING..."
                        percent < 50 -> "LOADING QUESTIONS..."
                        percent < 75 -> "PREPARING QUIZ..."
                        percent < 100 -> "ALMOST READY..."
                        else -> "LET'S GO!"
                    }
                }
            }

            widthAnimator.start()

            // Navigate to Quiz after progress completes
            handler.postDelayed({
                // Final flash effect on the logo
                val logoCircle = findViewById<View>(R.id.logoCircle)
                val flash = ObjectAnimator.ofFloat(logoCircle, "scaleX", 1f, 1.3f, 1f).apply {
                    duration = 300
                }
                val flashY = ObjectAnimator.ofFloat(logoCircle, "scaleY", 1f, 1.3f, 1f).apply {
                    duration = 300
                }
                AnimatorSet().apply {
                    playTogether(flash, flashY)
                    start()
                }

                // Navigate after brief flash - directly to Quiz (which will show username dialog)
                handler.postDelayed({
                    val intent = Intent(this@SplashActivity, Quiz::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }, 400)
            }, 3200)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}