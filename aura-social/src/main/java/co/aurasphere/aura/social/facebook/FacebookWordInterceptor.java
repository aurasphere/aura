package co.aurasphere.aura.social.facebook;

import org.springframework.stereotype.Component;

@Component
public class FacebookWordInterceptor {

	private long lastReplyTime = 0;

	private final long delay = 300000; // 5 min

	String findKeywords(String message) {
		String lowercaseMessage = message.toLowerCase();
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastReplyTime > delay) {
			if (lowercaseMessage.matches(".*ciao.*")) {
				lastReplyTime = currentTime;
				return "Ciao.";
			}
		}
		return "";
	}

}
