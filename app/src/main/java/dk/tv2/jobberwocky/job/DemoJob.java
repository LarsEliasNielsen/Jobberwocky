package dk.tv2.jobberwocky.job;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import org.joda.time.DateTime;

import java.util.Locale;

import dk.tv2.jobberwocky.MainActivity;
import dk.tv2.jobberwocky.R;

/**
 * @author Lars Nielsen <larn@tv2.dk>
 */
public class DemoJob extends Job {

    private static final String LOG_TAG = DemoJob.class.getSimpleName();
    private static final String HOUR_KEY = "hour_key";
    private static final String MINUTE_KEY = "minute_key";

    public static final String TAG = "demo_job_tag";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        Log.d(LOG_TAG, "Running job: " + params.getTag());
        Log.d(LOG_TAG, "Params; hour: " + params.getExtras().getInt(HOUR_KEY, 0) + ", minute: " + params.getExtras().getInt(MINUTE_KEY, 0));

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, makeNotification(getContext(), params.getExtras()));

        schedulePeriodicJob(params.getExtras().getInt(HOUR_KEY, 0), params.getExtras().getInt(MINUTE_KEY, 0));

        return Result.SUCCESS;
    }

    @Override
    protected void onReschedule(int newJobId) {
        super.onReschedule(newJobId);
    }

    public static int schedulePeriodicJob(int hours, int minutes) {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putInt(HOUR_KEY, hours);
        extras.putInt(MINUTE_KEY, minutes);

        DateTime now = DateTime.now();
        DateTime nextScheduledRun = now.withTimeAtStartOfDay()
//                .plusDays(1)
                .plusHours(hours)
                .plusMinutes(minutes);

        if (nextScheduledRun.isBefore(now)) {
            nextScheduledRun.plusDays(1);
        }

        Log.d(LOG_TAG, "Now: " + now.toString());
        Log.d(LOG_TAG, "Next: " + nextScheduledRun.toString());

        return new JobRequest.Builder(DemoJob.TAG)
//                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
//                .setExecutionWindow(60 * 1000, 5 * 60 * 1000)
                .setExact(nextScheduledRun.getMillis() - now.getMillis())
                .setPersisted(true)
                .setExtras(extras)
                .build()
                .schedule();
    }

    public static boolean cancelJob(int jobId) {
        return JobManager.instance().cancel(jobId);
    }

    public static int cancelAllJobsForTag(String jobTag) {
        return JobManager.instance().cancelAllForTag(jobTag);
    }

    public static int cancelAllJobs() {
        return JobManager.instance().cancelAll();
    }

    private Notification makeNotification(Context context, PersistableBundleCompat extras) {
        String title = "Demo Job";
        String content = String.format(Locale.getDefault(), "Job run at: %02d:%02d",
                extras.getInt(HOUR_KEY, 0), extras.getInt(MINUTE_KEY, 0));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Create pending-intent for notification-click,
        // that can be picked up in mainActivity, to log the click
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingOpenIntent = PendingIntent.getActivity(
                context, 0, intent,PendingIntent.FLAG_ONE_SHOT);

        return builder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentTitle(title)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingOpenIntent)
                .setAutoCancel(true)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .build();
    }
}
