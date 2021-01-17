package co.aurasphere.aura.nebula.modules.social.fbtasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.restfb.BinaryAttachment;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

import java.io.InputStream;

/**
 * Created by Donato on 19/04/2016.
 */
public class PublishPhotoTask extends BaseFbTask {

    @Override
    protected String doInBackground(Object... params) {
        context = (Context) params[0];
        postClient = (FacebookClient) params[1];
        InputStream photo = (InputStream) params[2];
        String message = (String) params[3];

        Log.d("FacebookController", "* Binary file publishing *");
        FacebookType response = null;
        if (message != null && !message.isEmpty())
            response = postClient.publish("me/photos", FacebookType.class,
                    BinaryAttachment.with("image", photo),
                    Parameter.with("message", message));
        else
            response = postClient.publish("me/photos", FacebookType.class,
                    BinaryAttachment.with("image", photo));
        Log.d("FacebookController","Published photo ID: " + response.getId());
        return response.getId();
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT);
    }
}
