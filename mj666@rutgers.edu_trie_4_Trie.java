package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		TrieNode trie = new TrieNode(null, null, null);
		if(allWords == null) {
			return null;
		}
		else {
			Indexes addIndexes;
			short startindex = 0;
			short endindex = (short) (allWords[0].length()-1);
			addIndexes = new Indexes(0, startindex, endindex);
			trie.firstChild = new TrieNode(addIndexes, null, null);
			
			for(int x = 1; x < allWords.length; x++) {
				TrieNode prevpointer = null;
				TrieNode currpointer = trie.firstChild;
				String temp = allWords[x];
				
				int start = -1;
				int end = -1;
				int pointer = -1; 
				int word = -1;;
				
				
		while(currpointer != null) {
			start = currpointer.substr.startIndex;
			end = currpointer.substr.endIndex;
          word = currpointer.substr.wordIndex;
          
          
		if(start > temp.length()){
			prevpointer = currpointer;
            currpointer = currpointer.sibling;
          }
		String w1 = allWords[word].substring(start, end + 1);
		  String w2 = temp.substring(start);
		  pointer = commonPref(w1, w2);
          if(pointer != -1){
        	  pointer += start;
          }
          if(pointer == -1){
        	  prevpointer = currpointer;
        	  currpointer = currpointer.sibling;
          }
          else{ 
            if(pointer < end){
            	prevpointer = currpointer;
            	break;
            }
            else if(pointer == end){
            	prevpointer = currpointer;
            	currpointer = currpointer.firstChild;
            }
          }
				}
		if(currpointer == null) {
           addIndexes = new Indexes(x, (short)start, (short)(temp.length()-1));                
            prevpointer.sibling = new TrieNode(addIndexes, null, null);
          }
        else {
              Indexes prevstring = prevpointer.substr; 
              TrieNode prevchild = prevpointer.firstChild;
              addIndexes = new Indexes(prevstring.wordIndex, (short) (pointer+1), prevstring.endIndex);
              prevstring.endIndex = (short) pointer;
              prevpointer.firstChild = new TrieNode(addIndexes, null, null);
              prevpointer.firstChild.firstChild = prevchild;
              addIndexes = new Indexes(x, (short) (pointer+1), (short) (temp.length()-1));
              prevpointer.firstChild.sibling = new TrieNode(addIndexes, null, null);
          }
		}
	}
	return trie;
}

	
	private static int commonPref(String word1, String word2) {
		int commonpref = 0;
		int check = Math.min(word1.length(), word2.length());
	  	while((commonpref < check) && (word1.charAt(commonpref) == word2.charAt(commonpref))){
	  		commonpref++;
	    }
	  	return commonpref-1; 
	}
		
		
		
		
		
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
			/** COMPLETE THIS METHOD **/
				if(root == null) 
						return null;
				ArrayList<TrieNode> list = new ArrayList<TrieNode>();
				TrieNode pointer = root;

				int count = 0;
				while(pointer != null) {
					if(pointer.substr == null) 
						pointer = pointer.firstChild;

						String word = allWords[pointer.substr.wordIndex];
						String str = word.substring(0,pointer.substr.endIndex+1);

				if(word.startsWith(prefix) || prefix.startsWith(str)) {
					if(pointer.firstChild != null) {
						list.addAll(completionList(pointer.firstChild, allWords, prefix));
						pointer = pointer.sibling;
						count++;
					} 
					else { 
						list.add(pointer);
						pointer = pointer.sibling;
						count++;
					}
				} 
				else {
					pointer = pointer.sibling;
					}

				}
				if(count > 0) {			
					return list;
				}
				else 
					return null;
	}
	
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
