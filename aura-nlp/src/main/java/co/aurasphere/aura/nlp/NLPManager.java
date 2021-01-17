package co.aurasphere.aura.nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.util.List;

import org.springframework.stereotype.Component;

import co.aurasphere.aura.model.business.Sentence;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphFactory;
import edu.stanford.nlp.semgraph.semgrex.SemgrexMatcher;
import edu.stanford.nlp.semgraph.semgrex.SemgrexPattern;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

@Component
public class NLPManager {

	private final static String PARSER_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";

	public void analyze(Sentence sentence) {

		// Impedisce che la copula cambi l'elemento root del discorso nel
		// predicato nominale invece che nel verbo essere.
		// Properties props = new Properties();
		// props.put("parse.flags", "-makeCopulaHead");
		// String[] options = {"-makeCopulaHead" };
		
		LexicalizedParser lp = LexicalizedParser.loadModel(PARSER_MODEL);

		// TreebankLanguagePack tlp = lp.treebankLanguagePack();
		// GrammaticalStructureFactory gsf = null;
		// if(tlp.supportsGrammaticalStructures()){
		// gsf = tlp.grammaticalStructureFactory();
		//
		// }
		//
		String sentenceToParse = sentence.toString();
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(
				new CoreLabelTokenFactory(), "");
		Tokenizer<CoreLabel> tok = tokenizerFactory
				.getTokenizer(new StringReader(sentenceToParse));
		List<CoreLabel> rawWords = tok.tokenize();
		Tree parse;

		parse = lp.apply(rawWords);
		// parse.pennPrint();

		// You can also use a TreePrint object to print trees and dependencies
		TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
		tp.printTree(parse);
	
		SemanticGraph dependencies = SemanticGraphFactory.allTypedDependencies(parse, true);

		sentence.setSentenceTree(parse);
		sentence.setDependencies(dependencies);
		detectSentenceSubject(sentence);
		detectCopula(sentence);
		detectMainVerb(sentence);
		detectQuestion(sentence);
		detectObjectComplement(sentence);
		if(sentence.getSubject()!=null)	
			sentence.setSubject((detectNounCompound(sentence, sentence.getSubject().toString())));	
		if(sentence.getComplement()!=null)
			sentence.setComplement(detectNounCompound(sentence, sentence.getComplement()));
		
		
		if(sentence.getStatistics().isCopulative())
			sentence.setComplement(sentence.getSubject());
		
		for (Tree subtree : parse) {
			if (subtree.label().value().equals("NN"))
				System.out.println("Pronome: " + subtree.yield());
		}
	} 

	private void detectCopula(Sentence sentence) {
		SemanticGraph graph = sentence.getDependencies();
		String pattern ="{}<cop{}";
		SemgrexMatcher m = SemgrexPattern.compile(pattern).matcher(graph);
		if(m.find()){
			System.out.println("Copula: " + m.getMatch().word());
//			Verb v = new Verb();
//			v.setInfinitive("toBe");
//			v.se
//			sentence.setVerb();
			sentence.getStatistics().setCopulative(true);
		}
	}
	
	private void detectMainVerb(Sentence sentence) {
		if(sentence.getStatistics().isCopulative())return;
		SemanticGraph graph = sentence.getDependencies();
		String pattern ="{$}";
		SemgrexMatcher m = SemgrexPattern.compile(pattern).matcher(graph);
		if(m.find()){
			System.out.println("Verb: " + m.getMatch().word());
			sentence.setVerb(m.getMatch().word());
		}
	}

	private void detectSentenceSubject(Sentence sentence) {
		SemanticGraph graph = sentence.getDependencies();
		String pattern = "{}<nsubj{}|>nsubjpass{}";
		SemgrexMatcher m = SemgrexPattern.compile(pattern).matcher(graph);
		if(m.find()){
		//	System.out.println("Subject: " + m.getMatch().word());
			sentence.setSubject(m.getMatch().word());
		}
	}
	
	private void detectQuestion(Sentence sentence){
		Tree tree = sentence.getSentenceTree();
		String pattern = "SBARQ";
		TregexMatcher m = TregexPattern.compile(pattern).matcher(tree);
		if(m.find()){
			System.out.println("modalQuestion");
			sentence.getStatistics().setModalQuestion(true);
		}
		else {
			pattern = "SQ";
			m = TregexPattern.compile(pattern).matcher(tree);
			if(m.find()){
				System.out.println("yesNoQuestion");
				sentence.getStatistics().setYesNoQuestion(true);
			}
		}
	}
	
	private void detectObjectComplement(Sentence sentence){
	SemanticGraph graph = sentence.getDependencies();
	String pattern = "{}<dobj{}";
	SemgrexMatcher m = SemgrexPattern.compile(pattern).matcher(graph);
	if(m.find()) sentence.setComplement(m.getMatch().word());
	}
	
	private String detectNounCompound(Sentence sentence, String word){
		SemanticGraph graph = sentence.getDependencies();
		if(word.contains("."))return word;
		String pattern = "{}>compound({}>/.+/{lemma:" + word + "})";
		SemgrexMatcher m = SemgrexPattern.compile(pattern).matcher(graph);
		if (m.getMatch()!=null) {
			System.out.println("Noun compound: " + word + m.getMatch().word());
			return word + m.getMatch().word();
		}
		return word;
	}
	

}
