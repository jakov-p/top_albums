package com.music.topalbums


import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.CoreMatchers.any
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import java.util.concurrent.TimeoutException

/**
 * A [ViewAction] that waits up to [timeout] milliseconds for a [View] to meet a condition [condition].
 */
class WaitUntilConditionMet(private val condition:(view: View )->Boolean, private val timeout: Long, ) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return any(View::class.java)
    }

    override fun getDescription(): String {
        return "wait up to $timeout milliseconds for the view to satisfy the condition"
    }

    override fun perform(uiController: UiController, view: View) {

        uiController.loopMainThreadUntilIdle()
        val endTime = System.currentTimeMillis() + timeout

        do {
            if (condition.invoke(view)) {
                return
            }
            uiController.loopMainThreadForAtLeast(50)
        }
        while (System.currentTimeMillis() < endTime)

        throw PerformException.Builder()
            .withActionDescription(description)
            .withCause(TimeoutException("Waited $timeout milliseconds"))
            .withViewDescription(HumanReadables.describe(view))
            .build()
    }
}


fun waitUntilVisible(timeout: Long): ViewAction {
    return WaitUntilConditionMet({it.visibility == View.VISIBLE}, timeout)
}


fun waitUntilEnabled(timeout: Long): ViewAction {
    return WaitUntilConditionMet({it.isEnabled == true}, timeout)
}

fun waitUntilDisabled(timeout: Long): ViewAction {
    return WaitUntilConditionMet({it.isEnabled == false}, timeout)
}

fun waitUntilGone(timeout: Long): ViewAction {
    return WaitUntilConditionMet({it.visibility == View.GONE}, timeout)
}


fun waitForViewToDisappear(view: Matcher<View>, maxWaitingTimeMs: Long)
{
    val endTime = System.currentTimeMillis() + maxWaitingTimeMs
    while (System.currentTimeMillis() <= endTime)
    {
        try
        {
            onView(allOf(view, isDisplayed())).check(matches(not(doesNotExist())))
        }
        catch (ex: NoMatchingViewException)
        {
            return  // view has disappeared
        }
    }
    throw RuntimeException("timeout exceeded") // or whatever exception you want
}