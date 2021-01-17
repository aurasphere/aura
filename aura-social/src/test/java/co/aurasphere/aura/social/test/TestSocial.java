package co.aurasphere.aura.social.test;

import org.junit.Ignore;
import org.junit.Test;

import co.aurasphere.aura.social.facebook.FacebookController;

public class TestSocial {
	
	@Test
	public void testPattern(){
		String message = "[UDT] Test this should match";
		String message2 = "This should not match";
		String message3 = "[UDT] ";
		String message4 = "[UDT]";
		
		String pattern = "^\\[UDT\\] .*";
		System.out.println(message.matches(pattern));
		System.out.println(message2.matches(pattern));
		System.out.println(message3.matches(pattern));
		System.out.println(message4.matches(pattern));

	}
	
	@Test
	@Ignore
	public void testComments(){
		FacebookController controller = new FacebookController();
		System.out.println(controller.getComments("POST_ID"));
	}
	
}
