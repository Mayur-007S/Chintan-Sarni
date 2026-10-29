package com.example

import com.example.data.model.DailyRoutineEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testDailyRoutineEntity_computations() {
        val routine = DailyRoutineEntity(
            date = "2026-10-29",
            naamjapKuldevi = 1000,
            naamjapGurudevDatta = 1000,
            kapurItrUpay = 2,
            prarthana = 5,
            krutajnata = 5,
            aavaranKadhne = 1,
            bhavjagruti = 3,
            aatmanivedan = 2,
            manachaAadhavDone = true,
            manachaAadhavCount = 1,
            dainandinLikhanDone = true,
            dainandinLikhanCount = 1,
            granthPathanDone = true,
            granthPathanCount = 5,
            chukaVicharneDone = true,
            chukaVicharneCount = 1,
            prayashchittaDone = true,
            prayashchittaCount = 1,
            saraniLikhanDone = true,
            chukaLihineDone = true,
            chukaLihineCount = 3,
            swayamsuchanaDone = true,
            swayamsuchanaCount = 3,
            kshamayachanaDone = true
        )

        assertEquals(2000, routine.totalNaamjap)
        assertEquals(17, routine.completedHabitsCount)
        assertEquals(100f, routine.completionPercentage, 0.01f)
        assertTrue(routine.chukaLihineDone)
        assertEquals(3, routine.chukaLihineCount)
    }
}
