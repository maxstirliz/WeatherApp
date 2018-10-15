package lymansky.artem.weatherapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import lymansky.artem.weatherapp.services.ScheduledJobService;

public class AutoUpdateUtils {

    private static final int REMINDER_INTERVAL_HOURS = 1;
    private static final int REMINDER_GO_OFF_HOURS = 2;
    private static final int REMINDER_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(REMINDER_INTERVAL_HOURS);
    private static final int REMINDER_GO_OFF_SECONDS = (int) TimeUnit.HOURS.toSeconds(REMINDER_GO_OFF_HOURS);

    private static final String JOB_TAG = "update-weather-job-tag";
    private static boolean sInitialized;

    synchronized public static void scheduleWeatherUpdater(@NonNull Context context) {

        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job updateWeatherJob = dispatcher.newJobBuilder()
                .setService(ScheduledJobService.class)
                .setTag(JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_GO_OFF_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(updateWeatherJob);

        sInitialized = true;
    }
}
