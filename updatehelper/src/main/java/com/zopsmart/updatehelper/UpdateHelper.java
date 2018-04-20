package com.zopsmart.updatehelper;

import android.content.Context;
import android.util.Log;

import com.zopsmart.updatehelper.exception.ZSException;
import com.zopsmart.updatehelper.interfaces.UpdateListener;
import com.zopsmart.updatehelper.pojo.UpdateConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UpdateHelper {

    private Context context;
    private static final String TAG = UpdateHelper.class.getName();
    private static final String HOST_URL = "http://apps.zopsmart.com/android/info/";
    private static final String APP_NAME = "name";
    private static final String LAST_SUPPORTED_VERSION = "lastSupportedVersion";
    private static final String LATEST_VERSION = "latestVersion";

    private UpdateListener updateListener;

    public void listenForUpdates(final String packageName, final UpdateListener updateListener){
        this.updateListener = updateListener;
        io.reactivex.Observable<UpdateConfig> updateObservable = io.reactivex.Observable.defer(new Callable<ObservableSource<? extends UpdateConfig>>() {
            @Override
            public ObservableSource<? extends UpdateConfig> call() throws Exception {
                return io.reactivex.Observable.just(getUpdateConfig(packageName));
            }
        });

        UpdateObserver<UpdateConfig> updateConfigUpdateObserver =  new UpdateObserver<UpdateConfig>() {
            @Override
            public void onNext(UpdateConfig updateConfig) {
                Log.i(TAG, updateConfig.toString());
                updateListener.success(updateConfig);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage());
                updateListener.error(e);
            }
        };

        updateObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateConfigUpdateObserver);
    }

    private JSONObject getUpdateJSONObject(String packageName) throws JSONException, ZSException, IOException {
        UpdateRequest request =  new UpdateRequest(UpdateRequest.Method.GET, HOST_URL + packageName,null,null,context);
        return request.fetch();
    }

    private UpdateConfig getUpdateConfig(String packageName) throws JSONException, IOException, ZSException {
        JSONObject updateJSON = getUpdateJSONObject(packageName);
        int latestVersion = updateJSON.getInt(LATEST_VERSION);
        int lastSupportedVersion = updateJSON.getInt(LAST_SUPPORTED_VERSION);
        String name = updateJSON.getString(APP_NAME);
        UpdateConfig update = new UpdateConfig(latestVersion,name,lastSupportedVersion);
        return update;
    }

    public UpdateHelper(Context context) {
        this.context = context;
    }
}
