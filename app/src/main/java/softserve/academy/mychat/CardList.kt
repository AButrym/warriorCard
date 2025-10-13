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
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel
import softserve.academy.mychat.CardListEvent.Add
import softserve.academy.mychat.CardListEvent.CancelDelete
import softserve.academy.mychat.CardListEvent.CancelEdit
import softserve.academy.mychat.CardListEvent.ConfirmDelete
import softserve.academy.mychat.CardListEvent.OpenDeleteConfirmationDialog
import softserve.academy.mychat.CardListEvent.OpenEditDialog
import softserve.academy.mychat.CardListEvent.SubmitEdit
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

typealias Callback = (CardListEvent) -> Unit

sealed interface CardListEvent {
    data class OpenDeleteConfirmationDialog(val ix: Int) : CardListEvent
    data object CancelDelete : CardListEvent
    data object ConfirmDelete : CardListEvent
    data class OpenEditDialog(val ix: Int) : CardListEvent
    data class SubmitEdit(val text: String) : CardListEvent
    data object CancelEdit : CardListEvent
    data class Add(val text: String) : CardListEvent
}

class CardListViewModel(
    private val storageService: StorageService
) : ViewModel() {
    private val _itemList = mutableStateListOf(
        *storageService.read().toTypedArray()
    )
    val itemList: List<String> get() = _itemList

    var isConfirmDialogOpen by mutableStateOf(false)
        private set
    var itemToBeDeleted by mutableIntStateOf(-1)
        private set

    val itemToBeDeletedText: String
        get() = _itemList[itemToBeDeleted]

    var isEditDialogOpen by mutableStateOf(false)
        private set
    var itemBeingEdited by mutableIntStateOf(-1)
        private set
    val itemBeingEditedText: String
        get() = _itemList[itemBeingEdited]

    fun onEvent(event: CardListEvent): Unit = when (event) {
        is OpenDeleteConfirmationDialog -> delete(event.ix)
        is CancelDelete                 -> deleteSelected(false)
        is ConfirmDelete                -> deleteSelected(true)
        is OpenEditDialog               -> edit(event.ix)
        is CancelEdit                   -> editSelected(null)
        is SubmitEdit                   -> editSelected(event.text)
        is Add                          -> add(event.text)
    }

    fun delete(ix: Int) {
        itemToBeDeleted = ix
        isConfirmDialogOpen = true
    }

    fun edit(ix: Int) {
        itemBeingEdited = ix
        isEditDialogOpen = true
    }

    fun editSelected(editedText: String?) {
        if (editedText != null) {
            _itemList[itemBeingEdited] = editedText
            storageService.write(_itemList)
        }
        itemBeingEdited = -1
        isEditDialogOpen = false
    }

    fun deleteSelected(confirmed: Boolean) {
        when {
            !confirmed -> itemToBeDeleted = -1
            itemToBeDeleted !in _itemList.indices ->
                Log.wtf("CardList", "deleteSelected is invoked while itemToBeDeleted is -1")

            else -> run {
                _itemList.removeAt(itemToBeDeleted)
                itemToBeDeleted = -1
                storageService.write(_itemList)
            }
        }
        isConfirmDialogOpen = false
    }

    fun add(text: String) {
        _itemList.add(text)
        storageService.write(_itemList)
    }
}

@Composable
fun CardList(vm: CardListViewModel = koinViewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.align(Alignment.Center)) {
            item {
                AddCardItem(vm::onEvent)
            }
            items(count = vm.itemList.size) { index ->
                CardListItem(
                    text = vm.itemList[index],
                    ix = index,
                    vm::onEvent
                )
            }
        }

        if (vm.isConfirmDialogOpen) {
            ConfirmDialog(
                itemText = vm.itemToBeDeletedText,
                callback = vm::onEvent,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (vm.isEditDialogOpen) {
            EditDialog(
                itemText = vm.itemBeingEditedText,
                callback = vm::onEvent,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ConfirmDialog(
    itemText: String,
    callback: Callback,
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
            callback(CardListEvent.CancelDelete)
        },
        confirmButton = {
            TextButton(onClick = { callback(CardListEvent.ConfirmDelete) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { callback(CardListEvent.CancelDelete) }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EditDialog(
    itemText: String,
    callback: Callback,
    modifier: Modifier = Modifier
) {
    var text by rememberSaveable { mutableStateOf(itemText) }

    AlertDialog(
        modifier = modifier,
        icon = {
            Icon(
                Icons.Filled.Edit,
                contentDescription = null,
                tint = Color.Blue
            )
        },
        title = { Text(text = "You are editing an item with initial text:'$itemText'?") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it }
            )
        },
        onDismissRequest = {
            callback(CardListEvent.CancelEdit)
        },
        confirmButton = {
            TextButton(onClick = {
                callback(CardListEvent.SubmitEdit(text))
                    .also { text = "" }
            }) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = { callback(CardListEvent.CancelEdit) }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddCardItem(
    callback: Callback = {}
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
                onClick = { callback(CardListEvent.Add(text)).also { text = "" } }
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
    ix: Int,
    callback: Callback = {}
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

            IconButton(onClick = { callback(OpenEditDialog(ix)) }) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "edit",
                    tint = Color.Blue
                )
            }

            Spacer(Modifier.width(8.dp))

            IconButton(onClick = { callback(OpenDeleteConfirmationDialog(ix)) }) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "delete",
                    tint = Color.Red
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun CardListItemPreview() {
//    CardListItem("Warrior #1", 0)
//}

//@Preview
//@Composable
//fun CardListPreview() {
//    Surface(
//        modifier = Modifier
//            .fillMaxSize()
//            .wrapContentSize(Alignment.Center)
//    ) {
//        CardList()
//    }
//}