package com.ev.dailyquotes.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Vidar on 12/30/16.
 */
public class AppController extends Application {

    private static final String TAG = "AppController";
    private static AppController mInstance;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public AppController() {
    }

    private RequestQueue mRequestQueue;
    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag){
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
