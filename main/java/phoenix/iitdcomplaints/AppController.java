package phoenix.iitdcomplaints;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Map;
import java.util.Set;
//this class stores cookies to keep a track of ongoing session
//so that data is not lost and seesion id's cookies can be preserved
//Every time a request is to be sent to server a previously created instance of this class is used
//to get the Request Cookies which saves the session ID.
//
public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "session_id_iitdcomplaints";
    private RequestQueue mRequestQueue;
    private static AppController mInstance;
    private SharedPreferences _preferences;
    private Context ctxn;

    public AppController(Context ctx){
        super.onCreate();
        ctxn=ctx;
        mInstance = this;
        if(mInstance==null){
            _preferences=null;
        }
        else
            _preferences = PreferenceManager.getDefaultSharedPreferences(ctx);


    }
    @Override
    public void onCreate() {
        super.onCreate();

    }

/*    public boolean isPendingToRequest( final Object tag ) {

        final Object mObject = new Mirror().on( getRequestQueue() ).get().field( "mCurrentRequests" );

        final Set<Request<?>> mCurrentRequests = ( Set<Request<?>> ) mObject;

        for ( final Request<?> request : mCurrentRequests ) {

            Log.d("tagIsPendingToRequest ", "tag: " + request.getTag());

            if ( request.getTag().equals( tag ) ) {

                Log.d( "tagIsPendingToRequest ", "Pendingtag: " + request.getTag() + " mytag:" + tag );
                return true;
            }
        }

  */

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(ctxn);
        }

        return mRequestQueue;
    }

    public final void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor prefEditor = _preferences.edit();
                prefEditor.putString(SESSION_COOKIE, cookie);
                prefEditor.commit();
            }
        }
    }

    public final void addSessionCookie(Map<String, String> headers) {

        String sessionId = _preferences.getString(SESSION_COOKIE, "");
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
