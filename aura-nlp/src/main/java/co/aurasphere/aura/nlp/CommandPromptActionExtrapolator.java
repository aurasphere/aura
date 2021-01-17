package co.aurasphere.aura.nlp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.aurasphere.aura.core.Aura;
import co.aurasphere.aura.core.Aura.Operation;
import co.aurasphere.aura.core.actions.Action;
import co.aurasphere.aura.core.actions.impl.live.Post;
import co.aurasphere.aura.core.actions.impl.live.Say;
import co.aurasphere.aura.core.io.InputInterface;
import co.aurasphere.aura.core.io.MultimediaInputInterface;
import co.aurasphere.aura.core.io.MultimediaOutputInterface;
import co.aurasphere.aura.core.io.OutputInterface;
import co.aurasphere.aura.core.io.impl.live.IOGate;
import co.aurasphere.aura.core.types.Media;
import co.aurasphere.aura.core.util.RegexUtil;
import co.aurasphere.aura.model.business.Bullet;

@Component
public class CommandPromptActionExtrapolator {

	@Autowired
	private IOGate gate;

	@Autowired
	private RegexUtil regex;
	
	@Autowired
	private Linguistics linguistics;

	private Bullet bullet;

	private OutputInterface outputInterface;

	private Operation lastOperation;
	
	private Map<String, Object> parameters;

	public List<Action> getActions(Bullet bullet) {
		this.bullet = bullet;
		this.outputInterface = gate.getDefaultOutputInterface(bullet
				.getRequest().getInputChannel());
		this.parameters = bullet.getRequest().getParameters();
		Action a = null;
		List<Action> actions = new ArrayList<Action>();
		String request = bullet.getRequest().toString();
		StringTokenizer tokenizer;

		// Cleans the request deleting her name and any leading or trailing
		// punctuation.
		request = request.replaceFirst(Aura.Self._NAME.getValue(), "");
		request = regex.removeLeadingTrailingPunctuation(request);

		// Splits the request in a List of subrequests
//		String[] requests = regex.splitCoordinateSentences(request);
//
//		for (String s : requests) {
			for (Operation o : Aura.Operation.values()) {
				tokenizer = new StringTokenizer(request);
				while (tokenizer.hasMoreElements()) {
					if (tokenizer.nextToken().equals(o.toString().toLowerCase())) {
						a = doSwitch(o, tokenizer, request);
						lastOperation = o;
						actions.add(a);
					}
				}
			}
				if(actions.isEmpty()){
					if(outputInterface.customOperationsManager(bullet)==null)a = new Say(outputInterface, linguistics.parseSentence(request).toString(), parameters);
					else a = new Say(outputInterface, outputInterface.customOperationsManager(bullet), parameters);
					actions.add(a);
					
			}
//		}
		return actions;
	}

	private Action doSwitch(Operation o, StringTokenizer tokenizer,
			String originalSentence) {
		switch (o) {
		case POST:
			return doPost(tokenizer);
		case GRANT:
			return doGrant(originalSentence);
		case REVOKE:
			return doRevoke(originalSentence);
		case SAY:
			return new Say(outputInterface, regex.getAfter(originalSentence,
					"say "), parameters);
		case SAVE:
			return doSave(originalSentence);
		default:
			return new Say(
					outputInterface,
					"I've intercepted a command but I didn't understand it. This may mean that I am smarter than you think or you just forgot to handle a case in that switch... You know SET, ENABLE or DISABLE Commands? Why do you even write stuff if you are too lazy to complete it?? Geez.", parameters);
		}
	}

	private Action doSave(String originalSentence) {
		InputInterface inputInterface = bullet.getRequest().getInputChannel();
		if (!(inputInterface instanceof MultimediaInputInterface))
			return new Say(outputInterface, "I cannot do that, since "
					+ inputInterface.getInterfaceName()
					+ " is not an instance of MultimediaInputInterface!", parameters);
		String fileName = new Date().toString();
		if (originalSentence.contains(" as "))
			fileName = regex.getAfter(originalSentence, " as ");
		if(!fileName.contains("."))fileName += ".jpg";
		URL url = ((MultimediaInputInterface) inputInterface).save(bullet.getRequest().getParameters());
		if(url==null) return new Say(outputInterface, "I don't see any images here!", parameters);
		File destinationFile = new File(Aura.Self._FILES.getValue() + fileName);
		try {
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(destinationFile);
			byte[] b = new byte[2048];
			int length;
			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			is.close();
			os.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Say(outputInterface, "Succesfully saved image as "
				+ fileName + ".", parameters);
	}

	private Action doRevoke(String sentence) {
		if (!sentence.contains(" to "))return new Say(outputInterface,"To who shall I revoke permissions?", parameters);
		String username = regex.getAfter(sentence, " to ");
		if (!Aura.isAuthorizedUser(username))return new Say(outputInterface, username + "...? I don't know that person!", parameters);
		Aura.removeAuthorizedUser(username);
		return new Say(outputInterface, "Ok! I'm going to forget everything about " + username + "!", parameters);
	}

	private Action doGrant(String sentence) {
		if (!sentence.contains(" to "))
			return new Say(outputInterface, "To who shall I grant permissions?", parameters);
		String username = regex.getAfter(sentence, " to ");
		if (Aura.isAuthorizedUser(username))
			return new Say(outputInterface, username
					+ " is already authorized!", parameters);
		Aura.addAuthorizedUser(username);
		return new Say(
				outputInterface,
				"From now on, the user "
						+ username
						+ " is able to give me commands. This permission is not permanent and will be automatically revoked as soon as I shut down or if an admin asks for it.", parameters);
	}

	private Action doPost(StringTokenizer tokenizer) {
		if (!tokenizer.hasMoreTokens())
			return new Say(outputInterface, "Which file should I post?", parameters);
		String imageName = tokenizer.nextToken();
		if (imageName.endsWith(Aura.ImageType.JPG.toString()))
			return new Say(outputInterface,
					"I only support JPG images at the moment! And that doesn't look like one!", parameters);
		File file = new File(Aura.Self._FILES.getValue() + imageName);
		if (!file.exists())
			return new Say(outputInterface, "Cannot find any file named "
					+ imageName, parameters);
		if (!(outputInterface instanceof MultimediaOutputInterface))
			return new Say(outputInterface,
					"Post operation is not supported on this output interface("
							+ outputInterface.getInterfaceName() + ")!", parameters);
		Media m = new Media(file, bullet.getRequest().getParameters());
		return new Post((MultimediaOutputInterface) outputInterface, m);
	}

	public Bullet getBullet() {
		return this.bullet;
	}
}
