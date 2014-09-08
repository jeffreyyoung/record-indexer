package spell;

import java.util.ArrayList;
import java.util.Collections;

public class MyTrie implements Trie {

	private WordNode root;
	private int wordCount;
	private int nodeCount;


	public MyTrie(){
		this.root = new WordNode( (char)0 );
		this.wordCount = 0;
		this.nodeCount = 1;
	}
	
	public void add(String word) {
		word = word.toLowerCase();
		System.out.println(word);
		
		WordNode it = root;
		String curPath = "";
		for ( int i = 0; i < word.length(); i++) {
			char curChar = word.charAt(i);
			curPath += curChar;
			int pos = curChar - 'a';
			if (pos < 0){
				pos = 26;
			}
			WordNode[] children = it.getChildren();
			if (children[pos] != null) {
				it = it.children[pos];
			}
			else {
				WordNode temp = new WordNode(curChar);
				children[pos] = temp;
				temp.setPath(curPath);
				it = temp;
				this.nodeCount++;
			}

			if (i == word.length() - 1){
				it.incrementCount();
			}
		}		
	}
	public String toString() { 
		StringBuilder builder = new StringBuilder();
		toStringRecurse(root, builder);
		return builder.toString();	
	}
	
	private void toStringRecurse(WordNode n, StringBuilder b) {
		if (n.getCount() > 0){
			b.append(n.getPath() + " " + n.getCount() + "\n");
		}
		
		for(int i = 0; i < 26; i++){
			if (n.children[i] != null)
				toStringRecurse(n.children[i], b);
		}
	}
	
	public String getSimilarWord(String w){
		ArrayList <WordNode> words = new ArrayList<WordNode>();
		
		String word = null;
		
		if (words.size() > 0){
			Collections.sort(words, new NodeDiffComparator());
			int minDif = words.get(words.size()-1).getDiff();
			for (int i = words.size() - 1; i >= 0; i--){
				if (words.get(i).getDiff() > minDif)
					words.remove(i);
			}
			
			Collections.sort(words, new NodeCountComparator());
			int minCount = words.get(0).getCount();
			for (int i = words.size() - 1; i >= 0; i--){
				if (words.get(i).getCount() < minCount)
					words.remove(i);
			}
			
			Collections.sort(words, new NodeAlphabetComparator());
			word = words.get(0).getPath();
		}
		return word;
	}
	
	public Trie.Node find(String word) { 
		WordNode it = root;
		for ( int i = 0; i < word.length(); i++) {
			char curChar = word.charAt(i);
			int pos = curChar - 'a';
			WordNode[] children = it.getChildren();
			if (pos < 0){
				pos = 26;
			}
			if (children[pos] != null) {
				it = it.children[pos];
			}
			else {
				return null;
			}
		}
		
		if (it.getCount() > 0)
			return it;
		else
			return null;
	}

	public int getWordCount() { 
		return wordCount;
	}

	public int getNodeCount() { 
		return nodeCount;
	}
	
	@Override
	public int hashCode() { 
		return 7 * getNodeCount() + 13 * getWordCount() + this.toString().length()*3;
	}

	@Override
	public boolean equals(Object obj) {
        
		if (obj == null)
			return false;
		
		if (this.getClass() != obj.getClass())
			return false;
		
	    MyTrie other = (MyTrie) obj;
	    
	    if (!this.toString().equals(obj.toString()))
	    	return false;
	    
	    if (wordCount != other.getWordCount())
	    	return false;
	    
	    if (nodeCount != other.getNodeCount())
	    	return false;
	    
	    return true;   
	}
	

	public class WordNode implements Trie.Node {

		private int count;
		private WordNode[] children = new WordNode[27];
		private String path;
		private int diff;

		public WordNode(char character){
			this.count = 0;
			this.children = new WordNode[27];
			this.path = "";
		}
		
		public int getDiff(){
			return diff;
		}
		
		public void setDiff(int diff){
			this.diff = diff;
		}


		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public int getCount() { 
			return count;
		}

		public int getValue(){
			return count;
		}
		public void incrementCount(){
			this.count++;
		}

		public WordNode[] getChildren() {
			return children;
		}
	}
}




