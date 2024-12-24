package tm.example.composescroll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tm.example.composescroll.ui.theme.ComposeScrollListenerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeScrollListenerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PositionDetector(
                        List(100) { index -> "$index" },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PositionDetector(
    items: List<String>,
    modifier: Modifier = Modifier
) {
    val positions = remember { items.map { 0f to 0f }.toMutableStateList() }

    val ss = rememberScrollState()
    val visibleRange by remember {
        derivedStateOf {
            val firstIndex = positions.indexOfLast {
                it.first <= ss.value
            }
            val lastIndex = positions.indexOfLast {
                it.first <= ss.value + ss.viewportSize
            }
            "$firstIndex - $lastIndex"
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = visibleRange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(16.dp)
        )
        Column(
            modifier = Modifier
                .verticalScroll(ss)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items.forEachIndexed { index, item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .height(100.dp)
                        .onGloballyPositioned {
                            positions[index] = it.positionInParent().y to it.size.height.toFloat()
                        }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeScrollListenerTheme {
        PositionDetector(List(10) { index -> "$index" })
    }
}