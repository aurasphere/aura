package co.aurasphere.aura.nebula.modules.social.fbtasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

/**
 * Created by Donato on 19/04/2016.
 */
public class PublishMessageTask extends BaseFbTask{

    @Override
    protected String doInBackground(Object... params) {
        context = (Context) params[0];
        postClient = (FacebookClient) params[1];
        String message = (String) params[2];

        Log.d("FacebookController", "* Feed publishing *");

        FacebookType response = postClient.publish("me/feed",
                FacebookType.class, Parameter.with("message", message));

        Log.d("FacebookController","Published message ID: " + response.getId());
        return response.getId();
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT);
    }
}
