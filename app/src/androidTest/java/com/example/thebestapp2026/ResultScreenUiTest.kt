package com.example.thebestapp2026


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.example.thebestapp2026.ui.theme.TheBestApp2026Theme
import org.junit.Rule
import org.junit.Test

class ResultScreenUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun resultScreen_displaysIndicatorsWithStatusBadges() {
        composeRule.setContent {
            TheBestApp2026Theme {
                TestResultScreen()
            }
        }

        composeRule.onNodeWithText("Гемоглобин").assertIsDisplayed()
        composeRule.onNodeWithText("135 г/л").assertIsDisplayed()
        composeRule.onNodeWithText("норма").assertIsDisplayed()

        composeRule.onNodeWithText("Лейкоциты").assertIsDisplayed()
        composeRule.onNodeWithText("11.2").assertIsDisplayed()
        composeRule.onNodeWithText("Есть отклонения").assertIsDisplayed()
    }
}

@Composable
fun TestResultScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Результат AI-анализа")

        TestIndicatorCard(
            title = "Гемоглобин",
            value = "135 г/л",
            status = "норма",
            color = Color.Green
        )

        TestIndicatorCard(
            title = "Лейкоциты",
            value = "11.2",
            status = "Есть отклонения",
            color = Color(0xFFFF9800)
        )
    }
}

@Composable
fun TestIndicatorCard(
    title: String,
    value: String,
    status: String,
    color: Color
) {
    Card(modifier = Modifier.padding(top = 8.dp)) {
        Row(modifier = Modifier.padding(12.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title)
                Text(text = value)
            }

            Text(
                text = status,
                modifier = Modifier
                    .background(color)
                    .padding(8.dp)
            )
        }
    }
}