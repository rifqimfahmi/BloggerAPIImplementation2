package renotekno.com.bloggerapiimplementation2;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by zcabez on 20/08/2017.
 */

public class SingleVolley {
    private static SingleVolley mInstance;
    private static Context mCtx;
    private RequestQueue requestQueue;

    public SingleVolley(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized SingleVolley getIstance(Context context){
        if (mInstance == null) {
            mInstance = new SingleVolley(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

}
