package com.example.roseledger.ui.reports.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.roseledger.util.DateFilter
import com.example.roseledger.util.formatAmountWithCommas
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

import java.text.DateFormatSymbols

@Composable
fun TotalExpenseCard(
    total: Double,
    dateRange: String,
    chartData: List<ChartEntry>,
    currentFilter: DateFilter
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Total Expenses",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            // Display the date range
            Text(
                text = dateRange,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                // Format the amount to the two decimal places
                text = formatAmountWithCommas(total),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            if (chartData.isNotEmpty()) {
                val chartProducer = remember(chartData) {
                    ChartEntryModelProducer(chartData)
                }

                val axisLabelColor = MaterialTheme.colorScheme.onSurfaceVariant

                Chart(
                    chart = lineChart(
                        lines = listOf(
                            lineSpec(
                                lineColor = MaterialTheme.colorScheme.primary,
                                lineThickness = 4.dp
                            )
                        )
                    ),
                    chartModelProducer = chartProducer,
                    startAxis = rememberStartAxis(
                        label = axisLabelComponent(color = axisLabelColor),
                        axis = lineComponent(color = MaterialTheme.colorScheme.outlineVariant)
                    ),
                    bottomAxis = rememberBottomAxis(
                        label = axisLabelComponent(color = axisLabelColor),
                        axis = lineComponent(color = MaterialTheme.colorScheme.outlineVariant),
                        valueFormatter = { value, _ ->
                            if (currentFilter == DateFilter.YEAR ) {
                                DateFormatSymbols().shortMonths[value.toInt()]
                            } else {
                                value.toInt().toString()
                            }
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 24.dp)
                )
            }
        }
    }
}