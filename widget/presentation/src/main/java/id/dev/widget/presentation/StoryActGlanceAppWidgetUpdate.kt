package id.dev.widget.presentation

import android.content.Context
import android.os.Build
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import id.dev.core.presentation.ui.story.toStoryUi
import id.dev.widget.domain.WidgetRepository
import id.dev.widget.presentation.themes.StoryActGlanceColorScheme
import id.dev.widget.presentation.ui.StoryContent
import kotlinx.coroutines.flow.map
import org.koin.mp.KoinPlatformTools

class StoryActGlanceAppWidgetUpdate : GlanceAppWidget() {

    private val widgetRepository: WidgetRepository by lazy {
        KoinPlatformTools.defaultContext().get().get<WidgetRepository>()
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)

    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val storyListState = widgetRepository.getAllStories().map { storyDomain ->
            storyDomain.map { it.toStoryUi() }
        }

        provideContent {
            val storyList by storyListState.collectAsState(initial = emptyList())

            GlanceTheme(
                colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    GlanceTheme.colors
                } else {
                    StoryActGlanceColorScheme.colors
                }
            ) {
                Column(
                    modifier = GlanceModifier
                        .fillMaxSize().padding(4.dp)
                        .background(GlanceTheme.colors.background),
                    verticalAlignment = Alignment.Vertical.CenterVertically,
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Text(
                        text = "StoryAct",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = GlanceTheme.colors.onBackground
                        )
                    )
                    if (storyList.isNotEmpty()) {
                        LazyColumn(
                            modifier = GlanceModifier.padding(4.dp)
                        ) {
                            items(storyList) {
                                StoryContent(storyUi = it)
                            }
                        }
                    } else {
                        Box(
                            modifier = GlanceModifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Story is empty, open and login to main App!",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = GlanceTheme.colors.onBackground,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = GlanceModifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
