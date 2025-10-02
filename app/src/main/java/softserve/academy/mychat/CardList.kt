package softserve.academy.mychat

import android.content.Context
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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    } catch (e: Exception) {
        return emptyList()
    }
}

@Composable
fun CardList() {
    val context = LocalContext.current
    val itemList = remember {
        mutableStateListOf(
            *readData(context).toTypedArray()
        )
    }

    @Composable
    fun updateData() {
        writeData(itemList, context)
    }
    updateData()

    fun deleteAt(ix: Int) {
        itemList.removeAt(ix)
    }

    fun add(text: String) {
        itemList.add(text)
    }

    LazyColumn {
        item {
            AddCardItem(::add)
        }
        items(count = itemList.size) { index ->
            CardListItem(
                text = itemList[index],
                onDelete = { deleteAt(index) }
            )
        }
    }
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
                .padding(4.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it }
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )
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
                .padding(4.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .width(300.dp)
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onDelete
            ) {
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