package dk.tv2.jobberwocky.job.creator;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import dk.tv2.jobberwocky.job.DemoJob;
import dk.tv2.jobberwocky.job.HourlyJob;

/**
 * @author Lars Nielsen <larn@tv2.dk>
 */
public class JobberCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case DemoJob.TAG:
                return new DemoJob();
            case HourlyJob.TAG:
                return new HourlyJob();
            default:
                return null;
        }
    }
}
