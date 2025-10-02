package softserve.academy.mychat

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showSystemUi = true, showBackground = false)
@Composable
fun Dropdown2Preview() {
    Surface(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
    ) {
        DropdownComponent2()
    }
}

@Composable
fun DropdownComponent2() {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Warrior", "Knight", "Defender", "Lancer", "Vampire")
    var selectedText by remember { mutableStateOf(options[0]) }

    Box {
        Text(
            text = selectedText,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(16.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { label ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        selectedText = label
                        expanded = false
                        Log.i("DropdownComponent2", label)
                    }
                )
            }
        }
    }
}