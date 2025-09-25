package softserve.academy.mychat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import softserve.academy.mychat.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
//                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
//@Preview
@Composable
fun PreviewScaffoldScreen() {
    val sections = listOf("A", "B", "C", "D", "E", "F", "G")

    LazyColumn(reverseLayout = false, contentPadding = PaddingValues(6.dp)) {
        sections.forEach { section ->
            stickyHeader {
                Text(text = "Section $section",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(8.dp)
                )
            }
            items(10) {
                Text("Item $it from the section $section")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Hyperskill") }
            )
        },
        bottomBar = {
            BottomAppBar { /*...*/ }
        }
    ) { innerPadding ->
        ScaffoldContent(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize())
    }
}

@Composable
fun ScaffoldContent(modifier: Modifier = Modifier) =
    Column(modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text("Start of the Column")
        Text("End of the Column")
    }



typealias PropName = String

data class WarriorData(
    val className: String,
    val health: Int,
    val attack: Int,
    val extraProps: Map<PropName, Int> = emptyMap()
)

val warrior = WarriorData(
    className = "Warrior",
    health = 50,
    attack = 5
)

val knight = WarriorData(
    className = "Knight",
    health = 50,
    attack = 7
)

val defender = WarriorData(
    className = "Defender",
    health = 60,
    attack = 3,
    extraProps = mapOf("\uD83D\uDEE1\uFE0F Defense" to 2)
)

val vampire = WarriorData(
    className = "Vampire",
    health = 40,
    attack = 4,
    extraProps = mapOf("vampirism" to 50)
)

val lancer = WarriorData(
    className = "Lancer",
    health = 50,
    attack = 6
)

val healer = WarriorData(
    className = "Healer",
    health = 60,
    attack = 0,
    extraProps = mapOf("healing" to 2)
)

val slogans = mapOf(
    "Warrior" to "Strength above all!",
    "Knight" to "Honor in battle!",
    "Defender" to "Shield of the weak!",
    "Vampire" to "Life is mine to take.",
    "Healer" to "Mend, not end.",
    "Lancer" to "Pierce through destiny!"
)

@Composable
fun WarriorCard(
    warrior: WarriorData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(width = 280.dp, height = 152.dp)
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.warrior),
                    contentDescription = warrior.className,
                    modifier = Modifier
                        .size(52.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = warrior.className,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = slogans[warrior.className] ?: "Ready for battle!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("❤️ Health: ${warrior.health}")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚔️ Attack: ")
                        Text(warrior.attack.toString())
                    }
                }

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    warrior.extraProps.forEach { (name, value) ->
                        Card(
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .height(40.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF0F0F0)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Text("$name: $value")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HealthIcon(
    health: Float, // 0.0 .. 1.0
    modifier: Modifier = Modifier,
    filledColor: Color = Color.Red,
    emptyColor: Color = Color.Gray
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Health",
            tint = emptyColor,
            modifier = Modifier.matchParentSize()
        )

        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = filledColor,
            modifier = Modifier
                .matchParentSize()
                .drawWithContent {
                    val progressHeight = size.height * health.coerceIn(0f, 1f)
                    clipRect(
                        left = 0f,
                        top = size.height - progressHeight,
                        right = size.width,
                        bottom = size.height
                    ) {
                        this@drawWithContent.drawContent()
                    }
                }
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF00FD48)
@Composable
fun GreetingPreview() {
    AppTheme {
        WarriorCard(defender)
    }
}

@Preview(showBackground = true)
@Composable
fun WarriorCardPreview() {
    val defender = WarriorData(
        className = "Defender",
        health = 60,
        attack = 3,
        extraProps = mapOf("🛡️Defense" to 2)
    )
    WarriorCard(defender)
}