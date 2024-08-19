package id.dev.core.presentation.design_system.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.dev.core.presentation.design_system.LogoIcon
import id.dev.core.presentation.design_system.StoryActTheme

@Composable
fun StoryActLogo(
    logo: ImageVector,
    modifier: Modifier = Modifier,
    text: String? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = logo,
            contentDescription = "Logo"
        )
        text?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun StoryActLogoPreview() {
    StoryActTheme {
        StoryActLogo(
            logo = LogoIcon,
            text = "test",
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        )
    }
}