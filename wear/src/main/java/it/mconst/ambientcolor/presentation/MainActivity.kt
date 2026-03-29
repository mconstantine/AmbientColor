package it.mconst.ambientcolor.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.Text
import android.util.Log
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.produceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("AmbientColor", "started")

        setContent {
            val rustDataState = produceState<AndroidColorResult?>(initialValue = null) {
                try {
                    value = withContext(Dispatchers.IO) {
                        generateColorAndroid()
                    }
                } catch (e: Exception) {
                    Log.e("AmbientColor", "Rust failed", e)
                }
            }

            Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val displayText = when (val response = rustDataState.value) {
                    null -> "Loading..."
                    is AndroidColorResult.Ok -> "${response.temperature}"
                    is AndroidColorResult.NetworkError -> "Network error"
                    is AndroidColorResult.ParseError -> "Parse error"
                }

                Text(
                    text = displayText,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}