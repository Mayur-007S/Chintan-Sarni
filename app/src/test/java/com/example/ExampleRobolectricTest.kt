package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleRobolectricTest {

  @Test
  fun `app basic arithmetic and logic verification`() {
    val totalTimeMinutes = 30 + 10
    assertEquals(40, totalTimeMinutes)
    assertTrue(totalTimeMinutes > 0)
  }
}



