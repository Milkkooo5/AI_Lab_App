package com.example.thebestapp2026

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.thebestapp2026.ui.screens.LoginScreen
import com.example.thebestapp2026.ui.theme.TheBestApp2026Theme
import com.example.thebestapp2026.ui.viewmodel.LoginViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loginScreen_displaysFieldsAndShowsValidationErrorWhenEmptyLoginClicked() {

        val loginViewModel = LoginViewModel()

        composeRule.setContent {
            TheBestApp2026Theme {
                LoginScreen(
                    navController = rememberNavController(),
                    viewModel = loginViewModel
                )
            }
        }

        composeRule.onNodeWithText("Вход")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Пароль")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Войти")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Войти")
            .performClick()

        composeRule.onNodeWithText("Введите корректную почту")
            .assertIsDisplayed()
    }
}