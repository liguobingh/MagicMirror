package viomi.com.mojingface.service;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import viomi.com.mojingface.util.SystemUtil;


/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: viomi_face
 * @Package: viomi.com.mojingface.service
 * @ClassName: SpeechServiceJobWakeUpService
 * @Description: 用于判断Service是否被杀死
 * @Author: randysu
 * @CreateDate: 2019/3/25 10:48 AM
 * @UpdateUser:
 * @UpdateDate: 2019/3/25 10:48 AM
 * @UpdateRemark:
 * @Version: 1.0
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SpeechServiceJobWakeUpService extends JobService {

    private static final String TAG = SpeechServiceJobWakeUpService.class.getName();

    private static final int JOBWAKEUP_ID = 101;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        JobInfo.Builder builder = new JobInfo.Builder(JOBWAKEUP_ID, new ComponentName(this,
                SpeechServiceJobWakeUpService.class));
        builder.setPeriodic(2000);

        JobScheduler jobScheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());

        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        boolean serviceIsAlive = SystemUtil.isServiceAlive(this, SpeechInitService.class.getName());
        Log.i(TAG, "serviceIsAlive:" + serviceIsAlive);
        if (!serviceIsAlive) {
            startService(new Intent(this, SpeechInitService.class));
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}
