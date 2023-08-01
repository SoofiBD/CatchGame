package com.example.catchgame

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.catchgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var score = 0
    private var highScore: Int? = null
    private val imageArray = ArrayList<android.widget.ImageView>()
    private val handler = android.os.Handler(Looper.getMainLooper())
    private var runnable = Runnable { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // highScore'u SharedPreferences'tan al
        val sharedPref = getSharedPreferences("MyGame", Context.MODE_PRIVATE)
        highScore = sharedPref.getInt("highScore", 0)
        binding.highScoreText.text = "High Score: $highScore"

        imageArray.addAll(listOf(
            binding.imageView1, binding.imageView2, binding.imageView3,
            binding.imageView4, binding.imageView5, binding.imageView6,
            binding.imageView7, binding.imageView8, binding.imageView9,
            binding.imageView10, binding.imageView11, binding.imageView12,
            binding.imageView13, binding.imageView14, binding.imageView15,
            binding.imageView16
        ))

        hideImages()

        //Countdown Timer
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timeText.text = "Time: " + millisUntilFinished / 1000
            }

            override fun onFinish() {
                binding.timeText.text = "Time's Off"
                handler.removeCallbacks(runnable)
                for (image in imageArray) {
                    image.visibility = android.view.View.INVISIBLE
                }

                //High Score
                if (highScore == null || score > highScore!!) {
                    highScore = score
                    // save new high score in SharedPreferences
                    val editor = sharedPref.edit()
                    editor.putInt("highScore", highScore!!)
                    editor.apply()

                    binding.highScoreText.text = "High Score: $highScore"
                }

                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setTitle("Game Over")
                alert.setMessage("Restart The Game?")
                alert.setPositiveButton("Yes") { dialog, _ ->
                    //Restart
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
                alert.setNegativeButton("No") { _, _ ->
                    Toast.makeText(this@MainActivity, "Game Over", Toast.LENGTH_SHORT).show()
                }
                alert.show()
            }
        }.start()
    }

    fun hideImages() {
        runnable = object : Runnable {
            override fun run() {
                for (image in imageArray) {
                    image.visibility = android.view.View.INVISIBLE
                }
                val random = java.util.Random()
                val randomIndex = random.nextInt(16) // 16 is the size of imageArray

                imageArray[randomIndex].visibility = android.view.View.VISIBLE

                handler.postDelayed(this, 400)
            }
        }
        handler.post(runnable)
    }

    fun increaseScore(view: android.view.View) {
        score++
        binding.scoreText.text = "Score: $score"
    }
}


//binding.imageView1.visibility = android.view.View.INVISIBLE this is the long way to hiding images