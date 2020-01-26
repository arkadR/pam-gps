package com.pam.gps


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class SignInTest {

  @Rule
  @JvmField
  var activityTestRule = ActivityTestRule(MainActivity::class.java)

  @Test
  fun signInTest() {
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//    activityTestRule.launchActivity(null)
    Thread.sleep(7000)

    for (i in 0..1) {
      UiDevice
        .getInstance(InstrumentationRegistry.getInstrumentation())
        .findObject(UiSelector().resourceId("com.android.packageinstaller:id/permission_allow_button"))
        .click()
      Thread.sleep(500)
    }

    val button = onView(withId(R.id.sign_in_button))
    val email = onView(withId(R.id.email_text))
    val password = onView(withId(R.id.password_text))

    button.perform(click())

    val emailErrorText = UiDevice
      .getInstance(InstrumentationRegistry.getInstrumentation())
      .findObject(UiSelector().resourceId("com.pam.gps:id/textinput_error"))
      .text

    assert(emailErrorText != "")

    email.perform(replaceText("michallengel@gmail.com"), closeSoftKeyboard())

    button.perform(click())

    val passwordErrorText = UiDevice
      .getInstance(InstrumentationRegistry.getInstrumentation())
      .findObject(UiSelector().resourceId("com.pam.gps:id/textinput_error"))
      .text

    assert(passwordErrorText != "")

    password.perform(replaceText("zimneburaki"), closeSoftKeyboard())

    button.perform(click())

    Thread.sleep(7000)

    onView(withId(R.id.home_tab_layout))
      .check(ViewAssertions.matches(allOf(isDisplayed())))
  }
}