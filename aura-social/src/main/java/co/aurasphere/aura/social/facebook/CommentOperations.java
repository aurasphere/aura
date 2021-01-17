package co.aurasphere.aura.social.facebook;

import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.experimental.api.Comments;
import com.restfb.types.Comment;
import com.restfb.types.GraphResponse;

/**
 * Class which contains any possible operation on Facebook Comments.
 * 
 * @author Donato Rimenti
 *
 */
public class CommentOperations implements Comments {

	private final FacebookClient facebookClient;

	CommentOperations(FacebookClient facebookClient) {
		this.facebookClient = facebookClient;
	}

	public boolean hide(String commentId) {

		Comment comment = facebookClient.fetchObject(commentId, Comment.class,
				Parameter.with("fields", "can_hide"));
		if (comment.getCanHide()) {
			GraphResponse response = facebookClient.publish(commentId,
					GraphResponse.class, Parameter.with("is_hidden", "true"));

			return response.isSuccess();
		}
		return false;
	}

	public boolean unhide(String commentId) {
		Comment comment = facebookClient.fetchObject(commentId, Comment.class,
				Parameter.with("fields", "can_hide"));
		if (comment.getCanHide()) {
			GraphResponse response = facebookClient.publish(commentId,
					GraphResponse.class, Parameter.with("is_hidden", "false"));

			return response.isSuccess();
		}
		return false;
	}

	public Comment get(String commentId) {
		return facebookClient.fetchObject(commentId, Comment.class);
	}

	public boolean delete(String commentId) {
		return facebookClient.deleteObject(commentId);
	}

	public boolean update(String commentId, String updatedMessage) {
		boolean b = false;
		try {
			b = facebookClient.publish(commentId, Boolean.class,
					Parameter.with("message", updatedMessage));
		} catch (FacebookOAuthException e) {
			e.printStackTrace();
			System.out.println("Message : " + e.getErrorMessage());
		}
		return b;
	}

	public boolean like(String commentId) {
		GraphResponse response = facebookClient.publish(commentId + "/likes",
				GraphResponse.class);
		return response.isSuccess();
	}

	public boolean unLike(String commentId) {
		return facebookClient.deleteObject(commentId + "/likes");
	}

}
