package dk.tv2.jobberwocky.job.dispatch;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import org.joda.time.DateTime;

import dk.tv2.jobberwocky.job.FirebaseJob;


/**
 * @author Lars Nielsen <larn@tv2.dk>
 */
public class JobDispatcher {

    public static final String TAG = "firebase-hourly-job";
    private static final String LOG_TAG = JobDispatcher.class.getSimpleName();

    private FirebaseJobDispatcher mDispatcher;

    public JobDispatcher(Context context) {
        mDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
    }

    public void scheduleJob() {
        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("hello", "world");

        DateTime now = DateTime.now();
        DateTime next = now.plusMinutes(1);
        int differenceS = next.getSecondOfDay() - now.getSecondOfDay();

        Log.i(LOG_TAG, "Scheduling job to run at: " + next.toString());

        Job hourlyJob = mDispatcher.newJobBuilder()
                .setService(FirebaseJob.class)
                .setTag(TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(differenceS, differenceS + 30))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(
                        Constraint.ON_ANY_NETWORK
                )
                .setExtras(myExtrasBundle)
                .build();

        mDispatcher.mustSchedule(hourlyJob);
    }

    public void cancelJob() {
        Log.i(LOG_TAG, "Canceling job");
        mDispatcher.cancel(TAG);
    }
}
