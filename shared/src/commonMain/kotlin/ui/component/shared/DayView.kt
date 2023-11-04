package ui.component.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import ui.theme.Dimens

@Composable
fun RenderDayScreen(title:String, description:String) {
    Column (modifier = Modifier.padding(start = Dimens.space, end = Dimens.space), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, style = MaterialTheme.typography.titleSmall)
        Text(description,style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
    }
}
