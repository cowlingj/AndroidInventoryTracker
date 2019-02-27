package tk.jonathancowling.inventorytracker.report

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import org.threeten.bp.Duration
import org.threeten.bp.temporal.ChronoUnit

class BackgroundJob {
    fun start(c: Context) {
        c.applicationContext.apply {
            (getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler)
                .schedule(
                    JobInfo.Builder(0, ComponentName(this, ReportJobService::class.java))
                        .setPeriodic(Duration.of(1, ChronoUnit.WEEKS).toMillis())
                        .setPersisted(false)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                        .build()
                )
        }
    }
}