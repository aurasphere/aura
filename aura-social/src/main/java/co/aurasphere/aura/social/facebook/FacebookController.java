package co.aurasphere.aura.social.facebook;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import co.aurasphere.aura.common.module.dto.ServiceRequest;
import co.aurasphere.aura.common.module.dto.ServiceResponse;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.CategorizedFacebookType;
import com.restfb.types.Comment;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;
import com.restfb.types.Post.Comments;

@Controller
public class FacebookController {

	private static final Logger logger = LoggerFactory
			.getLogger(FacebookController.class);

	@Inject
	private FacebookParallelService service;

	// TODO: in file properties.
	private static final String AURA_FACEBOOK_NAME = "Aura";

	private static final String INTERCEPTED_SIGNATURE = "["
			+ AURA_FACEBOOK_NAME + "]";

	// XXX: Removed.
	private String AURA_TOKEN;
	
	// XXX: Removed.
	private String MY_TOKEN;

	// XXX: Removed.
	private Object MY_FACEBOOK_NAME;

	private static FacebookClient auraClient;

	private static FacebookClient myClient;

	private static FacebookClient publishClient;

	private static FacebookClient readingClient;

	private CommentOperations commentOperations;

	@Inject
	private FacebookWordInterceptor randomWords;

	public FacebookController() {
		auraClient = new DefaultFacebookClient(AURA_TOKEN, Version.VERSION_2_4);
		myClient = new DefaultFacebookClient(MY_TOKEN, Version.VERSION_2_4);
		commentOperations = new CommentOperations(myClient);
		readingClient = myClient;
		publishClient = auraClient;
	}

	void scanTimeline() {
		// Reads the timeline.
		Connection<Post> timeline = null;
		timeline = readingClient.fetchConnection("me/feed", Post.class,
				Parameter.with("limit", 10));
		if (timeline != null) {
			for (Post post : timeline.getData()) {
				// Post data.
				String postMessage = post.getMessage();
				CategorizedFacebookType from = post.getFrom();
				String postId = post.getId();
				String postAuthor = null;
				if (from != null) {
					postAuthor = from.getName();
				}
				Comments comments = post.getComments();
				List<Comment> commentList = null;
				if (comments != null) {
					commentList = comments.getData();
				} else {
					commentList = getComments(postId);
				}
//				System.out.println(postMessage);

				// If the post is a command, doesn't have a reply and
				// it's mine, I reply.
				if (postMessage != null && isAuth(postAuthor)
						&& !alreadyAnswered(commentList, 0)) {
					ServiceRequest command = getServiceRequest(postMessage);
					if (command != null) {
						processRequest(command, post, null);
						continue;
					} 
				}
				if (commentList != null) {
					for (Comment comment : commentList) {
						// Comment data.
						String commentMessage = comment.getMessage();
						String commentAuthor = comment.getFrom().getName();
						int commentIndex = commentList.indexOf(comment);

//						System.out.println(commentAuthor);
//						System.out.println("\t * " + commentMessage);
						// If the comment is a command, doesn't have a reply and
						// it's mine, I reply.
						if (isAuth(commentAuthor)
								&& !alreadyAnswered(commentList, commentIndex)) {
							ServiceRequest command = getServiceRequest(commentMessage);
							if (command != null) {
								processRequest(command, post, comment);
							} else {
								// Replies to random words just for fun.
								String randomWordsReply = randomWords.findKeywords(commentMessage);
								if(!randomWordsReply.isEmpty()){
									replyToPost(postId, randomWordsReply);
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean isAuth(String author) {
		// TODO
		return true || isMine(author);
	}

	private ServiceRequest getServiceRequest(String message) {
		if (message == null) {
			return null;
		}
		// TODO: generalize with AUTH
		String pattern = "^\\[UDT\\] .*";
		if (message.matches(pattern)) {
			HashMap<String, String> arguments = new HashMap<String, String>();
			arguments.put("text", message.substring(5));
			return new ServiceRequest("udt", arguments);
		}
		return null;
	}

	/**
	 * Returns true if the author was me, false otherwise.
	 * 
	 * @param author
	 * @return
	 */
	private boolean isMine(String author) {
		return author.equals(MY_FACEBOOK_NAME);
	}

	/**
	 * Detects if a request has already been replied to.
	 * 
	 * @param comments
	 *            the comments to this post.
	 * @param commentIndex
	 * @return true if the post has already been replied to, false otherwise.
	 */
	private boolean alreadyAnswered(List<Comment> comments, int commentIndex) {
		// If the comments are empty or null, the post hasn't been replied.
		if (comments == null || comments.isEmpty()) {
			return false;
		}
		List<Comment> commentsFromLastRequest = comments.subList(commentIndex,
				comments.size());
		for (Comment c : commentsFromLastRequest) {
			if (c.getFrom().getName().equals(AURA_FACEBOOK_NAME)) {
				return true;
			}
		}
		return false;
	}

	private void processRequest(ServiceRequest request, Post p, Comment c) {
		String messageResponse;
		ServiceResponse response = service.getServiceBus().requestService(
				request);
		if (response.isOutcome()) {
			messageResponse = response.getResult();
		} else {
			messageResponse = response.getMessage();
		}

		replyToPost(p.getId(), messageResponse);

	}

	private void autopost(String message) {
		auraClient.publish(createGrapple() + "/comments", FacebookType.class,
		// BinaryAttachment.with(imageName, fis),
				Parameter.with("message", message));
	}

	private String createGrapple() {
		FacebookType response = myClient.publish("me/feed", FacebookType.class,
		// BinaryAttachment.with(imageName, fis),
				Parameter.with("message", "[Aura Grapple]"));
		return response.getId();
	}

	private void replyToPost(String postId, String message) {
		publishClient.publish(postId + "/comments", String.class,
				Parameter.with("message", message));
	}

	public List<Comment> getComments(String postId) {
		Connection<Comment> allComments = myClient.fetchConnection(postId
				+ "/comments", Comment.class, Parameter.with("fields", "message,from"));
		return allComments.getData();
	}

	// OLD INTERFACES
	/*
	 * 
	 * @Override public void write(Text string) { if
	 * (string.getParameters().get("post") != null) { String postId = ((Post)
	 * string.getParameters().get("post")).getId(); // If there's a post I can
	 * only comment publishClient.publish(postId + "/comments", String.class,
	 * Parameter.with("message", string.getText())); } else
	 * autopost("[Posted using Aura from " +
	 * extrapolator.getBullet().getRequest().getInputChannel()
	 * .getInterfaceName() + "] " + string.toString()); }
	 * 
	 * @Override public void post(Media m) {
	 * 
	 * File image = m.getFile(); FileInputStream fis = null; try { fis = new
	 * FileInputStream(image); } catch (FileNotFoundException e) {
	 * e.printStackTrace(); } FacebookType publishPhotoResponse = publishClient
	 * .publish(((Post) m.getParameters().get("post")).getId() + "/comments",
	 * FacebookType.class, BinaryAttachment.with(m.getFilePath(), fis));
	 * 
	 * }
	 * 
	 * @Override public URL save(Map<String, Object> parameters) { URL url =
	 * null; Post p = (Post) parameters.get("post"); p =
	 * myClient.fetchObject(p.getId(), Post.class, Parameter.with("fields",
	 * "full_picture")); try { url = new URL(p.getFullPicture()); } catch
	 * (MalformedURLException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return url; }
	 * 
	 * @Override public String customOperationsManager(Bullet bullet) { if
	 * (bullet.getRequest().getInputSentence().toString() .contains("switch")) {
	 * if (publishClient == myClient) publishClient = auraClient; else
	 * publishClient = myClient; return "Switch done"; } return
	 * "Sorry! Didn't get that!"; }
	 */
}
