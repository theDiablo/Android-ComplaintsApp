package phoenix.iitdcomplaints;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gautam on 3/27/2016.
 */
public class JsonRequest
        extends JsonObjectRequest
{

    public JsonRequest(int method,String url,JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                       Response.ErrorListener errorListener) {
        super(method,url,jsonRequest, listener, errorListener);

    }


    /* (non-Javadoc)
     * @see com.android.volley.toolbox.StringRequest#parseNetworkResponse(com.android.volley.NetworkResponse)
     */
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        // since we don't know which of the two underlying network vehicles
        // will Volley use, we have to handle and store session cookies manually
        AppController.getInstance().checkSessionCookie(response.headers);

        return super.parseNetworkResponse(response);
    }

    /* (non-Javadoc)
     * @see com.android.volley.Request#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        AppController.getInstance().addSessionCookie(headers);

        return headers;
    }
}
