package com.example.ui.screens

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DailyRoutineEntity
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.components.BooleanCounterItemCard
import com.example.ui.components.BooleanOnlyItemCard
import com.example.ui.components.CounterItemCard
import com.example.ui.components.RoutineDetailBottomSheet
import com.example.ui.components.SubmittedEntriesSection
import com.example.ui.components.WizardProgressHeader
import com.example.ui.theme.ChaitanyaBlueDark
import com.example.ui.theme.ChaitanyaBluePrimary
import com.example.ui.theme.ChaitanyaCardBorder
import com.example.ui.theme.ChaitanyaSkyContainer
import com.example.ui.theme.ChaitanyaTextSecondary
import com.example.ui.theme.ColorActiveStatus
import com.example.ui.theme.ColorBackground
import com.example.ui.theme.ColorComponentWhite
import com.example.ui.theme.ColorNavigations
import com.example.ui.theme.ColorTextWhite
import com.example.ui.theme.ColorTopHeading
import com.example.ui.viewmodel.ChintanSaraniViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun DailyWizardScreen(
    viewModel: ChintanSaraniViewModel,
    modifier: Modifier = Modifier
) {
    val draft by viewModel.currentDraft.collectAsStateWithLifecycle()
    val activeLang by viewModel.activeLanguage.collectAsStateWithLifecycle()
    val currentStep by viewModel.currentStep.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val lastSubmittedEntry by viewModel.lastSubmittedEntry.collectAsStateWithLifecycle()
    val allEntries by viewModel.allEntries.collectAsStateWithLifecycle()
    val selectedDetailEntry by viewModel.selectedDetailEntry.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val isToday = selectedDate == viewModel.getTodayDate()
    val isYesterday = selectedDate == viewModel.getYesterdayDate()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ColorBackground)
    ) {
        // Date Selection & Navigation Bar
        DateSelectionBar(
            selectedDate = selectedDate,
            isToday = isToday,
            isYesterday = isYesterday,
            language = activeLang,
            onTodayClick = { viewModel.selectDate(viewModel.getTodayDate()) },
            onYesterdayClick = { viewModel.selectDate(viewModel.getYesterdayDate()) },
            onNewEntryClick = { viewModel.startNewEntry() },
            onPickDateClick = {
                val cal = Calendar.getInstance()
                val parts = selectedDate.split("-")
                val year = parts.getOrNull(0)?.toIntOrNull() ?: cal.get(Calendar.YEAR)
                val month = (parts.getOrNull(1)?.toIntOrNull()?.minus(1)) ?: cal.get(Calendar.MONTH)
                val day = parts.getOrNull(2)?.toIntOrNull() ?: cal.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(context, { _, y, m, d ->
                    val formatted = String.format("%04d-%02d-%02d", y, m + 1, d)
                    viewModel.selectDate(formatted)
                }, year, month, day).show()
            }
        )

        // Wizard Progress Header
        WizardProgressHeader(
            currentStep = currentStep,
            completedSteps = draft.completedSteps,
            language = activeLang,
            onStepClick = { step -> viewModel.setStep(step) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Main Animated Wizard Content
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> -width } + fadeOut()
                    )
                } else {
                    (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> width } + fadeOut()
                    )
                }
            },
            label = "wizard_step_transition"
        ) { targetStep ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                }

                when (targetStep) {
                    1 -> {
                        // --- STEP 1: Basic Practices & Remedies ---
                        item {
                            StepHeader(
                                title = AppStrings.step1Title(activeLang),
                                subtitle = AppStrings.step1Subtitle(activeLang)
                            )
                        }

                        // 1) नामजप : कुलदेवी आणि श्री गुरुदेव दत्त
                        item {
                            Text(
                                text = AppStrings.item1Title(activeLang),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                            )
                        }

                        item {
                            CounterItemCard(
                                title = AppStrings.kuldeviName(activeLang),
                                subtitle = AppStrings.kuldeviDesc(activeLang),
                                value = draft.naamjapKuldevi,
                                onValueChange = { newVal ->
                                    viewModel.updateDraft { it.copy(naamjapKuldevi = newVal) }
                                },
                                isTimeInMinutes = true,
                                language = activeLang,
                                testTag = "field_naamjap_kuldevi"
                            )
                        }

                        item {
                            CounterItemCard(
                                title = AppStrings.gurudevDattaName(activeLang),
                                subtitle = AppStrings.gurudevDattaDesc(activeLang),
                                value = draft.naamjapGurudevDatta,
                                onValueChange = { newVal ->
                                    viewModel.updateDraft { it.copy(naamjapGurudevDatta = newVal) }
                                },
                                isTimeInMinutes = true,
                                language = activeLang,
                                testTag = "field_naamjap_gurudev_datta"
                            )
                        }

                        // 2) कापूर इत्र उपाय
                        item {
                            CounterItemCard(
                                title = AppStrings.item2Title(activeLang),
                                subtitle = AppStrings.item2Desc(activeLang),
                                value = draft.kapurItrUpay,
                                onValueChange = { newVal ->
                                    viewModel.updateDraft { it.copy(kapurItrUpay = newVal) }
                                },
                                testTag = "field_kapur_itr_upay"
                            )
                        }

                        // 3) प्रार्थना
                        item {
                            CounterItemCard(
                                title = AppStrings.item3Title(activeLang),
                                subtitle = AppStrings.item3Desc(activeLang),
                                value = draft.prarthana,
                                onValueChange = { newVal ->
                                    viewModel.updateDraft { it.copy(prarthana = newVal) }
                                },
                                testTag = "field_prarthana"
                            )
                        }

                        // 4) कृतज्ञता
                        item {
                            CounterItemCard(
                                title = AppStrings.item4Title(activeLang),
                                subtitle = AppStrings.item4Desc(activeLang),
                                value = draft.krutajnata,
                                onValueChange = { newVal ->
                                    viewModel.updateDraft { it.copy(krutajnata = newVal) }
                                },
                                testTag = "field_krutajnata"
                            )
                        }

                        // 5) आवरण काढणे
                        item {
                            CounterItemCard(
                                title = AppStrings.item5Title(activeLang),
                                subtitle = AppStrings.item5Desc(activeLang),
                                value = draft.aavaranKadhne,
                                onValueChange = { newVal ->
                                    viewModel.updateDraft { it.copy(aavaranKadhne = newVal) }
                                },
                                testTag = "field_aavaran_kadhne"
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    viewModel.submitStep1()
                                    scope.launch { listState.scrollToItem(0) }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("submit_step_1_button"),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ChaitanyaBluePrimary,
                                    contentColor = ColorTextWhite
                                )
                            ) {
                                Text(
                                    text = AppStrings.nextStep(activeLang, 2),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = ColorTextWhite
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = ColorTextWhite
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Submitted entries component (shows submitted data; clicking shows full details)
                        item {
                            SubmittedEntriesSection(
                                lastSubmittedEntry = lastSubmittedEntry,
                                allEntries = allEntries,
                                language = activeLang,
                                onEntryClick = { entry ->
                                    viewModel.showEntryDetails(entry)
                                },
                                onDeleteEntry = { entry ->
                                    viewModel.deleteEntry(entry.date)
                                }
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                    }

                    2 -> {
                        // --- STEP 2: Devotion & Reflection ---
                        item {
                            StepHeader(
                                title = AppStrings.step2Title(activeLang),
                                subtitle = AppStrings.step2Subtitle(activeLang)
                            )
                        }

                        // 6) भावजागृती चे प्रयत्न
                        item {
                            CounterItemCard(
                                title = AppStrings.item6Title(activeLang),
                                subtitle = AppStrings.item6Desc(activeLang),
                                value = draft.bhavjagruti,
                                onValueChange = { newVal ->
                                    viewModel.updateDraft { it.copy(bhavjagruti = newVal) }
                                },
                                testTag = "field_bhavjagruti"
                            )
                        }

                        // 7) आत्मनिवेदन
                        item {
                            CounterItemCard(
                                title = AppStrings.item7Title(activeLang),
                                subtitle = AppStrings.item7Desc(activeLang),
                                value = draft.aatmanivedan,
                                onValueChange = { newVal ->
                                    viewModel.updateDraft { it.copy(aatmanivedan = newVal) }
                                },
                                testTag = "field_aatmanivedan"
                            )
                        }

                        // 8) मनाचा आढावा घेणे
                        item {
                            BooleanCounterItemCard(
                                title = AppStrings.item8Title(activeLang),
                                subtitle = AppStrings.item8Desc(activeLang),
                                isDone = draft.manachaAadhavDone,
                                count = draft.manachaAadhavCount,
                                onDoneChange = { isDone ->
                                    viewModel.updateDraft { it.copy(manachaAadhavDone = isDone) }
                                },
                                onCountChange = { count ->
                                    viewModel.updateDraft { it.copy(manachaAadhavCount = count) }
                                },
                                language = activeLang,
                                testTag = "field_manacha_aadhav"
                            )
                        }

                        // 9) दैनंदिन लिखाण
                        item {
                            BooleanCounterItemCard(
                                title = AppStrings.item9Title(activeLang),
                                subtitle = AppStrings.item9Desc(activeLang),
                                isDone = draft.dainandinLikhanDone,
                                count = draft.dainandinLikhanCount,
                                onDoneChange = { isDone ->
                                    viewModel.updateDraft { it.copy(dainandinLikhanDone = isDone) }
                                },
                                onCountChange = { count ->
                                    viewModel.updateDraft { it.copy(dainandinLikhanCount = count) }
                                },
                                language = activeLang,
                                testTag = "field_dainandin_likhan"
                            )
                        }

                        // 10) दैनिक ग्रंथ पठण
                        item {
                            BooleanCounterItemCard(
                                title = AppStrings.item10Title(activeLang),
                                subtitle = AppStrings.item10Desc(activeLang),
                                isDone = draft.granthPathanDone,
                                count = draft.granthPathanCount,
                                onDoneChange = { isDone ->
                                    viewModel.updateDraft { it.copy(granthPathanDone = isDone) }
                                },
                                onCountChange = { count ->
                                    viewModel.updateDraft { it.copy(granthPathanCount = count) }
                                },
                                language = activeLang,
                                testTag = "field_granth_pathan"
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        viewModel.setStep(1)
                                        scope.launch { listState.scrollToItem(0) }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                        .testTag("step_2_previous_button"),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = AppStrings.previousStep(activeLang),
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }

                                Button(
                                    onClick = {
                                        viewModel.submitStep2()
                                        scope.launch { listState.scrollToItem(0) }
                                    },
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .height(52.dp)
                                        .testTag("submit_step_2_button"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ChaitanyaBluePrimary,
                                        contentColor = ColorTextWhite
                                    )
                                ) {
                                    Text(
                                        text = AppStrings.nextStep(activeLang, 3),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = ColorTextWhite
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = null,
                                        tint = ColorTextWhite
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Submitted entries component in Step 2 as well
                        item {
                            SubmittedEntriesSection(
                                lastSubmittedEntry = lastSubmittedEntry,
                                allEntries = allEntries,
                                language = activeLang,
                                onEntryClick = { entry ->
                                    viewModel.showEntryDetails(entry)
                                },
                                onDeleteEntry = { entry ->
                                    viewModel.deleteEntry(entry.date)
                                }
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                    }

                    3 -> {
                        // --- STEP 3: Error Rectification, Penance & Humility ---
                        item {
                            StepHeader(
                                title = AppStrings.step3Title(activeLang),
                                subtitle = AppStrings.step3Subtitle(activeLang)
                            )
                        }

                        // 11) चुका विचारणे व आपली चूक दुसऱ्यांना सांगणे
                        item {
                            BooleanCounterItemCard(
                                title = AppStrings.item11Title(activeLang),
                                subtitle = AppStrings.item11Desc(activeLang),
                                isDone = draft.chukaVicharneDone,
                                count = draft.chukaVicharneCount,
                                onDoneChange = { isDone ->
                                    viewModel.updateDraft { it.copy(chukaVicharneDone = isDone) }
                                },
                                onCountChange = { count ->
                                    viewModel.updateDraft { it.copy(chukaVicharneCount = count) }
                                },
                                language = activeLang,
                                testTag = "field_chuka_vicharne"
                            )
                        }

                        // 12) प्रायश्चित व शिक्षा घेणे
                        item {
                            BooleanCounterItemCard(
                                title = AppStrings.item12Title(activeLang),
                                subtitle = AppStrings.item12Desc(activeLang),
                                isDone = draft.prayashchittaDone,
                                count = draft.prayashchittaCount,
                                onDoneChange = { isDone ->
                                    viewModel.updateDraft { it.copy(prayashchittaDone = isDone) }
                                },
                                onCountChange = { count ->
                                    viewModel.updateDraft { it.copy(prayashchittaCount = count) }
                                },
                                language = activeLang,
                                testTag = "field_prayashchitta"
                            )
                        }

                        // 13) सारणी लिखाण
                        item {
                            BooleanOnlyItemCard(
                                title = AppStrings.item13Title(activeLang),
                                subtitle = AppStrings.item13Desc(activeLang),
                                isDone = draft.saraniLikhanDone,
                                onDoneChange = { isDone ->
                                    viewModel.updateDraft { it.copy(saraniLikhanDone = isDone) }
                                },
                                language = activeLang,
                                testTag = "field_sarani_likhan"
                            )
                        }

                        // 14) चुका लिहिणे (Added after 13)
                        item {
                            BooleanCounterItemCard(
                                title = AppStrings.item14Title(activeLang),
                                subtitle = AppStrings.item14Desc(activeLang),
                                isDone = draft.chukaLihineDone,
                                count = draft.chukaLihineCount,
                                onDoneChange = { isDone ->
                                    viewModel.updateDraft { it.copy(chukaLihineDone = isDone) }
                                },
                                onCountChange = { count ->
                                    viewModel.updateDraft { it.copy(chukaLihineCount = count) }
                                },
                                language = activeLang,
                                testTag = "field_chuka_lihine"
                            )
                        }

                        // 15) स्वयंसूचना सत्र
                        item {
                            BooleanCounterItemCard(
                                title = AppStrings.item15Title(activeLang),
                                subtitle = AppStrings.item15Desc(activeLang),
                                isDone = draft.swayamsuchanaDone,
                                count = draft.swayamsuchanaCount,
                                onDoneChange = { isDone ->
                                    viewModel.updateDraft { it.copy(swayamsuchanaDone = isDone) }
                                },
                                onCountChange = { count ->
                                    viewModel.updateDraft { it.copy(swayamsuchanaCount = count) }
                                },
                                language = activeLang,
                                testTag = "field_swayamsuchana"
                            )
                        }

                        // 16) क्षमायाचना ईश्वर व साधक
                        item {
                            BooleanOnlyItemCard(
                                title = AppStrings.item16Title(activeLang),
                                subtitle = AppStrings.item16Desc(activeLang),
                                isDone = draft.kshamayachanaDone,
                                onDoneChange = { isDone ->
                                    viewModel.updateDraft { it.copy(kshamayachanaDone = isDone) }
                                },
                                language = activeLang,
                                testTag = "field_kshamayachana"
                            )
                        }

                        // Reflection Notes
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = ColorComponentWhite // #FFFFFF
                                ),
                                border = BorderStroke(1.dp, ChaitanyaCardBorder)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = if (activeLang == AppLanguage.MARATHI) "आजची आध्यात्मिक अनुभूती किंवा नोंद" else "Today's Spiritual Reflection or Notes",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = ChaitanyaBluePrimary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = draft.reflectionNotes,
                                        onValueChange = { text ->
                                            viewModel.updateDraft { it.copy(reflectionNotes = text) }
                                        },
                                        placeholder = {
                                            Text(
                                                text = if (activeLang == AppLanguage.MARATHI) "मनमोकळी नोंद लिहा..." else "Write heartfelt reflections...",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(90.dp)
                                            .testTag("reflection_notes_input"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = ChaitanyaBluePrimary,
                                            unfocusedBorderColor = ChaitanyaCardBorder
                                        )
                                    )
                                }
                            }
                        }

                        // Completion Scorecard Summary (White Component)
                        item {
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = ColorComponentWhite // #FFFFFF
                                ),
                                border = BorderStroke(1.dp, ChaitanyaCardBorder),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = ChaitanyaBluePrimary,
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = if (activeLang == AppLanguage.MARATHI) "पूर्ण झालेल्या कृती" else "Completed Actions",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = ChaitanyaTextSecondary
                                            )
                                            Text(
                                                text = "${draft.completedHabitsCount} / ${draft.totalHabitsCount}",
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                                color = ChaitanyaBluePrimary
                                            )
                                        }
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = if (activeLang == AppLanguage.MARATHI) "एकूण नामजप वेळ" else "Total Naamjap Time",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = ChaitanyaTextSecondary
                                        )
                                        val totalMins = draft.totalNaamjap
                                        val formattedTotal = if (totalMins <= 0) {
                                            if (activeLang == AppLanguage.MARATHI) "० मि." else "0 min"
                                        } else if (totalMins < 60) {
                                            if (activeLang == AppLanguage.MARATHI) "$totalMins मिनिटे" else "$totalMins mins"
                                        } else {
                                            val h = totalMins / 60
                                            val m = totalMins % 60
                                            if (activeLang == AppLanguage.MARATHI) {
                                                if (m == 0) "$h तास" else "$h तास $m मि."
                                            } else {
                                                if (m == 0) "$h hr" else "$h hr $m min"
                                            }
                                        }
                                        Text(
                                            text = formattedTotal,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = ChaitanyaBluePrimary
                                        )
                                    }
                                }
                            }
                        }

                        // Buttons: Previous & Save Chintan Sarani (Redirects to fresh entry)
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        viewModel.setStep(2)
                                        scope.launch { listState.scrollToItem(0) }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                        .testTag("step_3_previous_button"),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = AppStrings.previousStep(activeLang),
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }

                                Button(
                                    onClick = {
                                        viewModel.submitStep3 {
                                            scope.launch { listState.scrollToItem(0) }
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1.4f)
                                        .height(52.dp)
                                        .testTag("submit_chintan_sarani_button"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ColorActiveStatus, // #FD0031
                                        contentColor = ColorTextWhite
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DoneAll,
                                        contentDescription = null,
                                        tint = ColorTextWhite
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (activeLang == AppLanguage.MARATHI) "जतन करा आणि नवीन नोंद" else "Save & Fill New Entry",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = ColorTextWhite // #FFFFFF
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Submitted entries component in Step 3 as well
                        item {
                            SubmittedEntriesSection(
                                lastSubmittedEntry = lastSubmittedEntry,
                                allEntries = allEntries,
                                language = activeLang,
                                onEntryClick = { entry ->
                                    viewModel.showEntryDetails(entry)
                                },
                                onDeleteEntry = { entry ->
                                    viewModel.deleteEntry(entry.date)
                                }
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                    }
                }
            }
        }

        // Full Detailed Data Bottom Sheet shown when any routine entry data is clicked
        selectedDetailEntry?.let { entry ->
            RoutineDetailBottomSheet(
                entry = entry,
                language = activeLang,
                onDismiss = { viewModel.dismissEntryDetails() },
                onEditEntry = { entryToEdit ->
                    viewModel.loadEntryIntoDraft(entryToEdit)
                },
                onDeleteEntry = { entryToDelete ->
                    viewModel.deleteEntry(entryToDelete.date)
                }
            )
        }
    }
}

@Composable
private fun StepHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DateSelectionBar(
    selectedDate: String,
    isToday: Boolean,
    isYesterday: Boolean,
    language: AppLanguage,
    onTodayClick: () -> Unit,
    onYesterdayClick: () -> Unit,
    onNewEntryClick: () -> Unit,
    onPickDateClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = ColorComponentWhite, // White component
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Today button
                DatePill(
                    label = AppStrings.todayLabel(language),
                    isSelected = isToday,
                    onClick = onTodayClick,
                    testTag = "date_today_pill"
                )

                // Yesterday button
                DatePill(
                    label = AppStrings.yesterdayLabel(language),
                    isSelected = isYesterday,
                    onClick = onYesterdayClick,
                    testTag = "date_yesterday_pill"
                )

                // New Entry / Clear button
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onNewEntryClick() }
                        .testTag("start_new_entry_pill"),
                    color = ColorNavigations // #D3EFFB
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "New Entry",
                            modifier = Modifier.size(14.dp),
                            tint = ChaitanyaBlueDark
                        )
                        Text(
                            text = AppStrings.newEntryButton(language),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = ChaitanyaBlueDark
                        )
                    }
                }
            }

            // Calendar date picker button
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onPickDateClick() }
                    .testTag("date_picker_button"),
                color = ColorNavigations // #D3EFFB
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Pick date",
                        modifier = Modifier.size(16.dp),
                        tint = ChaitanyaBluePrimary
                    )
                    Text(
                        text = selectedDate,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = ChaitanyaBlueDark
                    )
                }
            }
        }
    }
}

@Composable
private fun DatePill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String = ""
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) ChaitanyaBluePrimary
                else ChaitanyaSkyContainer
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (isSelected) ColorTextWhite // #FFFFFF
            else ChaitanyaBlueDark
        )
    }
}
