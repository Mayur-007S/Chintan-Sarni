package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_routine_entries")
data class DailyRoutineEntity(
    @PrimaryKey
    val date: String, // Format: YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),

    // --- Phase 1: Basic Spiritual Practice & Remedies ---
    // 1) नामजप : कुलदेवी आणि श्री गुरुदेव दत्त चा नामजप 2 different fields
    val naamjapKuldevi: Int = 0,
    val naamjapGurudevDatta: Int = 0,
    // 2) कापूर इत्र उपाय
    val kapurItrUpay: Int = 0,
    // 3) प्रार्थना
    val prarthana: Int = 0,
    // 4) कृतज्ञता
    val krutajnata: Int = 0,
    // 5) आवरण काढणे
    val aavaranKadhne: Int = 0,

    // --- Phase 2: Devotion & Reflection ---
    // 6) भावजागृती चे प्रयत्न
    val bhavjagruti: Int = 0,
    // 7) आत्मनिवेदन
    val aatmanivedan: Int = 0,
    // 8) मनाचा आढावा घेणे
    val manachaAadhavDone: Boolean = false,
    val manachaAadhavCount: Int = 0,
    // 9) दैनंदिन लिखाण
    val dainandinLikhanDone: Boolean = false,
    val dainandinLikhanCount: Int = 0,
    // 10) दैनिक ग्रंथ पठण
    val granthPathanDone: Boolean = false,
    val granthPathanCount: Int = 0,

    // --- Phase 3: Error Rectification & Humility ---
    // 11) चुका विचारणे व आपली चूक दुसऱ्यांना सांगणे
    val chukaVicharneDone: Boolean = false,
    val chukaVicharneCount: Int = 0,
    // 12) प्रायश्चित व शिक्षा घेणे
    val prayashchittaDone: Boolean = false,
    val prayashchittaCount: Int = 0,
    // 13) सारणी लिखाण
    val saraniLikhanDone: Boolean = false,
    // 14) चुका लिहिणे (Added after 13)
    val chukaLihineDone: Boolean = false,
    val chukaLihineCount: Int = 0,
    // 15) स्वयंसूचना सत्र
    val swayamsuchanaDone: Boolean = false,
    val swayamsuchanaCount: Int = 0,
    // 16) क्षमायाचना ईश्वर व साधक
    val kshamayachanaDone: Boolean = false,

    // Tracking progress
    val completedSteps: Int = 0, // 0, 1, 2, or 3
    val reflectionNotes: String = ""
) {
    val totalNaamjap: Int
        get() = naamjapKuldevi + naamjapGurudevDatta

    val completedHabitsCount: Int
        get() {
            var count = 0
            if (naamjapKuldevi > 0) count++
            if (naamjapGurudevDatta > 0) count++
            if (kapurItrUpay > 0) count++
            if (prarthana > 0) count++
            if (krutajnata > 0) count++
            if (aavaranKadhne > 0) count++
            if (bhavjagruti > 0) count++
            if (aatmanivedan > 0) count++
            if (manachaAadhavDone) count++
            if (dainandinLikhanDone) count++
            if (granthPathanDone) count++
            if (chukaVicharneDone) count++
            if (prayashchittaDone) count++
            if (saraniLikhanDone) count++
            if (chukaLihineDone) count++
            if (swayamsuchanaDone) count++
            if (kshamayachanaDone) count++
            return count
        }

    val totalHabitsCount: Int
        get() = 17

    val completionPercentage: Float
        get() = (completedHabitsCount.toFloat() / totalHabitsCount) * 100f
}
