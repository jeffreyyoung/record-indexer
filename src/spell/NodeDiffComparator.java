package spell;

import java.util.Comparator;

import spell.MyTrie.WordNode;

class NodeDiffComparator implements Comparator<WordNode> {
    public int compare(WordNode n1, WordNode n2) {
        return n2.getDiff() - n1.getDiff();
    }
}
class NodeCountComparator implements Comparator<WordNode> {
    public int compare(WordNode n1, WordNode n2) {
        return n2.getCount() - n1.getCount();
    }
}
class NodeAlphabetComparator implements Comparator<WordNode> {
    public int compare(WordNode n1, WordNode n2) {
        return n2.getPath().compareTo(n1.getPath());
    }
}