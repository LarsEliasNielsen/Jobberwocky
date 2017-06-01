package dk.tv2.jobberwocky;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import org.joda.time.DateTime;

import java.util.Locale;

import dk.tv2.jobberwocky.job.HourlyJob;
import dk.tv2.jobberwocky.job.creator.JobberCreator;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final JobDispatcher jobDispatcher = new JobDispatcher(this);
        JobberCreator jobberCreator = new JobberCreator();
        final HourlyJob hourlyJob = (HourlyJob) jobberCreator.create(HourlyJob.TAG);

        Button scheduleJobButton = (Button) findViewById(R.id.schedule_job_button);
        Button cancelJobButton = (Button) findViewById(R.id.cancel_job_button);
        Button getJobsButton = (Button) findViewById(R.id.get_jobs_button);

        scheduleJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DemoJob.schedulePeriodicJob(12, 48);
//                HourlyJob.scheduleHourlyJob();
//                jobDispatcher.scheduleJob();
                hourlyJob.scheduleHourlyJob();
                Toast.makeText(v.getContext(), "Scheduling hourly job", Toast.LENGTH_SHORT).show();
            }
        });
        cancelJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DemoJob.cancelAllJobsForTag(DemoJob.TAG);
//                HourlyJob.cancelAllJobsForTag(HourlyJob.TAG);
//                jobDispatcher.cancelJob();
                hourlyJob.cancelAllJobs();
                Toast.makeText(v.getContext(), "Canceling all jobs", Toast.LENGTH_SHORT).show();
            }
        });
        getJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (JobRequest jobRequest : JobManager.instance().getAllJobRequests()) {
                    String log = String.format(Locale.getDefault(), "%s (%d) start at %s, end at %s",
                            jobRequest.getTag(), jobRequest.getJobId(),
                            new DateTime(DateTime.now().getMillis() + jobRequest.getStartMs()).toString(),
                            new DateTime(DateTime.now().getMillis() + jobRequest.getEndMs()).toString());
                    Log.d(LOG_TAG, log);
                    Toast.makeText(v.getContext(), log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
