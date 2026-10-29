package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.data.model.DailyRoutineEntity
import com.example.localization.AppLanguage
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExportManager {

    fun getDefaultBackupFileName(): String {
        val dateStr = SimpleDateFormat("yyyyMMdd_HHmm", Locale.US).format(Date())
        return "chintan_sarani_backup_$dateStr.csv"
    }

    fun generateCsv(entries: List<DailyRoutineEntity>, language: AppLanguage): String {
        val sb = StringBuilder()
        // UTF-8 BOM for Excel and spreadsheet viewers compatibility
        sb.append('\uFEFF')

        // CSV Header with bilingual column titles
        val headers = listOf(
            "दिनांक (Date)",
            "कुलदेवी नामजप (Kuldevi Naamjap)",
            "श्री गुरुदेव दत्त नामजप (Shri Gurudev Datta Naamjap)",
            "एकूण नामजप (Total Naamjap)",
            "कापूर इत्र उपाय (Camphor & Attar)",
            "प्रार्थना (Prayers)",
            "कृतज्ञता (Gratitude)",
            "आवरण काढणे (Aavaran Removal)",
            "भावजागृती चे प्रयत्न (Bhavjagruti)",
            "आत्मनिवेदन (Atmanivedan)",
            "मनाचा आढावा होय/नाही (Mind Review Done)",
            "मनाचा आढावा संख्या (Mind Review Count)",
            "दैनंदिन लिखाण होय/नाही (Daily Journaling Done)",
            "दैनंदिन लिखाण संख्या (Daily Journaling Count)",
            "दैनिक ग्रंथ पठण होय/नाही (Holy Reading Done)",
            "दैनिक ग्रंथ पठण संख्या (Holy Reading Count)",
            "चुका विचारणे होय/नाही (Mistakes Shared Done)",
            "चुका विचारणे संख्या (Mistakes Shared Count)",
            "प्रायश्चित होय/नाही (Penance Done)",
            "प्रायश्चित संख्या (Penance Count)",
            "सारणी लिखाण (Sarani Likhan Done)",
            "चुका लिहिणे होय/नाही (Writing Mistakes Done)",
            "चुका लिहिणे संख्या (Writing Mistakes Count)",
            "स्वयंसूचना सत्र होय/नाही (Autosuggestion Done)",
            "स्वयंसूचना संख्या (Autosuggestion Count)",
            "क्षमायाचना ईश्वर व साधक (Forgiveness Done)",
            "पूर्ण सवयी (Completed Habits)",
            "पूर्णता टक्केवारी (Completion %)",
            "नोंद / आध्यात्मिक अनुभूती (Reflection Notes)"
        )
        sb.append(headers.joinToString(separator = ",", postfix = "\n") { escapeCsv(it) })

        // Data Rows
        val sortedEntries = entries.sortedByDescending { it.date }
        for (entry in sortedEntries) {
            val row = listOf(
                entry.date,
                entry.naamjapKuldevi.toString(),
                entry.naamjapGurudevDatta.toString(),
                entry.totalNaamjap.toString(),
                entry.kapurItrUpay.toString(),
                entry.prarthana.toString(),
                entry.krutajnata.toString(),
                entry.aavaranKadhne.toString(),
                entry.bhavjagruti.toString(),
                entry.aatmanivedan.toString(),
                formatBoolean(entry.manachaAadhavDone, language),
                entry.manachaAadhavCount.toString(),
                formatBoolean(entry.dainandinLikhanDone, language),
                entry.dainandinLikhanCount.toString(),
                formatBoolean(entry.granthPathanDone, language),
                entry.granthPathanCount.toString(),
                formatBoolean(entry.chukaVicharneDone, language),
                entry.chukaVicharneCount.toString(),
                formatBoolean(entry.prayashchittaDone, language),
                entry.prayashchittaCount.toString(),
                formatBoolean(entry.saraniLikhanDone, language),
                formatBoolean(entry.chukaLihineDone, language),
                entry.chukaLihineCount.toString(),
                formatBoolean(entry.swayamsuchanaDone, language),
                entry.swayamsuchanaCount.toString(),
                formatBoolean(entry.kshamayachanaDone, language),
                "${entry.completedHabitsCount}/${entry.totalHabitsCount}",
                "%.1f%%".format(entry.completionPercentage),
                entry.reflectionNotes
            )
            sb.append(row.joinToString(separator = ",", postfix = "\n") { escapeCsv(it) })
        }

        return sb.toString()
    }

    fun writeCsvToUri(context: Context, uri: Uri, csvContent: String): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(csvContent.toByteArray(Charsets.UTF_8))
                outputStream.flush()
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    fun shareCsvContent(context: Context, csvContent: String, language: AppLanguage) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, if (language == AppLanguage.MARATHI) "चिंतन सारणी दैनिक बॅकअप" else "Chintan Sarani Daily Routine Backup")
            putExtra(Intent.EXTRA_TEXT, csvContent)
            type = "text/csv"
        }
        val chooser = Intent.createChooser(sendIntent, if (language == AppLanguage.MARATHI) "CSV बॅकअप शेअर करा" else "Share CSV Backup")
        context.startActivity(chooser)
    }

    private fun escapeCsv(value: String): String {
        var str = value.replace("\"", "\"\"")
        if (str.contains(",") || str.contains("\n") || str.contains("\r") || str.contains("\"")) {
            str = "\"$str\""
        }
        return str
    }

    private fun formatBoolean(value: Boolean, language: AppLanguage): String {
        return if (value) {
            if (language == AppLanguage.MARATHI) "होय (Yes)" else "Yes"
        } else {
            if (language == AppLanguage.MARATHI) "नाही (No)" else "No"
        }
    }
}
