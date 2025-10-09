package softserve.academy.mychat

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val FILE_NAME = "cardlist.dat"

fun writeData(items: List<String>, context: Context) {
    context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
        val oos = ObjectOutputStream(it)
        oos.writeObject(
            ArrayList<String>(items)
        )
    }
}

fun readData(context: Context): List<String> {
    try {
        @Suppress("UNCHECKED_CAST")
        return ObjectInputStream(context.openFileInput(FILE_NAME))
            .readObject() as ArrayList<String>
    } catch (_: Exception) {
        return emptyList()
    }
}

class CardListViewModel : ViewModel() {
    private val _itemList = mutableStateListOf("Item 1", "Item 2")
    val itemList: List<String> get() = _itemList

    var isConfirmDialogOpen by mutableStateOf(false)
        private set
    var itemToBeDeleted by mutableIntStateOf(-1)
        private set

    val itemToBeDeletedText: String
        get() = _itemList[itemToBeDeleted]

    fun delete(ix: Int) {
        itemToBeDeleted = ix
        isConfirmDialogOpen = true
    }

    fun deleteSelected(confirmed: Boolean) {
        when {
            !confirmed -> itemToBeDeleted = -1
            itemToBeDeleted !in _itemList.indices ->
                Log.wtf("CardList", "deleteSelected is invoked while itemToBeDeleted is -1")

            else -> run {
                _itemList.removeAt(itemToBeDeleted)
                itemToBeDeleted = -1
            }
        }
        isConfirmDialogOpen = false
    }

    fun add(text: String) {
        _itemList.add(text)
    }
}

@Composable
fun CardList(
    vm: CardListViewModel = viewModel()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.align(Alignment.Center)) {
            item {
                AddCardItem(vm::add)
            }
            items(count = vm.itemList.size) { index ->
                CardListItem(
                    text = vm.itemList[index],
                    onDelete = { vm.delete(index) }
                )
            }
        }

        if (vm.isConfirmDialogOpen) {
            ConfirmDialog(
                itemText = vm.itemToBeDeletedText,
                callback = vm::deleteSelected,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ConfirmDialog(
    itemText: String,
    callback: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        icon = {
            Icon(
                Icons.Filled.Warning,
                contentDescription = null,
                tint = Color.Red
            )
        },
        title = { Text(text = "Are you sure to delete this item?") },
        text = { Text(text = itemText) },
        onDismissRequest = {
            callback(false)
        },
        confirmButton = {
            TextButton(onClick = { callback(true) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { callback(false) }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddCardItem(
    onCreate: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it }
            )
            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = { onCreate(text).also { text = "" } }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "create",
                    tint = Color.Black
                )
            }
        }
    }

}

@Composable
fun CardListItem(
    text: String,
    onDelete: () -> Unit = {}
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .width(300.dp)
                    .padding(start = 8.dp)
            )
            Spacer(Modifier.weight(1f))

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "delete",
                    tint = Color.Black
                )
            }
        }
    }
}

@Preview
@Composable
fun CardListItemPreview() {
    CardListItem("Warrior #1")
}

@Preview
@Composable
fun CardListPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CardList()
    }
}