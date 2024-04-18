package co.yml.charts.common.components.accessibility

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.ui.barchart.models.GroupBar


/**
 * Composable to display each group bar item for given group bar chart.
 * @param axisLabelDescription: Axis label description.
 * @param groupBar: Details of each group bar.
 * @param barColorPaletteList: List of each bar colors for a given group bar.
 * @param titleTextSize: TextUnit title font size
 * @param descriptionTextSize: TextUnit description font size
 */
@Composable
fun GroupBarInfo(
    groupBar: GroupBar,
    axisLabelDescription: String,
    barColorPaletteList: List<Color>,
    titleTextSize: TextUnit,
    descriptionTextSize: TextUnit
) {
    // Merge elements below for accessibility purposes
    Row(modifier = Modifier
        .padding(start = 10.dp, end = 10.dp)
        .clickable { }
        .semantics(mergeDescendants = true) {}, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(axisLabelDescription, fontSize =titleTextSize)
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
        ) {
            groupBar.barList.forEachIndexed { index, value ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .background(barColorPaletteList[index])
                            .size(20.dp)
                    )
                    Text(value.description, fontSize = descriptionTextSize)
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}
