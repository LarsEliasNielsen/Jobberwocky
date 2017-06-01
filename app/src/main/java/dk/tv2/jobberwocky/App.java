package dk.tv2.jobberwocky;

import android.app.Application;

import com.evernote.android.job.JobManager;

import net.danlew.android.joda.JodaTimeAndroid;

import dk.tv2.jobberwocky.job.creator.JobberCreator;

/**
 * @author Lars Nielsen <larn@tv2.dk>
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        JobManager.create(this).addJobCreator(new JobberCreator());

        JodaTimeAndroid.init(this);
    }
}
