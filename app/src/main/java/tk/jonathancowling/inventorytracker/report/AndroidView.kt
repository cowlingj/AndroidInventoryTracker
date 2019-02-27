package tk.jonathancowling.inventorytracker.report

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.report_fragment.*
import tk.jonathancowling.inventorytracker.R

class AndroidView : Fragment() {

    private lateinit var viewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.report_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        report_bar_chart.data = BarData(
            BarDataSet(
                listOf(
                    BarEntry(0f, arrayOf(3f, -2f).toFloatArray()),
                    BarEntry(1f, arrayOf(2f, -1f).toFloatArray())
                ), "LABEL"
            ).apply {
                colors = values.flatMap { entry -> entry.yVals.toList().map { if (it > 0) Color.GREEN else Color.RED } }
            })
        report_bar_chart.axisRight.isEnabled = false
        report_bar_chart.axisLeft.textSize = 20f
        report_bar_chart.data.setValueTextSize(20f)
        report_bar_chart.xAxis.isEnabled = false
        report_bar_chart.legend.textSize = 20f
        report_bar_chart.description.textSize = 20f
        report_bar_chart.invalidate()
    }

}
