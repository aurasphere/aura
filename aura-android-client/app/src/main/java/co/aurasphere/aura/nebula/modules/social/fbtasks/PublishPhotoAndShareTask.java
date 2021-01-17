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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Donato on 19/04/2016.
 */
public class PublishPhotoAndShareTask extends BaseFbTask{

    @Override
    protected String doInBackground(Object... params) {
        context = (Context) params[0];
        postClient = (FacebookClient) params[1];
        InputStream photo = (InputStream) params[2];
        String mainMessage = (String) params[3];

        Log.d("FacebookController", "* Binary file publishing *");
        FacebookType response = null;
        if (mainMessage != null && !mainMessage.isEmpty())
            response = postClient.publish("me/photos", FacebookType.class,
                    BinaryAttachment.with("image", photo),
                    Parameter.with("message", mainMessage));
        else
            response = postClient.publish("me/photos", FacebookType.class,
                    BinaryAttachment.with("image", photo));
        Log.d("FacebookController", "Published photo ID: " + response.getId());
        String postId = response.getId();

        FacebookClient shareClient = (FacebookClient) params[4];
        String shareMessage = (String) params[5];
        Set<String> tagList = (HashSet<String>) params[6];
        List<Parameter> parameters = new ArrayList<Parameter>();

        parameters.add(Parameter.with("link", "https://www.facebook.com/" + postId));
        if(tagList != null && !tagList.isEmpty()){
            List<String> tags = new ArrayList<String>();
            tags.addAll(tagList);
            parameters.add(Parameter.with("tags", tags));
        }
        if (shareMessage != null && !shareMessage.isEmpty()){
            parameters.add(Parameter.with("message", shareMessage));
        }
//            shareClient.publish("me/feed", FacebookType.class, Parameter.with(
//                    "message", shareMessage), Parameter.with("link",
//                    "https://www.facebook.com/" + postId));
//        else
        shareClient.publish("me/feed", FacebookType.class, parameters.toArray(new Parameter[parameters.size()]));
        return postId;

    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT);
    }
}
