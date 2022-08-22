package com.live.location.Service;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.ListenableWorker;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.live.location.Activities.MainActivity;
import com.live.location.R;
import com.live.location.Util.PreferencesUtil;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.live.location.Util.Utils.removeCode;


public class LocationWorker extends ListenableWorker {

    Context mContext;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    LocationManager locationManager;
    PreferencesUtil preferencesUtil;
    double user_lat, user_lng;
    String name, date;
    PendingIntent pendingIntent;
    NotificationManager notificationManager;

    public LocationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);

        this.mContext = context;
        this.notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    @SuppressLint("MissingPermission")
    @NonNull
    @NotNull
    @Override
    public ListenableFuture<Result> startWork() {

        preferencesUtil = new PreferencesUtil(mContext);

        String phn = getInputData().getString("myPhn");

        if (phn != null) {

            try {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        if (location != null) {



                                            Log.i("loc", location.getLatitude() + " " + location.getLongitude());

                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("lat", location.getLatitude());
                                            hashMap.put("lng", location.getLongitude());

                                            firebaseFirestore.collection("Member").document(phn).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });


                        }
                    }


                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(@NonNull String provider) {

                    }

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {

                    }
                });
            } catch (Exception e) {

            }

        }

        return new ListenableFuture<Result>() {
            @Override
            public void addListener(Runnable listener, Executor executor) {

            }

            @Override
            public boolean cancel(boolean b) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {

                ForegroundInfo foregroundInfo = new ForegroundInfo(1, createNotification());
                setForegroundAsync(foregroundInfo);

                return false;
            }

            @Override
            public Result get() throws ExecutionException, InterruptedException {
                return null;
            }

            @Override
            public Result get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }
        };
    }

    public Notification createNotification() {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(String.valueOf(1), "Live Location", NotificationManager.IMPORTANCE_HIGH);
            this.notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent resultIntent = new Intent(mContext, MainActivity.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            pendingIntent = PendingIntent.getActivity(mContext, 1, resultIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        }
        else {
            pendingIntent = PendingIntent.getActivity(mContext, 1, resultIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        }



        builder = new NotificationCompat.Builder(mContext, String.valueOf(1));
        builder.setContentTitle("Live Location");
        builder.setContentText("You are sharing live location");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setSound(defaultSoundUri);
        return builder.build();
    }
}
