package com.helic.heybooks

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.helic.heybooks.data.viewmodels.MainViewModel
import com.helic.heybooks.navigation.RootNavGraph
import com.helic.heybooks.ui.theme.BackgroundColor
import com.helic.heybooks.ui.theme.HeyBooksTheme
import com.helic.heybooks.utils.Msnackbar
import com.helic.heybooks.utils.rememberSnackbarState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    private val mainViewModel: MainViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeyBooksTheme {
                val systemUiController = rememberSystemUiController()
                navController = rememberAnimatedNavController()
                val appState: Msnackbar = rememberSnackbarState()
                val systemUIColor = MaterialTheme.colors.BackgroundColor
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = systemUIColor
                    )
                }
                Scaffold(
                    scaffoldState = appState.scaffoldState
                ) {
                    RootNavGraph(
                        navController = navController,
                        mainViewModel = mainViewModel
                    ) { message, duration ->
                        appState.showSnackbar(message = message, duration = duration)
                    }
                }
            }
        }
    }
}
