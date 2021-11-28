package lse;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	HashSet<String> noiseWords;
	

	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		BufferedReader txt = new BufferedReader(new FileReader(docFile));
		HashMap<String,Occurrence> keywordList = new HashMap<String,Occurrence>();
		try {
			String line = "";
			int count = 0;
			while((line = txt.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line, " ");
				while(tokenizer.hasMoreTokens()) {
					String x = tokenizer.nextToken();
					String word = getKeyword(x);
					if(word != null) {
						if(keywordList.containsKey(word)) {
							Occurrence old = keywordList.get(word);
							old.frequency++;
							keywordList.replace(word,old); 
						} else {						
							keywordList.put(word,new Occurrence(docFile,1));
						}
					}									
				}
				count++;
			}
		System.out.println("\nHashMap:\n"+keywordList);
			
		}
		catch (IOException e) {
			throw new NoSuchElementException();
		}	
		return keywordList;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		HashMap<String, Occurrence> kw = kws;
		for(Entry<String, Occurrence> word : kw.entrySet()) {
			String keyword = word.getKey();
			Occurrence occ = word.getValue();
			
			ArrayList<Occurrence> OccList = keywordsIndex.get(keyword);
			
			if(OccList == null) {
				OccList = new ArrayList<Occurrence>();
				OccList.add(occ);
				keywordsIndex.put(keyword, OccList);
			} else {
				OccList.add(occ);
				insertLastOccurrence(OccList);
			}
		}
		
		
	}
	
	private boolean checkPunc(char x){
		if(x == '.' || x == ',' || x == '?' || x == ':' || x == ';' || x == '!') {
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		word = word.toLowerCase();
		int len = word.length();
		
		if(word.length() == 1) {
			char x = word.charAt(0);
			if((checkPunc(x))) {
				return null;
			}
		}
		String punc = "";
		for(int x = word.length()-1; x >= 0; x--) {
			if(checkPunc(word.charAt(x))) {
				punc+=word.charAt(x);
				word = word.substring(0,word.length()-1);
			} 
			else {
				break;
			}
		}
	
		if(noiseWords.contains(word)) {
			return null;
		}
		
		
		for(int x = 0; x < word.length(); x++) {
			if(!(Character.isLetter(word.charAt(x)))) {
				return null;
			}
		}

		return word;
	    	
	}
		
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		ArrayList<Integer> indexes = new ArrayList<>();
        int freq = occs.get(occs.size()-1).frequency;
        int left = 0;
        int right = occs.size() - 2;
        int mid;
        int insertIndex;
        while (true) {                                              
            mid = (right + left) / 2;
            indexes.add(mid);
            Occurrence occ = occs.get(mid);
            if (occ.frequency == freq) {       
                insertIndex = mid;
                break;
            }
            else if (occ.frequency < freq) {   
                right = mid - 1;               
                if (left > right) {
                    insertIndex = mid;         
                    break;
                }
            }
            else {                                  
                left = mid + 1;                
                if (left > right) {
                    insertIndex = mid + 1;     
                    break;
                }
            }
        }
		
        if (insertIndex != occs.size() - 1) {
            Occurrence temp = occs.get(occs.size()-1);              
            occs.remove(occs.size()-1);                             
            occs.add(insertIndex, temp);                          
        }
        return indexes;

	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		ArrayList<Occurrence> occ1 = new ArrayList<Occurrence>();
		ArrayList<Occurrence> occ2 = new ArrayList<Occurrence>();
		ArrayList<Occurrence> occFinal = new ArrayList<Occurrence>();
		ArrayList<String> strList = new ArrayList<String>();
		if (keywordsIndex.containsKey(kw1)) {
			occ1 = keywordsIndex.get(kw1);
		}
		if (keywordsIndex.containsKey(kw2)) {
			occ2 = keywordsIndex.get(kw2);
		}
		occFinal.addAll(occ1);
		occFinal.addAll(occ2);
		if ( !(occ1.isEmpty()) && !(occ2.isEmpty()) ) {
			for (int i = 0; i < occFinal.size() - 1; i++) {
					for (int j = i + 1; j < occFinal.size(); j++) {
							if (occFinal.get(i).frequency < occFinal.get(j).frequency) {
								Occurrence occSwap = occFinal.get(i);
								occFinal.set(i, occFinal.get(j));
								occFinal.set(j, occSwap);
							}
						}
			}
		for (int i = 0; i < occFinal.size() - 1; i++) {
			for (int j = i + 1; j < occFinal.size(); j++) {
					if (occFinal.get(i).document == occFinal.get(j).document) 
							occFinal.remove(j);
						}
				}
			}
		while (occFinal.size() > 5)
			occFinal.remove(occFinal.size() - 1);
		for (Occurrence occur:occFinal)
			strList.add(occur.document);
		return strList;

		}


	}