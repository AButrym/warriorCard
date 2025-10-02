package softserve.academy.mychat

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.tooling.preview.Preview
import kotlin.String

@Preview(showSystemUi = true, showBackground = false)
@Composable
fun DropdownPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
//        val options = listOf("Warrior", "Knight", "Defender", "Lancer", "Vampire")
        var options by remember { mutableStateOf(listOf("Health", "Attack", "Vampirism")) }

        fun callback(option: String) {
            Log.i("DropdownPreview", option)
            options = options + if (option.endsWith(")")) {
                val lastOpeningParenthesis = option.lastIndexOf('(')
                val name = option.substring(0, lastOpeningParenthesis)
                val i = option.substring(lastOpeningParenthesis + 1, option.length - 1).toInt()
                "$name(${i + 1})"
            } else {
                "$option(1)"
            }
        }

        DropdownComponent1WithState(
            options = options,
            onSelectedOptionChanged = ::callback
        )
    }
}

@Composable
fun DropdownComponent1WithState(
    options: List<String>,
    onSelectedOptionChanged: (String) -> Unit
) {
    var selectedOption by remember { mutableStateOf(options[0]) }

    DropdownComponent1(
        options = options,
        selectedOption = selectedOption,
        onSelectedOptionChanged = {
            selectedOption = it
            onSelectedOptionChanged(it)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownComponent1(
    options: List<String>,
    selectedOption: String,
    onSelectedOptionChanged: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text("Choose option") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelectedOptionChanged(option)
                        expanded = false
                    }
                )
            }
        }
    }
}