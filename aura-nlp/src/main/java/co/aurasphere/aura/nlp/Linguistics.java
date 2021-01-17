package co.aurasphere.aura.nlp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.aurasphere.aura.core.Aura;
import co.aurasphere.aura.core.Aura.Operation;
import co.aurasphere.aura.core.skyscraper.impl.live.WikipediaScraper;
import co.aurasphere.aura.model.business.Sentence;

@Service
public class Linguistics {
	
	private Sentence inputSentence;
	
	private Sentence outputSentence;
	
	@Autowired
	private WikipediaScraper wikipediaScraper;

	@Autowired
	private NLPManager nlpManager;
	
//	@Autowired
//	private DictionaryManager dictionaryManager;

	public Sentence parseSentence(String sentence){
		this.inputSentence = new Sentence(sentence);
		this.outputSentence = inputSentence;
	//	subjectManager.detectSentenceSubject(inputSentence);
		nlpManager.analyze(inputSentence);
	//	if(extrapolateClosestCommand(inputSentence)!=null)inputSentence.setCommand(extrapolateClosestCommand(inputSentence));			
		if(inputSentence.getStatistics().isModalQuestion())
			inputSentence.setSentence(wikipediaScraper.scrape(inputSentence.getSubject().toString()));
		else
		//	answer("@description");
			outputSentence.setSentence("Sorry, I did not understand.");
		return inputSentence;
	}

	/*
	private Operation extrapolateClosestCommand(Sentence sentence){
		int tempDist = 0, bestDist = 3;
		if(sentence.getVerb()==null) return null;
		String verb;
		String operation;
		Operation command = null;
		for(Operation o: Aura.Operation.values()){
			verb = sentence.getVerb().toString();
			operation = o.toString();
			tempDist = dictionaryManager.measureVerbsDistance(sentence.getVerb().toString(), o.toString());
			if(tempDist<=bestDist) {
				bestDist = tempDist;
				command = o;
			}
		}
		return command;
	}
	*/

}
