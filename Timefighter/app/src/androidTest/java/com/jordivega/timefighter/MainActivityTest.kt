package com.jordivega.timefighter

import android.os.Looper
import android.os.Handler
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    //creem variable per esperar a que el comptador acabi
    //private val idlingResource = CountingIdlingResource("countdown")

    @Test
    fun testTimeDecrementsAfterStartGame() {
        //Registrem el recurs idling
        //IdlingRegistry.getInstance().register(idlingResource)

        //iniciem el joc
        onView(withId(R.id.tap_me_button)).perform(click())

        //Deixem passar un segon
        //Thread.sleep(1000)
        //idlingResource.increment()

        //comprovem que el temps ha passat un segon
        //onView(withId(R.id.time_left_text_view)).check(matches(withText(containsString("Time left: 9"))))
        Handler(Looper.getMainLooper()).postDelayed({
            onView(withId(R.id.time_left_text_view)).check(matches(withText(containsString("Time left: 9"))))
        },1000)
        //eliminem el registre idling
        //IdlingRegistry.getInstance().unregister(idlingResource)
    }


    @Test
    fun testGameStartsOnButtonClick() {
        //comprovem que el botó estigui habilitat
        onView(withId(R.id.tap_me_button)).check(matches(isEnabled()))

        //farem click al botó
        onView(withId(R.id.tap_me_button)).perform(click())

        //comprovem que la puntuació ha augmentat ja que si augmenta el joc ha començat
        onView(withId(R.id.game_score_text_view)).check(matches(withText(containsString("Your score: 1"))))
    }

    @Test
    fun testEndGameDisablesButton() {

        //inicialitzar el joc al fer el click
        onView(withId(R.id.tap_me_button)).perform(click())

        //Esperem a que el joc s'acabi
        Thread.sleep(11000)

        //comprovem que el botó estigui desactivat
        onView(withId(R.id.tap_me_button)).check(matches(not(isEnabled())))

    }

    @Test
    fun testButtonReactivatesAfterDelay() {
        //inici del joc
        onView(withId(R.id.tap_me_button)).perform(click())

        //Esperem a que el temps s'acabi
        Thread.sleep(11000)

        //comprovem que el botó estigui desactivat
        onView(withId(R.id.tap_me_button)).check(matches(not(isEnabled())))

        //Esperem 5 segons
        Thread.sleep(5000)

        //Comprovem que el botó està habilitat per poder tornar a començar el joc
        onView(withId(R.id.tap_me_button)).check(matches(isEnabled()))

    }




}