package tk.jonathancowling.inventorytracker.report

import android.app.job.JobParameters
import android.app.job.JobService

class ReportJobService : JobService() {
    override fun onStopJob(params: JobParameters) = false

    override fun onStartJob(params: JobParameters): Boolean {
        // TODO: FirebaseMessaging.getInstance().send(null)
        return false
    }
}