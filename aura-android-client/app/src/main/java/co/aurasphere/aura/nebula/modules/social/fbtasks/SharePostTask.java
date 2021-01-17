package co.aurasphere.aura.nebula.modules.social.fbtasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

/**
 * Created by Donato on 19/04/2016.
 */
public class SharePostTask extends BaseFbTask{

    @Override
    protected String doInBackground(Object... params) {
        context = (Context) params[0];
        postClient = (FacebookClient) params[1];
        String postId = (String) params[2];
        String message = (String) params[3];
        if (message != null && !message.isEmpty())
            postClient.publish("me/feed", FacebookType.class, Parameter.with(
                    "message", message), Parameter.with("link",
                    "https://www.facebook.com/" + postId));
        else
            postClient.publish("me/feed", FacebookType.class, Parameter.with(
                    "link", "https://www.facebook.com/" + postId));
        return postId;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, "Post shared on your profile with id " + s, Toast.LENGTH_SHORT);
    }
}
