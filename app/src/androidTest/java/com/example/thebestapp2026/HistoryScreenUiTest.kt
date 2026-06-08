package com.example.thebestapp2026


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.example.thebestapp2026.ui.theme.TheBestApp2026Theme
import org.junit.Rule
import org.junit.Test

class HistoryScreenUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun historyScreen_filterByDeviationShowsOnlyDeviationItems() {
        composeRule.setContent {
            TheBestApp2026Theme {
                TestHistoryScreen()
            }
        }

        composeRule.onNodeWithText("Гемоглобин").assertIsDisplayed()
        composeRule.onNodeWithText("Глюкоза").assertIsDisplayed()

        composeRule.onNodeWithText("Отклонения").performClick()

        composeRule.onNodeWithText("Гемоглобин").assertIsDisplayed()
        composeRule.onNodeWithText("Есть отклонения").assertIsDisplayed()

        composeRule.onNodeWithText("Глюкоза").assertDoesNotExist()
        composeRule.onNodeWithText("Норма").assertDoesNotExist()
    }
}

data class TestAnalysisItem(
    val title: String,
    val value: String,
    val status: String
)

@Composable
fun TestHistoryScreen() {
    val items = listOf(
        TestAnalysisItem("Гемоглобин", "98 г/л", "Есть отклонения"),
        TestAnalysisItem("Глюкоза", "5.1 ммоль/л", "Норма")
    )

    var filter by remember { mutableStateOf("Все") }

    val filteredItems = if (filter == "Отклонения") {
        items.filter { it.status == "Есть отклонения" }
    } else {
        items
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "История анализов")

        Row {
            AssistChip(
                onClick = { filter = "Все" },
                label = { Text("Все") }
            )

            AssistChip(
                onClick = { filter = "Отклонения" },
                label = { Text("Отклонения") }
            )
        }

        filteredItems.forEach { item ->
            Card(modifier = Modifier.padding(top = 8.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = item.title)
                    Text(text = item.value)
                    Text(text = item.status)
                }
            }
        }
    }
}