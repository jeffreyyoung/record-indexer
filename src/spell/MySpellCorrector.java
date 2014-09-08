package spell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import client.communication.ClientCommunicator;
import spell.MyTrie.WordNode;

public class MySpellCorrector implements SpellCorrector {

	private MyTrie words;

	public MyTrie getWords() {
		return words;
	}

	public void useDictionary(String dictionaryFileName) throws IOException {

		words = new MyTrie();

		System.out.println(dictionaryFileName);
		URL url = new URL(dictionaryFileName);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

		String inputLine;

		while ((inputLine = in.readLine()) != null){
			System.out.println(inputLine);
			String[] csv = inputLine.split(",");

			for(String w : csv){
				//System.out.println(w);
				words.add(w);
			}
		}

		in.close();
	}

	public String suggestSimilarWord(String inputWord)
			throws NoSimilarWordFoundException {
		String word = inputWord.toLowerCase();
		String similarWord = getSimilarWord(word);

		if (similarWord != null)
			return similarWord;

		throw new NoSimilarWordFoundException();
	}

	public List<String> suggestSimilarWords(String inputWord){

		String word = sanitizeInput(inputWord);

		Set<String> similarWordSet = getSimilarWords(word);

		if (similarWordSet == null || inputWord.equals("")){
			System.out.println("there are no suggestions for the word:" + inputWord);
			return null;
		}
		else {

			List<String> suggestions = new ArrayList<String>(similarWordSet);
			System.out.println("For the word: "+ inputWord +"Suggestion is: " + suggestions.toString());
			return suggestions;
		}
	}

	private String sanitizeInput(String word_in){
		String word = word_in.toLowerCase();

		//word = word.replaceAll("[^a-zA-Z]", "");
		word = word.replaceAll("[^a-zA-Z0-9\\s]", "");
		return word;
	}

	public Set<String> getSimilarWords(String word) {

		if (words == null)
			return null;

		ArrayList<String> simWords = new ArrayList<String>();

		WordNode w = (WordNode) words.find(word);
		if (w != null)//check if word is already in bank, if not do everything
			return null;

		delToArray(word, simWords); //add all possible solutions 1 dif away
		transToArray(word, simWords);
		insToArray(word, simWords);
		altToArray(word, simWords);

		ArrayList<WordNode> nodes = removeNonWords(simWords); //remove all words not in dictionary

		ArrayList<String> simWords1 = new ArrayList<String>(); //if there is no word of 1 diff away, get all possible solutions for all possible words
		for (int i = 0; i < simWords.size(); i++) { //iterate through simWords
			String curWord = simWords.get(i);
			delToArray(curWord, simWords1);
			transToArray(curWord, simWords1);
			insToArray(curWord, simWords1);
			altToArray(curWord, simWords1);
		}

		ArrayList<WordNode> nodes1 = removeNonWords(simWords1);

		Set<String> similarWords = new TreeSet<String>();

		for (WordNode n : nodes){
			similarWords.add(n.getPath());
		}

		for (WordNode n : nodes1){
			similarWords.add(n.getPath());
		}

		return similarWords;
	}

	public String getSimilarWord(String word) {

		ArrayList<String> simWords = new ArrayList<String>();

		WordNode w = (WordNode) words.find(word);
		if (w != null)//check if word is already in bank, if not do everything
			return w.getPath();

		delToArray(word, simWords); //add all possible solutions 1 dif away
		transToArray(word, simWords);
		insToArray(word, simWords);
		altToArray(word, simWords);

		ArrayList<WordNode> nodes = removeNonWords(simWords); //remove all words not in dictionary

		String simWord = null;

		if (nodes.size() > 0) {

			simWord = retrieveWord(nodes);

		} else {
			ArrayList<String> simWords1 = new ArrayList<String>(); //if there is no word of 1 diff away, get all possible solutions for all possible words
			for (int i = 0; i < simWords.size(); i++) { //iterate through simWords
				String curWord = simWords.get(i);
				delToArray(curWord, simWords1);
				transToArray(curWord, simWords1);
				insToArray(curWord, simWords1);
				altToArray(curWord, simWords1);
			}

			ArrayList<WordNode> nodes1 = removeNonWords(simWords1);

			if (nodes1.size() > 0) {
				simWord = retrieveWord(nodes1);
			}
		}

		return simWord;
	}

	private String retrieveWord(ArrayList<WordNode> nodes) {
		Collections.sort(nodes, new NodeDiffComparator());
		int minDif = nodes.get(nodes.size() - 1).getDiff();
		for (int i = nodes.size() - 1; i >= 0; i--) {
			if (nodes.get(i).getDiff() > minDif)
				nodes.remove(i);
		}

		Collections.sort(nodes, new NodeCountComparator());
		int minCount = nodes.get(0).getCount();
		for (int i = nodes.size() - 1; i >= 0; i--) {
			if (nodes.get(i).getCount() < minCount)
				nodes.remove(i);
		}

		Collections.sort(nodes, new NodeAlphabetComparator());
		return nodes.get(nodes.size()-1).getPath();
	}

	public ArrayList<WordNode> removeNonWords(ArrayList<String> simWords) {

		ArrayList<WordNode> nodes = new ArrayList<WordNode>();

		for (int i = simWords.size() - 1; i >= 0; i--) {

			String w = simWords.get(i);

			WordNode n = (WordNode) words.find(w);

			if (n != null) {
				nodes.add(n);
			}
		}

		return nodes;
	}

	public void delToArray(String word, ArrayList<String> simWords) {
		for (int i = 0; i < word.length(); i++) {
			StringBuilder b = new StringBuilder();
			b.append(word.substring(0, i));
			b.append(word.substring(i + 1));
			simWords.add(b.toString());
			// String w = word.substring(0, i) + (word.substring(i+1));
			// simWords.add(w);
		}
	}

	public void insToArray(String word, ArrayList<String> simWords) {
		for (int i = 0; i < word.length() + 1; i++) {
			for (int a = 97; a <= 123; a++) {
				char c = (char) a;
				if (a == 123) c = ' ';

				StringBuilder b = new StringBuilder();
				b.append(word.substring(0, i));
				b.append(c);
				b.append(word.substring(i));
				simWords.add(b.toString());
				// String w = word.substring(0,i) + c + word.substring(i);
				// simWords.add(w);
			}
		}
	}

	public void altToArray(String word, ArrayList<String> simWords) {
		for (int i = 0; i < word.length(); i++) {
			for (int a = 97; a <= 123; a++) {
				char c = (char) a;
				if (a == 123) c = ' ';

				StringBuilder b = new StringBuilder();
				b.append(word.substring(0, i));
				b.append(c);
				b.append(word.substring(i + 1));
				simWords.add(b.toString());
				// String w = word.substring(0,i) + c + word.substring(i+1);
				// simWords.add(w);
			}
		}
	}

	public static void transToArray(String word, ArrayList<String> simWords) {
		for (int i = 0; i < word.length() - 1; i++) {
			char a = word.charAt(i);
			char b = word.charAt(i + 1);

			char[] ary = word.toCharArray();

			ary[i] = b;
			ary[i + 1] = a;

			StringBuilder builder = new StringBuilder();

			builder.append(ary);

			simWords.add(builder.toString());
		}

	}

}
