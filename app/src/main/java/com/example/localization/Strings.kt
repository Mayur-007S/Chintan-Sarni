package com.example.localization

object AppStrings {

    fun appTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "चिंतन सारणी"
        AppLanguage.ENGLISH -> "Chintan Sarani"
    }

    fun appSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "दैनिक साधना व स्व-चिंतन दैनंदिनी"
        AppLanguage.ENGLISH -> "Daily Spiritual Routine & Introspection"
    }

    // Tabs
    fun tabDaily(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "दैनिक सारणी"
        AppLanguage.ENGLISH -> "Daily Routine"
    }

    fun tabWeeklyCharts(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "साप्ताहिक प्रगती"
        AppLanguage.ENGLISH -> "Weekly Charts"
    }

    fun tabAnalytics(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "प्रगती व आलेख"
        AppLanguage.ENGLISH -> "Analytics"
    }

    fun tabHistory(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "इतिहास"
        AppLanguage.ENGLISH -> "History"
    }

    fun tabGoals(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "ध्येय व सवयी"
        AppLanguage.ENGLISH -> "Goals & Habits"
    }

    // Step Headers
    fun step1Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "टप्पा १: पायाभूत साधना व उपाय"
        AppLanguage.ENGLISH -> "Phase 1: Basic Practices & Remedies"
    }

    fun step1Subtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "नामजप, उपाय, प्रार्थना, कृतज्ञता व आवरण काढणे"
        AppLanguage.ENGLISH -> "Naamjap, Remedies, Prayers, Gratitude & Aavaran"
    }

    fun step2Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "टप्पा २: भाव व अंतर्मुखता"
        AppLanguage.ENGLISH -> "Phase 2: Devotion & Reflection"
    }

    fun step2Subtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "भावजागृती, आत्मनिवेदन, मनाचा आढावा व ग्रंथ पठण"
        AppLanguage.ENGLISH -> "Bhavjagruti, Confession, Mind Review & Reading"
    }

    fun step3Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "टप्पा ३: दोष निर्मूलन व साधना"
        AppLanguage.ENGLISH -> "Phase 3: Rectification & Humility"
    }

    fun step3Subtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "चुका सांगणे, प्रायश्चित, सारणी, चुका लिहिणे व क्षमायाचना"
        AppLanguage.ENGLISH -> "Mistakes, Penance, Sarani, Writing Errors & Forgiveness"
    }

    // 16 Items
    fun item1Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "१) नामजप (कुलदेवी आणि श्री गुरुदेव दत्त)"
        AppLanguage.ENGLISH -> "1) Naamjap (Kuldevi & Shri Gurudev Datta)"
    }
    fun kuldeviName(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "कुलदेवी नामजप"
        AppLanguage.ENGLISH -> "Kuldevi Naamjap"
    }
    fun gurudevDattaName(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "श्री गुरुदेव दत्त नामजप"
        AppLanguage.ENGLISH -> "Shri Gurudev Datta Naamjap"
    }

    fun item2Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "२) कापूर इत्र उपाय"
        AppLanguage.ENGLISH -> "2) Camphor & Attar Remedy (कापूर इत्र)"
    }
    fun item2Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "दिवसभरात केलेले कापूर किंवा अत्तर उपाय (वेळा / संख्या)"
        AppLanguage.ENGLISH -> "Camphor / scented oil remedies performed (count/times)"
    }

    fun item3Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "३) प्रार्थना"
        AppLanguage.ENGLISH -> "3) Prayers (प्रार्थना)"
    }
    fun item3Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "कृतीपूर्वी व दिवसभरातील भावपूर्ण प्रार्थनांची संख्या"
        AppLanguage.ENGLISH -> "Heartfelt prayers offered before activities and during day"
    }

    fun item4Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "४) कृतज्ञता"
        AppLanguage.ENGLISH -> "4) Gratitude (कृतज्ञता)"
    }
    fun item4Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "ईश्वराप्रती व्यक्त केलेल्या कृतज्ञतेची संख्या"
        AppLanguage.ENGLISH -> "Expressions of gratitude expressed towards God"
    }

    fun item5Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "५) आवरण काढणे"
        AppLanguage.ENGLISH -> "5) Removing Black Covering (आवरण काढणे)"
    }
    fun item5Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "अनिष्ट शक्तींचे आवरण काढण्याचे उपाय सत्र (वेळा)"
        AppLanguage.ENGLISH -> "Spiritual black covering removal sessions (times)"
    }

    fun item6Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "६) भावजागृती चे प्रयत्न"
        AppLanguage.ENGLISH -> "6) Efforts for Bhavjagruti (भावजागृती)"
    }
    fun item6Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "दिवसभरात भाववृद्धीसाठी केलेले विशेष प्रयत्न (वेळा)"
        AppLanguage.ENGLISH -> "Conscious efforts made to awaken devotion/spiritual emotion"
    }

    fun item7Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "७) आत्मनिवेदन"
        AppLanguage.ENGLISH -> "7) Self-Submission (आत्मनिवेदन)"
    }
    fun item7Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "ईश्वराशी साधलेला मनमोकळा संवाद व आत्मनिवेदन (वेळा)"
        AppLanguage.ENGLISH -> "Heart-to-heart communication and surrender to God"
    }

    fun item8Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "८) मनाचा आढावा घेणे"
        AppLanguage.ENGLISH -> "8) Review of Mind (मनाचा आढावा घेणे)"
    }
    fun item8Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "मनाची स्थिती व विचारांचे निरीक्षण केले का?"
        AppLanguage.ENGLISH -> "Did you review the state of your mind and thoughts?"
    }

    fun item9Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "९) दैनंदिन लिखाण"
        AppLanguage.ENGLISH -> "9) Daily Journaling (दैनंदिन लिखाण)"
    }
    fun item9Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "दिवसाची दैनंदिनी किंवा आध्यात्मिक नोंदी केल्या का?"
        AppLanguage.ENGLISH -> "Did you write daily spiritual notes or diary entries?"
    }

    fun item10Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "१०) दैनिक ग्रंथ पठण"
        AppLanguage.ENGLISH -> "10) Daily Holy Text Reading (दैनिक ग्रंथ पठण)"
    }
    fun item10Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "ग्रंथ, पोथी किंवा आध्यात्मिक वाचन केले का?"
        AppLanguage.ENGLISH -> "Did you read sacred texts or spiritual literature?"
    }

    fun item11Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "११) चुका विचारणे व आपली चूक दुसऱ्यांना सांगणे"
        AppLanguage.ENGLISH -> "11) Asking Mistakes & Admitting to Others"
    }
    fun item11Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "स्वतःच्या चुका इतरांना विचारल्या किंवा प्रांजळपणे सांगितल्या का?"
        AppLanguage.ENGLISH -> "Did you ask others about your faults or openly admit them?"
    }

    fun item12Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "१२) प्रायश्चित व शिक्षा घेणे"
        AppLanguage.ENGLISH -> "12) Self-Correction & Penance (प्रायश्चित)"
    }
    fun item12Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "झालेल्या चुकीचे प्रायश्चित किंवा आत्म-शिक्षा घेतली का?"
        AppLanguage.ENGLISH -> "Did you take penance/self-correction for committed mistakes?"
    }

    fun item13Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "१३) सारणी लिखाण"
        AppLanguage.ENGLISH -> "13) Sarani Writing (सारणी लिखाण)"
    }
    fun item13Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "आजची चिंतन सारणी संपूर्ण भरली आहे का?"
        AppLanguage.ENGLISH -> "Has today's introspection chart been filled completely?"
    }

    fun item14Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "१४) चुका लिहिणे"
        AppLanguage.ENGLISH -> "14) Writing Down Mistakes (चुका लिहिणे)"
    }
    fun item14Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "दिवसभरातील झालेल्या चुका वहीत किंवा सारणीत लिहिल्या का?"
        AppLanguage.ENGLISH -> "Did you write down the mistakes committed today?"
    }

    fun item15Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "१५) स्वयंसूचना सत्र"
        AppLanguage.ENGLISH -> "15) Autosuggestion Sessions (स्वयंसूचना सत्र)"
    }
    fun item15Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "दोष निर्मूलनासाठी स्वयंसूचना सत्रे केली का?"
        AppLanguage.ENGLISH -> "Did you perform autosuggestion sessions for defect removal?"
    }

    fun item16Title(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "१६) क्षमायाचना ईश्वर व साधक"
        AppLanguage.ENGLISH -> "16) Forgiveness from God & Seekers (क्षमायाचना)"
    }
    fun item16Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "ईश्वराची व सहसाधकांची नम्रतेने क्षमा मागितली का?"
        AppLanguage.ENGLISH -> "Did you humbly seek forgiveness from God and fellow seekers?"
    }

    // Common Controls
    fun yesLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "होय"
        AppLanguage.ENGLISH -> "Yes"
    }

    fun noLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "नाही"
        AppLanguage.ENGLISH -> "No"
    }

    fun countLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "संख्या (Count)"
        AppLanguage.ENGLISH -> "Count"
    }

    fun nextStep(lang: AppLanguage, stepNumber: Int) = when (lang) {
        AppLanguage.MARATHI -> "टप्पा $stepNumber वर जा (Next)"
        AppLanguage.ENGLISH -> "Proceed to Step $stepNumber"
    }

    fun previousStep(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "मागे (Previous)"
        AppLanguage.ENGLISH -> "Previous Step"
    }

    fun saveTodayEntry(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "सारणी जतन करा (Save Routine)"
        AppLanguage.ENGLISH -> "Save Daily Routine"
    }

    fun savedSuccess(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "आजची चिंतन सारणी यशस्वीरीत्या जतन झाली!"
        AppLanguage.ENGLISH -> "Daily Chintan Sarani saved successfully!"
    }

    fun todayLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "आज"
        AppLanguage.ENGLISH -> "Today"
    }

    fun yesterdayLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "काल"
        AppLanguage.ENGLISH -> "Yesterday"
    }

    fun weeklyAvg(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "या आठवड्यातील सरासरी"
        AppLanguage.ENGLISH -> "This Week's Average"
    }

    fun monthlyAvg(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "या महिन्यातील सरासरी"
        AppLanguage.ENGLISH -> "This Month's Average"
    }

    fun chantingTotal(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "एकूण नामजप"
        AppLanguage.ENGLISH -> "Total Naamjap"
    }

    fun completionScore(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "साधना पूर्णता प्रमाण"
        AppLanguage.ENGLISH -> "Routine Completion"
    }

    // Weekly Charts Strings
    fun weeklyChartsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "साप्ताहिक प्रगती आलेख"
        AppLanguage.ENGLISH -> "Weekly Progress Charts"
    }

    fun weeklyChartsSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "संख्यात्मक सवयींचा रेषा आलेख आणि सत्य/असत्य सवयींचा स्तंभ आलेख"
        AppLanguage.ENGLISH -> "Line graph for integer habits and bar chart for boolean ones"
    }

    fun lineChartHeading(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "संख्यात्मक साधना (Line Graph)"
        AppLanguage.ENGLISH -> "Integer Practices (Line Graph)"
    }

    fun barChartHeading(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "सत्य/असत्य सवयी (Bar Chart)"
        AppLanguage.ENGLISH -> "Boolean Habits (Bar Chart)"
    }

    fun selectPractice(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "साधना निवडा"
        AppLanguage.ENGLISH -> "Select Practice"
    }

    fun weekTotalLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "आठवडा एकूण"
        AppLanguage.ENGLISH -> "Week Total"
    }

    fun dailyAverageLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "दैनिक सरासरी"
        AppLanguage.ENGLISH -> "Daily Avg"
    }

    fun completedDaysLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "यशस्वी दिवस"
        AppLanguage.ENGLISH -> "Completed Days"
    }

    fun previousWeek(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "मागील आठवडा"
        AppLanguage.ENGLISH -> "Prev Week"
    }

    fun nextWeek(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "पुढील आठवडा"
        AppLanguage.ENGLISH -> "Next Week"
    }

    fun currentWeek(lang: AppLanguage) = when (lang) {
        AppLanguage.MARATHI -> "चालू आठवडा"
        AppLanguage.ENGLISH -> "Current Week"
    }
}
