package com.jordivega.timefighter

import android.content.IntentSender.OnFinished
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    //declarem variables
    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView
    private lateinit var tapMeButton: Button

    //creem variable de la puntuació
    private var score = 0

    //variable inicial del joc
    private var gameStarted = false

    //creem variables del temps
    private lateinit var countDownTimer: CountDownTimer //objecte que compta fins a zero
    private var initialCountDown: Long = 10000 //interval 60s
    private var countDownInterval: Long = 1000 //temps que decrementa 1s
    private var timeLeft = 10 //variable per a saber el temps que queda al compte enrrere


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        //assignem variables a la vista del layout
        gameScoreTextView = findViewById(R.id.game_score_text_view) //busca al activity_main el id
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        tapMeButton = findViewById(R.id.tap_me_button)

        //assignem un listener al boto al clicar li afegim animació
        tapMeButton.setOnClickListener { view ->
            val bounceAnimation = AnimationUtils.loadAnimation(this,R.anim.bounce)
            view.startAnimation(bounceAnimation)
            incrementScore()
        }

        //reset game
        if (savedInstanceState != null) {
            //assignem els valors score i timeLeft de l'estat
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            //restaurem joc
            restoreGame()
        } else {
            resetGame()
        }
    }

    //la funció onSaveInstanceState se crida abans que pasen els canvis de configuració i per tant guardarem l'estat de les variables
    override fun onSaveInstanceState(outState: Bundle) { //Bundle es un hashmap que utilitza Android per passar valors a diferents pantalles
        super.onSaveInstanceState(outState)

        //guardem l'estat de les variables score i timeleft
        outState.putInt(SCORE_KEY,score)
        outState.putInt(TIME_LEFT_KEY,timeLeft)
        //aturem el temps
        countDownTimer.cancel()

        //guardem el log
        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left: $timeLeft")

    }

    //la funció onDestroy neteja l'activitat al destruir-se
    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy called.")
    }

    //sobreescriu l'activitat quan intenta crear el menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflem el menu, aixo afegeix els items a la action bar
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    //creem la funcio per a poder clicar al desplegable
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.about_item) {
            //cridem una funció per a mostrar el dialog
            showInfo()
        }
        return true
    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.about_title, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.about_message)

        //crearem el dialeg
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }

    //funcio per incrementar la puntuació
    private fun incrementScore() {
        //si el estat no està inicialitzat, comença el joc cridant la funció startGame
        if(!gameStarted) {
            startGame()
        }

        //incrementem puntuació
        score++

        //creem string
        //val newScore = "Your Score: $score"
        //utilitzem el string.xml
        val newScore = getString(R.string.your_score, score) //en la funcio getstring agafem your_Score de string.xml i li passem la variable score que mostrarem
        //assignar al textview la variable newscore
        gameScoreTextView.text = newScore
    }

    //funcio restore game al girar la pantalla
    private fun restoreGame() {
        //creem variables i li assignem el valor a la vista
        val restoredScore = getString(R.string.your_score,score)
        gameScoreTextView.text = restoredScore

        val restoredTime = getString(R.string.time_left,timeLeft)
        timeLeftTextView.text = restoredTime

        countDownTimer = object : CountDownTimer((timeLeft * 1000).toLong(), countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000

                val timeLeftString = getString(R.string.time_left,timeLeft)
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }

        //inicialitzar el timer i posar la variable d'estat a true
        countDownTimer.start()
        gameStarted = true
    }

    //funcio reset game
    private fun resetGame() {
        //primer li assignem la variable score a zero
        score = 0

        //assignem al text view gamescore la variable score inicial
        val initialScore = getString(R.string.your_score, score)
        gameScoreTextView.text = initialScore

        //assignem al text view timeleft el temps inicial a 60s
        val initialTimeLeft = getString(R.string.time_left, 10)
        timeLeftTextView.text = initialTimeLeft

        //creem objecte countdowntimer
        countDownTimer = object : CountDownTimer(initialCountDown,countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000

                val timeLeftString = getString(R.string.time_left, timeLeft)
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                //cridem la funció endgame per a mostrar el toast i resetejar el joc
                endGame()
            }
        }

        gameStarted = false
    }

    //funcio start
    private fun startGame() {
        //cridem al timer
        countDownTimer.start()
        //assignem la variable del estat a true
        gameStarted = true
    }

    //funcio end
    private fun endGame() {
        //mostrar toast en text de la puntuació final
        Toast.makeText(this, getString(R.string.game_over_message, score), Toast.LENGTH_LONG).show() //LENGTH_LONG apareix uns segons i desapareix el toast

        //desactivem el botó
        tapMeButton.isEnabled = false

        //creem el temporitzador de 5 segons per reactivar el botó
        object : CountDownTimer(5000,1000) {
            override fun onTick(millisUntilFinished: Long) {
                //no ho utilitzem per aquesta funcio de bloqueig
            }

            override fun onFinish() {
                //tornem a activar el botó
                tapMeButton.isEnabled = true

                //resetejar el joc cridant la funció resetGame
                resetGame()
            }

        }.start()

    }

    //creem companion object per guardar l'estat de les variables al girar la pantalla
    companion object {

        private const val SCORE_KEY = "SCORE_KEY"

        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"

    }

}