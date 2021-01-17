package co.aurasphere.aura.nebula.modules.social.fbtasks;

import android.content.Context;

import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;

import co.aurasphere.aura.nebula.modules.social.FacebookController;

/**
 * Created by Donato on 23/04/2016.
 */
public class FetchFriendsTask extends BaseFbTask {
    @Override
    protected String doInBackground(Object... params) {
        context = (Context) params[0];
        postClient = (FacebookClient) params[1];
        FacebookController controller = (FacebookController) params[2];
        Connection<User> friends = postClient.fetchConnection("me/taggable_friends", User.class, Parameter.with("limit", 1000));
        controller.setMyFriendList(friends.getData());
        return null;
    }

}
