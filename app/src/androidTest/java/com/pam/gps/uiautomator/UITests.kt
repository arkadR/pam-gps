package com.pam.gps.uiautomator


import androidx.test.uiautomator.*
import com.pam.gps.TrackerService
import org.junit.Assert
import org.junit.Test

class UITests : UIAutomatorTestBase() {

  @Test
  fun startTrackingButtonClicked_ServiceRunsInBackground() {
    //Arrange
    val fab: UiObject = device.findObject(
      UiSelector().resourceId("com.pam.gps:id/fab")
    )

    //Act
    fab.click()

    //Assert
    Assert.assertTrue(TrackerService.isRunning)
  }

  @Test
  fun startTrackingButtonClicked_NotificationAppears() {
    //Arrange
    val fab: UiObject = device.findObject(
      UiSelector().resourceId("com.pam.gps:id/fab")
    )

    //Act
    fab.click()
    device.openNotification()

    val notification: UiObject? = device.findObject(
      UiSelector().resourceId("android:id/status_bar_latest_event_content").childSelector(
        UiSelector().resourceId("android:id/notification_header").childSelector(
          UiSelector().resourceId("android:id/app_name_text").text("GPS")
        )
      )
    )

    //Assert
    Assert.assertTrue(notification != null)
  }

  @Test
  fun stopTrackingButtonClicked_NotificationDisappears() {
    //Arrange
    val fab: UiObject = device.findObject(
      UiSelector().resourceId("com.pam.gps:id/fab")
    )

    //Act
    fab.click()
    device.waitForWindowUpdate(PACKAGE_NAME, 2*1000)
    fab.click()
    device.waitForWindowUpdate(PACKAGE_NAME, 2*1000)

    //Assert
    val isFound = device.hasObject(
      By.res("android:id/app_name_text").text("GPS")
    )
    Assert.assertFalse(isFound)
  }
}