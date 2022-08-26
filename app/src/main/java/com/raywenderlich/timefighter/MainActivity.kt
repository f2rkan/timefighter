package com.raywenderlich.timefighter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity




//class'in adini TAG'e attik; android'te kural, log kayitlarinda sinif ismi kullanmaktir. Bu, mesajin
//hangi siniftan geldigini gormeyi kolaylastirir
private val TAG = MainActivity::class.java.simpleName
class MainActivity : AppCompatActivity() {



    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView
    private lateinit var tapMeButton: Button
    private var score = 0


    //for countdown
    //milisaniye cinsinden
    //countDownInterval, azalma hızı
    //countDownTimer, sıfıra kadar sayıyor
    //gameStarted, oyunun baslayıp baslamadıgını kontrol ediyor
    //timeLeft, geri sayımda kac saniye kaldi
    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //activity olusturuldugunda bir mesaj log'u olusturulur
        //iletiye $score seklinde degisken eklemek, Kotlin'deki String enterpolasyonu ornegidir.
        //runtime'da kotlin score'u arar ve log kaydinda degistirir
        Log.d(TAG, "onCreate called. Score is: $score")

        /*
        * “Şimdiye kadar Aktivitenizi ayarlamak için yalnızca onCreate() kullandınız.
        * onCreate() çağrıldığında oyunun sıfırlanmadığından emin olmak istiyorsanız,
        * bunu yapmak için parametre olarak yönteme geçirilen saveInstanceState nesnesini kullanmanız gerekir.
        */

        // 1
        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        tapMeButton = findViewById(R.id.tap_me_button)

        // 2
        tapMeButton.setOnClickListener { incrementScore() }
        // connect views to variables
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }
    //score ve timeLeft degerlerini Bundle'a ekledik
    //bir oryantasyon degisikligi olmadan once cagirilir ve onemli bi seyleri kaydetme olanagi verir
    //Bundle, Android'in değerleri farklı ekranlar arasında iletmek için kullandığı bir karma haritadır.
    //Ayrıca timer'ı iptal eder ve method'un ne zaman çağrıldığını izlemek için bir log ekler.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & TimeLeft: $timeLeft")
    }

    //Activity tarafından yok edildiğinde kendini temizlemek için kullanılan bir method olan onDestroy()'u override ettik.
    //Android'in belleği geri alması gerektiğinde veya bir geliştirici tarafından açıkça yok edildiğinde activity'ler yok edilir.
    //activity'nizin gerekli tüm temizleme işlemlerini yapabilmesi için 'super' öğesini çağırırsınız
    //ve onDestroy() çağrıldığında izlemek için son bir log eklersiniz.

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }
    private fun incrementScore() {
        // increment score logic
        //score++
        //val newScore = "Your Score: $score"
        //gameScoreTextView.text = newScore

        //oyun gercekten baslatıldı mı
        if (!gameStarted) {
            startGame()
        }

        score++
        val newScore = getString(R.string.your_score, score)
        gameScoreTextView.text = newScore
    }
    private fun resetGame() {
        // reset game logic
        // 1
        score = 0

        val initialScore = getString(R.string.your_score, score)
        gameScoreTextView.text = initialScore

        val initialTimeLeft = getString(R.string.time_left, 60)
        timeLeftTextView.text = initialTimeLeft

        // 2
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            // 3
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000

                val timeLeftString = getString(R.string.time_left, timeLeft)
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                // To Be Implemented Later
                endGame()
            }
        }

        // 4
        gameStarted = false
    }
    private fun restoreGame() {
        val restoredScore = getString(R.string.your_score, score)
        gameScoreTextView.text = restoredScore
        val restoredTime = getString(R.string.time_left, timeLeft)
        timeLeftTextView.text = restoredTime
        countDownTimer = object : CountDownTimer((timeLeft *
                1000).toLong(), countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.time_left,
                    timeLeft)
                timeLeftTextView.text = timeLeftString
            }
            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true
    }
    private fun startGame() {
        // start game logic
        countDownTimer.start()
        gameStarted = true
    }
    private fun endGame() {
        // end game logic
        Toast.makeText(this, getString(R.string.game_over_message,
            score), Toast.LENGTH_LONG).show()
        resetGame()
    }
    //oryantasyon degistiginde degerlerin kaydolacagi sabitler
    companion object {
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }
}