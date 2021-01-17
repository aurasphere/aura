package co.aurasphere.aura.nlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.data.list.PointerTargetTree;
import net.didion.jwnl.data.relationship.Relationship;
import net.didion.jwnl.data.relationship.RelationshipFinder;
import net.didion.jwnl.data.relationship.RelationshipList;
import net.didion.jwnl.dictionary.Dictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DictionaryManager {
	//
	// @Autowired
	// private DictionaryDaoInterface dictionaryDao;
	//
	// public boolean isWordIntoDictionary(String word){
	// if(dictionaryDao.lookupWord(word)==null)return false;
	// return true;
	// }

	public static Dictionary wordnet;

	public DictionaryManager() {
		// Initialize the database
		try {
			JWNL.initialize(getClass().getResourceAsStream("/file_properties.xml"));
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		// Create dictionary object
		wordnet = Dictionary.getInstance();
	}

	/**
	 * Method which print all the Part Of Speech (POS) tags for a word.
	 * 
	 * @param word
	 *            to print the POS.
	 * 
	 */

	public static void findPartsOfSpeech(String word) throws JWNLException {
		System.out.println("\nFinding parts of speech for " + word + ".");
		// Get an array of parts of speech
		POS[] pos = getPOS(word);
		// If we found at least one we found the word
		if (pos.length > 0) {
			// Loop through and display them all
			for (int i = 0; i < pos.length; i++) {
				System.out.println("POS: " + pos[i].getLabel());
			}
		} else {
			System.out.println("I could not find " + word + " in WordNet!");
		}
	}

	/**
	 * Method which prints all the definition for a word.
	 * 
	 * @param word
	 *            to define.
	 * @throws JWNLException
	 */

	public static void listGlosses(IndexWord word) throws JWNLException {
		System.out.println("\n\nDefinitions for " + word.getLemma() + ":");
		// Get an array of Synsets for a word
		Synset[] senses = word.getSenses();
		// Display all definitions
		for (int i = 0; i < senses.length; i++) {
			System.out.println(word + ": " + senses[i].getGloss());
		}
	}

	/**
	 * <p>
	 * Method which lists related words of type of relation for a word.
	 * 
	 * <p>
	 * The possible types of relactions are:
	 * 
	 * <p>
	 * <code>PointerType.SIMILAR_TO</code>: returns synonyms (only for
	 * adjectives). -
	 * <p>
	 * <code>PointerType.HYPERNIM, PointerType.HYPONYM</code>: X is Hypernym of
	 * Y if every Y is of type X. Hyponym is the inverse.
	 * <p>
	 * <code>PointerType.ANTONYM</code>: returns antonyms.
	 * <p>
	 * <code>PointerType.ENTAILMENT</code>: X is entailment of Y if to do Y you
	 * need to do X (verbs only).
	 * 
	 * @param w
	 *            the word whose relaction to find.
	 * @param type
	 *            of the relaction.
	 * @throws JWNLException
	 */

	public static void findRelatedWords(IndexWord w, PointerType type)
			throws JWNLException {
		System.out.println("\n\nI am now going to find related words for "
				+ w.getLemma() + ", listed in groups by sense.");
		System.out.println("We'll look for relationships of type "
				+ type.getLabel() + ".");

		// Call a function that returns an ArrayList of related senses
		ArrayList a = getRelated(w, type);
		if (a.isEmpty()) {
			System.out.println("Hmmm, I didn't find any related words.");
		} else {
			// Display the words for all the senses
			for (int i = 0; i < a.size(); i++) {
				Synset s = (Synset) a.get(i);
				Word[] words = s.getWords();
				for (int j = 0; j < words.length; j++) {
					System.out.print(words[j].getLemma());
					if (j != words.length - 1)
						System.out.print(", ");
				}
				System.out.println();
			}
		}
	}

	/**
	 * Method which finds a relation of a given type between 2 words. It starts
	 * from the first word and travel into the three towards the second, telling
	 * the depth of the relation by the number of node passed.
	 * 
	 * @param start
	 *            the first word.
	 * @param end
	 *            the second word.
	 * @param type
	 *            the type of relation to find.
	 * @throws JWNLException
	 */

	public static void findRelationships(IndexWord start, IndexWord end,
			PointerType type) throws JWNLException {

		System.out.println("\n\nTrying to find a relationship between \""
				+ start.getLemma() + "\" and \"" + end.getLemma() + "\".");
		System.out.println("Looking for relationship of type "
				+ type.getLabel() + ".");

		// Ask for a Relationship object
		Relationship rel = getRelationship(start, end, type);
		// If it's not null we found the relationship
		if (rel != null) {
			// Display depth
			System.out.println("The depth of this relationship is: "
					+ rel.getDepth());
			System.out.println("Here is how the words are related: ");
			// Get a list of the words that make up the relationship
			ArrayList a = getRelationshipSenses(rel);
			System.out.println("Start: " + start.getLemma());
			// Display all senses
			for (int i = 0; i < a.size(); i++) {
				Synset s = (Synset) a.get(i);
				Word[] words = s.getWords();
				System.out.print(i + ": ");
				for (int j = 0; j < words.length; j++) {
					System.out.print(words[j].getLemma());
					if (j != words.length - 1)
						System.out.print(", ");
				}
				System.out.println();
			}

		} else
			System.out
					.println("I could not find a relationship between these words!");

	}
	public int measureVerbsDistance(String firstWord, String secondWord){
		int depth = 99;
		try {
			IndexWord start = getWord(POS.VERB, firstWord);
			IndexWord end = getWord(POS.VERB, secondWord);
			if((start==null)||(end==null))return 99;
			// Ask for a Relationship object
			Relationship rel = getRelationship(start, end, PointerType.HYPERNYM);
			// If it's not null we found the relationship
			if (rel != null) {
				// Display depth
				System.out.println("The depth of this relationship is: "
						+ rel.getDepth());
				depth = rel.getDepth();
				System.out.println("Here is how the words are related: ");
				// Get a list of the words that make up the relationship
				ArrayList a = getRelationshipSenses(rel);
				System.out.println("Start: " + start.getLemma());
				// Display all senses
				for (int i = 0; i < a.size(); i++) {
					Synset s = (Synset) a.get(i);
					Word[] words = s.getWords();
					System.out.print(i + ": ");
					for (int j = 0; j < words.length; j++) {
						System.out.print(words[j].getLemma());
						if (j != words.length - 1)
							System.out.print(", ");
					}
					System.out.println();
				}
				
			} else
				System.out
				.println("I could not find a relationship between these words!");
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return depth;
	}

	/**
	 * Method which returns all the POS(Part Of Speech) for a word.
	 * 
	 * @param s
	 *            the word to process.
	 * @return an array of POS.
	 * @throws JWNLException
	 */
	public static POS[] getPOS(String s) throws JWNLException {
		// Look up all IndexWords (an IndexWord can only be one POS)
		IndexWordSet set = wordnet.lookupAllIndexWords(s);
		// Turn it into an array of IndexWords
		IndexWord[] words = set.getIndexWordArray();
		// Make the array of POS
		POS[] pos = new POS[words.length];
		for (int i = 0; i < words.length; i++) {
			pos[i] = words[i].getPOS();
		}
		return pos;
	}

	/**
	 * Method which returns the related words from the first synset that
	 * contains them.
	 * 
	 * @param word
	 *            whose related word to find.
	 * @param type
	 *            of relation.
	 * @return an ArrayList of related words.
	 * @throws JWNLException
	 */

	public static ArrayList getRelated(IndexWord word, PointerType type)
			throws JWNLException {
		try {
			Synset[] senses = word.getSenses();
			// Look for the related words for all Senses
			for (int i = 0; i < senses.length; i++) {
				ArrayList a = getRelated(senses[i], type);
				// If we find some, return them
				if (a != null && !a.isEmpty()) {
					return a;
				}
			}
		} catch (NullPointerException e) {
			// System.out.println("Oops, NULL problem: " + e);
		}
		return null;
	}

	/**
	 * Method which returns the related words of a given sense from the first
	 * synset that contains them. Does synonyms by default.
	 * 
	 * @param sense
	 *            the sense of the words to find.
	 * @param type
	 *            of relation.
	 * @return an ArrayList of related words.
	 * @throws JWNLException
	 * @throws NullPointerException
	 */

	public static ArrayList getRelated(Synset sense, PointerType type)
			throws JWNLException, NullPointerException {
		PointerTargetNodeList relatedList;
		// Call a different function based on what type of relationship you are
		// looking for
		if (type == PointerType.HYPERNYM) {
			relatedList = PointerUtils.getInstance().getDirectHypernyms(sense);
		} else if (type == PointerType.HYPONYM) {
			relatedList = PointerUtils.getInstance().getDirectHyponyms(sense);
		} else {
			relatedList = PointerUtils.getInstance().getSynonyms(sense);
		}
		// Iterate through the related list and make an ArrayList of Synsets to
		// send back
		Iterator i = relatedList.iterator();
		ArrayList a = new ArrayList();
		while (i.hasNext()) {
			PointerTargetNode related = (PointerTargetNode) i.next();
			Synset s = related.getSynset();
			a.add(s);
		}
		return a;
	}

	/**
	 * Method which prints the tree of related words for a given sense.
	 * 
	 * @param sense
	 *            the synset of the world. You can get it from an IndexWord with
	 *            the getSense() method.
	 * @param depth
	 *            how many node to print.
	 * @param type
	 *            the type of relation(synonyms, antonyms...) to print.
	 * @throws JWNLException
	 */

	public static void showRelatedTree(Synset sense, int depth, PointerType type)
			throws JWNLException {
		PointerTargetTree relatedTree;
		// Call a different function based on what type of relationship you are
		// looking for
		if (type == PointerType.HYPERNYM) {
			relatedTree = PointerUtils.getInstance().getHypernymTree(sense,
					depth);
		} else if (type == PointerType.HYPONYM) {
			relatedTree = PointerUtils.getInstance().getHyponymTree(sense,
					depth);
		} else {
			relatedTree = PointerUtils.getInstance().getSynonymTree(sense,
					depth);
		}
		// If we really need this info, we wil have to write some code to
		// Process the tree
		// Not just display it
		relatedTree.print();
	}

	/**
	 * Method which looks for the relationship between two words.
	 * @param start the first word.
	 * @param end the second word.
	 * @param type the type of relationship to find.
	 * @return a relationship between the two words or null if it doesn't exist.
	 * @throws JWNLException
	 */
	
	public static Relationship getRelationship(IndexWord start, IndexWord end,
			PointerType type) throws JWNLException {
		// All the start senses
		Synset[] startSenses = start.getSenses();
		// All the end senses
		Synset[] endSenses = end.getSenses();
		// Check all against each other to find a relationship
		for (int i = 0; i < startSenses.length; i++) {
			for (int j = 0; j < endSenses.length; j++) {
				RelationshipList list = RelationshipFinder.getInstance()
						.findRelationships(startSenses[i], endSenses[j], type);
				if (!list.isEmpty()) {
					return (Relationship) list.get(0);
				}
			}
		}
		return null;
	}
	
	/**
	 * Method which creates an ArrayList of Synsets from a given relationship.
	 * @param rel the relationship whose synsets to create.
	 * @return an ArrayList of Synsets.
	 * @throws JWNLException
	 */
	
	public static ArrayList getRelationshipSenses(Relationship rel)
			throws JWNLException {
		ArrayList a = new ArrayList();
		PointerTargetNodeList nodelist = rel.getNodeList();
		Iterator i = nodelist.iterator();
		while (i.hasNext()) {
			PointerTargetNode related = (PointerTargetNode) i.next();
			a.add(related.getSynset());
		}
		return a;
	}

	/**
	 * Method which lemmatize a word, returning back the base form.
	 * 
	 * @param pos
	 *            the part of speech tag.
	 * @param s
	 *            the word to lemmatize.
	 * @return a lemmatized IndexWord object.
	 * @throws JWNLException
	 */

	public static IndexWord lemmatizeWord(POS pos, String s)
			throws JWNLException {
		IndexWord word = wordnet.getMorphologicalProcessor().lookupBaseForm(
				pos, s);
		return word;
	}

	/**
	 * Method which turns a word and a POS into an IndexWord object.
	 * 
	 * @param pos
	 *            the part of speech tag.
	 * @param s
	 *            the word to convert.
	 * @return an IndexWord object.
	 * @throws JWNLException
	 */

	public static IndexWord getWord(POS pos, String s) throws JWNLException {
		IndexWord word = wordnet.getIndexWord(pos, s);
		return word;
	}

}