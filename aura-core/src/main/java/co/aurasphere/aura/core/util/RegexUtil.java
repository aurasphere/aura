package co.aurasphere.aura.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class RegexUtil {
	
	/**
	 * Method which creates a RegExp that matches a single word delimited by whitespaces or start/end of string.
	 * @param word to match.
	 * @return The RegExp that matches that word.
	 */
	
	public String singleWord(String word){
		return "(\\s|^)" + word + "(\\s|$)";
	}
	
	/**
	 * Method which creates a RegExp that matches a single word delimited by whitespaces, punctuation or start/end of string.
	 * @param word: The word to match.
	 * @return The RegExp that matches that word.
	 */
	
	public String singleWordWithPunctuaction(String word){
		return "(\\W|^)" + word + "(\\W|$)";
	}
	
	/**
	 * Method which generates a regex starting from a word and tryes to match it against a string.
	 * @param word to match.
	 * @param string to match against.
	 * @param punctuationAllowed a boolean representing if the word may be delimitated by punctuation.
	 * @return true if the word is contained within the string, false otherwise.
	 */
	
	public boolean matchWord(String word, String string, boolean punctuationAllowed){
		if(punctuationAllowed)word = singleWordWithPunctuaction(word);
		else word = singleWord(word);
		return Pattern.compile(word).matcher(string).find();
	}
	
	/**
	 * Method which retrieves a tag inner option.
	 * @param tag a tag with an inner option.
	 * @return the inner option to retrieve.
	 */
	
	public String getTagOption(String tag){
		String outerTag = tag.substring(1 ,tag.indexOf("("));
		return tag.replaceAll( outerTag + "[\\(]|[\\)]", "");
	}
	
	public String removeLeadingTrailingPunctuation(String sentence){
		if(sentence.endsWith("."))return sentence.substring(0,sentence.length()-1);
		return sentence;
		//return sentence.replaceAll("^\\W+|\\W+$", "");
	}
	
	public String[] splitCoordinateSentences(String sentence){
		return sentence.split(" and |,");
	}
	
	public String getQuotes(String sentence){
		Matcher m = Pattern.compile("([\"'])(?:(?=(\\\\?))\\2.)*?\\1").matcher(sentence);
		if(m.find())return m.group().substring(1, m.group().length()-1);
		return "";
	}
	
	public String getAfter(String sentence, String string){
		return sentence.substring(sentence.indexOf(string) + string.length());
	}

}