# Chintan Sarani (चिंतन सारणी) - Daily Spiritual Routine & Habit Tracker

A comprehensive daily spiritual checkup and introspection tracker that empowers seekers to record their 16 daily spiritual practices through an intuitive 3-stage guided wizard, monitor progress with week and month analytics, customize personal targets, and switch seamlessly between Marathi and English.

## User Review & Critical Decisions

> [!IMPORTANT]
> Based on your preferences from Phase 1 and revision feedback:
> - **New Point Added**: Added **"चुका लिहिणे" (Writing Mistakes)** immediately after Point 13 (सारणी लिखाण) with `Yes/No` toggle and integer count.
> - **Data Storage**: Fully offline-first device-local **Room Database** for high performance, zero network delay, absolute privacy, and instant data persistence.
> - **Entry Workflow**: A **3-stage guided wizard** with step progression (`Next` buttons after Stage 1 and Stage 2, and a comprehensive summary on Stage 3 before final submission).
> - **Language System**: Dynamic **full app language switcher** allowing one-tap switching between Marathi (मराठी) and English across all forms, analytics charts, tabs, and settings.
> - **Dual Input Structure**: Specialized smart input components for fields that support `Yes/No` toggle with an optional positive integer count when marked as `Yes`.

---

## 1. Overview & Core Concept

**Chintan Sarani (चिंतन सारणी)** is designed to assist seekers (साधक) in practicing disciplined spiritual introspection, daily chanting (*naamjap*), spiritual remedies (*upay*), reflection (*manaacha aadhav*), error rectification (*chuka vicharne / lihinye*), and autosuggestion (*swayamsuchana*). 

### Complete 16-Point Checklist Structure

- **Phase 1: Basic Spiritual Practice & Remedies (पायाभूत साधना व उपाय)**:
  1. **नामजप (Naamjap)**: 
     - कुलदेवी नामजप (Kuldevi Naamjap) [Integer Count]
     - श्री गुरुदेव दत्त नामजप (Shri Gurudev Datta Naamjap) [Integer Count]
  2. **कापूर इत्र उपाय (Camphor & Attar Remedy)** [Integer Count]
  3. **प्रार्थना (Prayer / Prarthana)** [Integer Count]
  4. **कृतज्ञता (Gratitude / Krutajnata)** [Integer Count]
  5. **आवरण काढणे (Removing Spiritual Black Covering / Aavaran Kadhne)** [Integer Count]
  *(Submit Stage 1 to proceed to Stage 2)*

- **Phase 2: Devotion & Inner Reflection (भाव व अंतर्मुखता)**:
  6. **भावजागृती चे प्रयत्न (Efforts for Awakening Spiritual Emotion / Bhavjagruti)** [Integer Count]
  7. **आत्मनिवेदन (Self-Submission / Heartfelt Confession / Aatmanivedan)** [Integer Count]
  8. **मनाचा आढावा घेणे (Review of Mind / Manaacha Aadhav)** [Yes/No Boolean + Integer Count]
  9. **दैनंदिन लिखाण (Daily Journaling / Dainandin Likhan)** [Yes/No Boolean + Integer Count]
  10. **दैनिक ग्रंथ पठण (Daily Holy Scripture Reading / Granth Pathan)** [Yes/No Boolean + Integer Count]
  *(Submit Stage 2 to proceed to Stage 3)*

- **Phase 3: Error Rectification, Penance & Humility (दोष निर्मूलन, प्रायश्चित व साधना)**:
  11. **चुका विचारणे व आपली चूक दुसऱ्यांना सांगणे (Asking Mistakes & Telling Mistakes to Others)** [Yes/No Boolean + Integer Count]
  12. **प्रायश्चित व शिक्षा घेणे (Repentance & Self-Imposed Penance / Prayashchitta)** [Yes/No Boolean + Integer Count]
  13. **सारणी लिखाण (Table / Sarani Writing Completed)** [Yes/No Boolean]
  14. **चुका लिहिणे (Writing Down Mistakes / Writing Errors)** [Yes/No Boolean + Integer Count] *(Newly Added)*
  15. **स्वयंसूचना सत्र (Autosuggestion Sessions / Swayamsuchana Satra)** [Yes/No Boolean + Integer Count]
  16. **क्षमायाचना ईश्वर व साधक (Seeking Forgiveness from God & Seekers)** [Yes/No Boolean]

---

## 2. User Experience & Visual Design

### Visual Identity & Theme
- **Aesthetic Direction**: Serene, sacred, and grounded. Drawing inspiration from traditional Indian spiritual design with warm saffron/ochre accents, clean warm sandalwood tones, and high-contrast readable typography.
- **Color Palette**:
  - *Primary*: Deep Saffron Amber (`#C85A17` / `#E6732B`) symbolizing dedication, spirituality, and clarity.
  - *Secondary / Accent*: Warm Sandalwood Gold (`#D4A373`) and Deep Teak Wood (`#3E2723`).
  - *Background / Surface*: Soft parchment ivory (`#FBF8F2`) in light mode; deep warm twilight charcoal (`#1A1615`) in dark mode.
  - *Success / Completion*: Auspicious Tulsi Green (`#2E7D32`) for completed habits and streaks.
- **Typography & Formatting**: Clean, legible bilingual styling. Devanagari text rendered with crisp weights and generous line heights, accompanied by English translations.
- **Interactive Micro-Interactions**:
  - Step transitions with smooth sliding animation between Wizard Steps 1, 2, and 3.
  - Quick count steppers (`+`, `-`, preset increments like `+1`, `+5`, `+108`) for fast effortless data entry during chanting and prayer counting.
  - Switch pill buttons for `Yes / No` with automatic animated reveal of count text fields.

---

## 3. Key Product Decisions & Trade-Offs

| Decision | Selected Approach | Rationale & Trade-Offs |
|---|---|---|
| **Data Persistence** | Room Database (SQLite via AndroidX Room) | Fast local reads, zero latency, 100% offline functionality, and no authentication hurdles for personal spiritual entries. |
| **Bilingual Language Switch** | In-app reactive language state (`AppLanguage.MARATHI` / `AppLanguage.ENGLISH`) | Instant dynamic translation across all screens without requiring system language changes or app restart. |
| **Daily Entry Identification** | Unique entry per date string (`YYYY-MM-DD`) | Ensures one canonical record per day that can be created, updated incrementally across phases, or edited later. |
| **Dual-field Yes/No with Count** | Structured fields holding `hasDone: Boolean` and `count: Int` | Cleanly models items 8, 9, 10, 11, 12, 14 (चुका लिहिणे), and 15 (स्वयंसूचना सत्र) without ambiguous null values. |
| **Progressive Step Wizard** | Persistent draft state in ViewModel | Users can submit Stage 1, continue immediately or return later to complete Stage 2 and 3 without losing in-progress data. |

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                          Jetpack Compose UI                            │
│  ┌─────────────────┐  ┌──────────────────┐  ┌───────────────────────┐  │
│  │   Daily Entry   │  │  Analytics &     │  │  History & Calendar   │  │
│  │  3-Step Wizard  │  │  Progress Charts │  │  Past Day Review      │  │
│  └────────┬────────┘  └────────┬─────────┘  └───────────┬───────────┘  │
│           │                    │                        │              │
│           └────────────────────┼────────────────────────┘              │
│                                ▼                                       │
│                    ChintanSaraniViewModel                              │
│              (StateFlow, WizardDraft, AppLanguage)                     │
│                                │                                       │
│                                ▼                                       │
│                    ChintanSaraniRepository                             │
│                                │                                       │
│                                ▼                                       │
│                      Room Database & DAO                               │
│              (DailyRoutineEntity & HabitTargetEntity)                  │
└────────────────────────────────────────────────────────────────────────┘
```

### Data Schema (Room Entities)

```kotlin
@Entity(tableName = "daily_routine_entries")
data class DailyRoutineEntity(
    @PrimaryKey val date: String, // Format: YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    
    // Step 1: Basic Practices & Remedies
    val naamjapKuldevi: Int = 0,
    val naamjapGurudevDatta: Int = 0,
    val kapurItrUpay: Int = 0,
    val prarthana: Int = 0,
    val krutajnata: Int = 0,
    val aavaranKadhne: Int = 0,
    
    // Step 2: Devotion & Reflection
    val bhavjagruti: Int = 0,
    val aatmanivedan: Int = 0,
    val manachaAadhavDone: Boolean = false,
    val manachaAadhavCount: Int = 0,
    val dainandinLikhanDone: Boolean = false,
    val dainandinLikhanCount: Int = 0,
    val granthPathanDone: Boolean = false,
    val granthPathanCount: Int = 0,
    
    // Step 3: Rectification & Humility
    val chukaVicharneDone: Boolean = false,
    val chukaVicharneCount: Int = 0,
    val prayashchittaDone: Boolean = false,
    val prayashchittaCount: Int = 0,
    val saraniLikhanDone: Boolean = false,
    val chukaLihineDone: Boolean = false,       // <-- Point 14 (चुका लिहिणे)
    val chukaLihineCount: Int = 0,             // <-- Count of mistakes written
    val swayamsuchanaDone: Boolean = false,
    val swayamsuchanaCount: Int = 0,
    val kshamayachanaDone: Boolean = false,
    
    val completedSteps: Int = 0 // 1, 2, or 3
)
```

---

## 5. Execution Roadmap

1. **Database & Repository Layer**:
   - Create Room Entities, DAOs, and repository with reactive `Flow` queries for today's entry, 7-day window, 30-day window, and date-specific queries.
2. **Bilingual Localization Support**:
   - Provide comprehensive Marathi and English string keys, titles, descriptions, and habit helper texts for all 16 items.
3. **Daily Routine 3-Step Wizard**:
   - Step 1: Spiritual remedy & naamjap counters with quick-increment buttons.
   - Step 2: Reflection and reading entries with interactive Yes/No switches and count fields.
   - Step 3: Rectification, Sarani confirmation, writing mistakes (चुका लिहिणे), autosuggestions, and final review with celebratory completion badge.
4. **Analytics & Progress Dashboard**:
   - Interactive Compose Canvas Bar Charts for 7-day chanting trends.
   - Category completion rings and monthly averages.
   - Habit target adjustment sheet.
5. **History & Calendar Navigator**:
   - Day selector to view or update any past routine record.
   - Comprehensive **Search & Filter feature**:
     - Fast date search (supports partial dates, e.g. "2026-10", "30", "2026-10-30").
     - Search by specific practice name in Marathi & English (e.g. "नामजप" / "Naamjap", "प्रार्थना" / "Prayer", "कापूर" / "Camphor", "ग्रंथ" / "Reading", "चुका" / "Mistakes").
     - Horizontal scrolling filter chips for all 17 spiritual practices (All, Naamjap, Camphor, Prayers, Gratitude, Aavaran, Bhavjagruti, Surrender, Mind Review, Holy Text, Journaling, Admitting Faults, Penance, Sarani, Writing Mistakes, Autosuggestion, Forgiveness).
     - Live result counter ("X of Y entries") and instant reset button.
     - Custom highlight badges in history item cards for the filtered practice.
     - Empty state with reset filter button when no entries match.
     - Clickable items to open full 17-practice detail bottom sheet with edit capability.
6. **Compilation & Polish**:
   - Verify smooth edge-to-edge rendering, Material 3 theming, touch target guidelines, and compile clean.

