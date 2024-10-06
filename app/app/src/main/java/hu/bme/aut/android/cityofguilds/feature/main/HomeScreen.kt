package hu.bme.aut.android.cityofguilds.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextPainter.paint
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import hu.bme.aut.android.cityofguilds.R
import hu.bme.aut.android.cityofguilds.ui.common.GuildAppBar
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    logout : () -> Unit
) {
    Scaffold (
        topBar = {
            GuildAppBar(
                title = stringResource(id = R.string.explore_the_city),
                actions = {  },
                onNavigateBack = logout
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->



        ConstraintLayout(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .paint(
                painterResource(id = R.drawable.background),
                contentScale = ContentScale.FillBounds
            )
        ){
            val guideline = createGuidelineFromTop(0.6f)
            val (text) = createRefs()
            Box(
                modifier = Modifier
                    .padding(end = 20.dp)
                    .constrainAs(text){
                        bottom.linkTo(guideline)
                        top.linkTo(guideline)
                        end.linkTo(parent.end)

                    }
                    .size(160.dp, 80.dp)

            ){
                Text(
                    text = stringResource(R.string.text_explain_game),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.rotate(13f)
                )
            }

        }

    }
}

@Preview
@Composable
fun previewHome(){
    HomeScreen {  }
}