package co.aurasphere.aura.nebula.modules.social;

import android.content.Context;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import co.aurasphere.aura.nebula.modules.social.fbtasks.FetchFriendsTask;
import co.aurasphere.aura.nebula.modules.social.fbtasks.PublishMessageTask;
import co.aurasphere.aura.nebula.modules.social.fbtasks.PublishPhotoAndShareTask;
import co.aurasphere.aura.nebula.modules.social.fbtasks.PublishPhotoTask;
import co.aurasphere.aura.nebula.modules.social.fbtasks.SharePostTask;
import co.aurasphere.aura.nebula.modules.social.ioc.DaggerSocialComponent;
import co.aurasphere.aura.nebula.modules.social.ioc.SocialComponent;
import co.aurasphere.aura.nebula.modules.social.util.FacebookUserWrapper;
import co.aurasphere.aura.nebula.utils.PropertiesReader;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

import javax.inject.Inject;

public class FacebookController {

	@Inject
	PropertiesReader properties;

	public FacebookClient MYSELF;

	public FacebookClient LESDISSIA;

	private static FacebookController instance;

	private List<FacebookUserWrapper> myFriendList = new ArrayList<FacebookUserWrapper>();

	private FacebookController(){
		SocialComponent socialComponent = DaggerSocialComponent.builder().build();
		socialComponent.inject(this);
		properties.loadPropertyFile("facebook.properties");
		MYSELF = new DefaultFacebookClient(properties.getProperty("user0"));
		LESDISSIA = new DefaultFacebookClient(properties.getProperty("page0"));
		fetchFriends(MYSELF);
	}
	
	public void sharePost(Context context, FacebookClient client, String postId, String message) {
		new SharePostTask().execute(context, client, postId, message);
	}

	public void publishMessage(Context context, FacebookClient client, String message) {
		new PublishMessageTask().execute(context, client, message);
	}

	public void publishPhoto(Context context, FacebookClient client, InputStream photo,
			String message){
			new PublishPhotoTask().execute(context, client, photo, message);
	}

	public void publishPhotoAndShare(Context context, FacebookClient mainClient, InputStream photo,
							   String mainMessage, FacebookClient shareClient, String shareMessage, Set<String> tagList) {
			new PublishPhotoAndShareTask().execute(context, mainClient, photo, mainMessage, shareClient, shareMessage, tagList);
	}

	public void fetchFriends(FacebookClient mainClient) {
		new FetchFriendsTask().execute(null, mainClient, this);
	}

	public FacebookClient getClientById(String pageId){
		String token = properties.getProperty(pageId);
		return new DefaultFacebookClient(token);
	}

	public static FacebookController getInstance(){
		if(instance == null) {
			instance = new FacebookController();
		}
		return instance;
	}

	public List<FacebookUserWrapper> getMyFriendList() {
		return myFriendList;
	}

	public void setMyFriendList(List<User> myFriendList) {
		FacebookUserWrapper wrapper;
		for(User u : myFriendList){
			wrapper = new FacebookUserWrapper();
			wrapper.setUser(u);
			this.myFriendList.add(wrapper);
		}
		SocialFragment.refreshFriendList(this.myFriendList);
	}
}