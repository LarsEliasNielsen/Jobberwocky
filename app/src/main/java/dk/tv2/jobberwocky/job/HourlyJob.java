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

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import org.joda.time.DateTime;

import java.util.Locale;

import dk.tv2.jobberwocky.MainActivity;
import dk.tv2.jobberwocky.R;

/**
 * @author Lars Nielsen <larn@tv2.dk>
 */
public class HourlyJob extends Job {

    public static final String TAG = "hourly_job";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DateTime.now().getMillisOfDay(), makeNotification(getContext()));

        scheduleHourlyJob();

        return Result.SUCCESS;
    }

    public int scheduleHourlyJob() {
        DateTime now = DateTime.now();
        DateTime nextHour = now
                .plusHours(1)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);

        return new JobRequest.Builder(HourlyJob.TAG)
                .setExact(nextHour.getMillis() - now.getMillis())
                .setPersisted(true)
                .setUpdateCurrent(false)
                .build()
                .schedule();
    }

    public boolean cancelJob(int jobId) {
        return JobManager.instance().cancel(jobId);
    }

    public int cancelAllJobsForTag(String jobTag) {
        return JobManager.instance().cancelAllForTag(jobTag);
    }

    public int cancelAllJobs() {
        return JobManager.instance().cancelAll();
    }

    private Notification makeNotification(Context context) {
        String title = "Hourly Job";
        String content = String.format(Locale.getDefault(), "Job run at: %s",
                DateTime.now().toString());

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
                .setPriority(Notification.PRIORITY_HIGH)
                .build();
    }
}
