package dk.tv2.jobberwocky.job;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.joda.time.DateTime;

import java.util.Locale;

import dk.tv2.jobberwocky.MainActivity;
import dk.tv2.jobberwocky.R;

/**
 * @author Lars Nielsen <larn@tv2.dk>
 */
public class FirebaseJob extends JobService {

    private static final String LOG_TAG = FirebaseJob.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(LOG_TAG, "onStartJob: " + params.getTag());

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DateTime.now().getSecondOfDay(), makeNotification(getApplicationContext()));

        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(LOG_TAG, "onStopJob: " + params.getTag());

        return false; // Answers the question: "Should this job be retried?"
    }

    private Notification makeNotification(Context context) {
        String title = "Hourly Firebase job";
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
                .setGroup("recurring_firebase_job")
                .setContentTitle(title)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingOpenIntent)
                .setAutoCancel(true)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .build();
    }
}
