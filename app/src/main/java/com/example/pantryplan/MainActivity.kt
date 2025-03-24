package com.example.pantryplan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pantryplan.navigation.TopLevelDestination
import com.example.pantryplan.ui.theme.PantryPlanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantryPlanTheme {
                PantryPlanNavigationSuiteScaffold {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

/* Now in Android defines its NavigateSuiteScaffold wrapper in the component package of
   its /core/designsystem module. Probably a good idea to move ours eventually. But for now, having
   it in MainActivity isn't too bad...
 */
@Composable
@Preview
fun PantryPlanNavigationSuiteScaffold(content: @Composable (() -> Unit) = {}) {
    var currentDestination by rememberSaveable { mutableStateOf(TopLevelDestination.PANTRY) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach {
                item(
                    selected = it == currentDestination,
                    onClick = { currentDestination = it },
                    icon = { Icon(
                        if (it == currentDestination) it.selectedIcon else it.unselectedIcon,
                        contentDescription = stringResource(it.label)
                    )},
                    label = { Text(stringResource(it.label)) },
                )
            }
        }
    ) {
        content()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PantryPlanTheme {
        Greeting("Android")
    }
}