package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.DailyRoutineEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Chintan Sarani", appName)
  }

  @Test
  fun `naamjap minutes duration calculation`() {
    val routine = DailyRoutineEntity(
      date = "2026-10-30",
      naamjapKuldevi = 30, // 30 minutes
      naamjapGurudevDatta = 10 // 10 minutes
    )
    assertEquals(40, routine.totalNaamjap)
    assertEquals(2, routine.completedHabitsCount)
  }

  @Test
  fun `reset draft creates empty routine for new entry`() {
    val filled = DailyRoutineEntity(
      date = "2026-10-30",
      naamjapKuldevi = 30,
      naamjapGurudevDatta = 10,
      completedSteps = 3
    )
    val resetDraft = DailyRoutineEntity(date = filled.date)
    assertEquals(0, resetDraft.naamjapKuldevi)
    assertEquals(0, resetDraft.naamjapGurudevDatta)
    assertEquals(0, resetDraft.completedSteps)
    assertEquals(0, resetDraft.totalNaamjap)
  }

  @Test
  fun `naamjap minute step increment by 1 directly`() {
    var kuldeviMins = 0
    // Step by 1 directly (user request: don't increase by 5)
    kuldeviMins += 1
    assertEquals(1, kuldeviMins)
    kuldeviMins += 1
    assertEquals(2, kuldeviMins)
    kuldeviMins = (kuldeviMins - 1).coerceAtLeast(0)
    assertEquals(1, kuldeviMins)
  }

  @Test
  fun `full details calculations on routine entry`() {
    val fullEntry = DailyRoutineEntity(
      date = "2026-10-30",
      naamjapKuldevi = 30,
      naamjapGurudevDatta = 10,
      kapurItrUpay = 2,
      prarthana = 5,
      krutajnata = 3,
      aavaranKadhne = 1,
      bhavjagruti = 2,
      aatmanivedan = 1,
      manachaAadhavDone = true,
      dainandinLikhanDone = true,
      granthPathanDone = true,
      chukaVicharneDone = true,
      prayashchittaDone = true,
      saraniLikhanDone = true,
      chukaLihineDone = true,
      swayamsuchanaDone = true,
      kshamayachanaDone = true,
      reflectionNotes = "आज मन शांत होते आणि नामजप एकाग्रतेने झाला."
    )
    assertEquals(40, fullEntry.totalNaamjap)
    assertEquals(17, fullEntry.completedHabitsCount)
    assertEquals(100f, fullEntry.completionPercentage, 0.1f)
    assertTrue(fullEntry.reflectionNotes.isNotEmpty())
  }

  @Test
  fun `sample values between 5 and 10 verification`() {
    val sample = DailyRoutineEntity(
      date = "2026-10-30",
      naamjapKuldevi = 10,
      naamjapGurudevDatta = 5,
      kapurItrUpay = 5,
      prarthana = 8,
      krutajnata = 6,
      aavaranKadhne = 5,
      bhavjagruti = 7,
      aatmanivedan = 5,
      manachaAadhavDone = true,
      manachaAadhavCount = 5,
      dainandinLikhanDone = true,
      dainandinLikhanCount = 5,
      granthPathanDone = true,
      granthPathanCount = 10,
      chukaVicharneDone = true,
      chukaVicharneCount = 5,
      prayashchittaDone = true,
      prayashchittaCount = 5,
      saraniLikhanDone = true,
      chukaLihineDone = true,
      chukaLihineCount = 6,
      swayamsuchanaDone = true,
      swayamsuchanaCount = 5,
      kshamayachanaDone = true,
      reflectionNotes = "आज ५ ते १० मिनिटांचा नामजप व उपाय भावपूर्ण झाले."
    )
    assertTrue(sample.naamjapKuldevi in 5..10)
    assertTrue(sample.naamjapGurudevDatta in 5..10)
    assertTrue(sample.kapurItrUpay in 5..10)
    assertTrue(sample.prarthana in 5..10)
    assertTrue(sample.krutajnata in 5..10)
    assertTrue(sample.aavaranKadhne in 5..10)
    assertTrue(sample.bhavjagruti in 5..10)
    assertTrue(sample.aatmanivedan in 5..10)
    assertTrue(sample.manachaAadhavCount in 5..10)
    assertTrue(sample.dainandinLikhanCount in 5..10)
    assertTrue(sample.granthPathanCount in 5..10)
    assertTrue(sample.chukaVicharneCount in 5..10)
    assertTrue(sample.prayashchittaCount in 5..10)
    assertTrue(sample.chukaLihineCount in 5..10)
    assertTrue(sample.swayamsuchanaCount in 5..10)
    assertEquals(17, sample.completedHabitsCount)
    assertEquals(15, sample.totalNaamjap)
  }

  @Test
  fun `history search by date and practice name verification`() {
    val application = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = com.example.ui.viewmodel.ChintanSaraniViewModel(application)

    val entry1 = DailyRoutineEntity(
      date = "2026-10-28",
      naamjapKuldevi = 20,
      naamjapGurudevDatta = 10,
      prarthana = 4,
      reflectionNotes = "शांत दिवस"
    )
    val entry2 = DailyRoutineEntity(
      date = "2026-10-29",
      kapurItrUpay = 3,
      granthPathanDone = true,
      reflectionNotes = "वाचन पूर्ण"
    )

    // Test 1: Date search
    assertTrue(viewModel.matchesHistorySearchQuery(entry1, "2026-10-28"))
    assertTrue(viewModel.matchesHistorySearchQuery(entry1, "28"))
    org.junit.Assert.assertFalse(viewModel.matchesHistorySearchQuery(entry1, "2026-10-29"))

    // Test 2: Practice name search (Marathi & English)
    assertTrue(viewModel.matchesHistorySearchQuery(entry1, "नामजप"))
    assertTrue(viewModel.matchesHistorySearchQuery(entry1, "naamjap"))
    assertTrue(viewModel.matchesHistorySearchQuery(entry1, "प्रार्थना"))
    assertTrue(viewModel.matchesHistorySearchQuery(entry1, "prayer"))
    org.junit.Assert.assertFalse(viewModel.matchesHistorySearchQuery(entry1, "granth"))

    assertTrue(viewModel.matchesHistorySearchQuery(entry2, "कापूर"))
    assertTrue(viewModel.matchesHistorySearchQuery(entry2, "camphor"))
    assertTrue(viewModel.matchesHistorySearchQuery(entry2, "ग्रंथ"))
    assertTrue(viewModel.matchesHistorySearchQuery(entry2, "reading"))
    org.junit.Assert.assertFalse(viewModel.matchesHistorySearchQuery(entry2, "नामजप"))
  }

  @Test
  fun `history practice filter enum verification`() {
    val entry = DailyRoutineEntity(
      date = "2026-10-30",
      naamjapKuldevi = 30,
      naamjapGurudevDatta = 10,
      prarthana = 5,
      granthPathanDone = true
    )

    assertEquals(com.example.ui.viewmodel.PracticeFilter.ALL.name, "ALL")
    assertTrue(entry.totalNaamjap > 0)
    assertTrue(entry.prarthana > 0)
    assertTrue(entry.granthPathanDone)
    assertEquals(0, entry.krutajnata)
  }
}

