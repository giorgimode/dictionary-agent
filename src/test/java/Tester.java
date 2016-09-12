import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

import static edu.mit.jwi.item.POS.NOUN;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.runtime.regexp.joni.constants.StackType.POS;

/**
 * Created by modeg on 9/10/2016.
 */
public class Tester {

     public static void main (String[] args) {
          try {
               testDictionary();
          } catch (IOException e) {
               e.printStackTrace();
          }
     }

     public static void testDictionary() throws IOException {

          // construct the URL to the Wordnet dictionary directory
          //    String wnhome = System . getenv (" WNHOME ") ;
          String path = ".\\src\\test\\resources\\wordnet_3.0.0\\wordnet.dict";
          URL url = new URL("file", null, path);

          // construct the dictionary object and open it
          IDictionary dict = new edu.mit.jwi.Dictionary(url);
          dict.open();

          // look up first sense of the word "dog "
/*          IIndexWord idxWord = wordnet.dict.getIndexWord("dog", edu.mit.jwi.item.POS.NOUN);
          IWordID wordID = idxWord.getWordIDs().get(0);
          IWord word = wordnet.dict.getWord(wordID);
          System.out.println("Id = " + wordID);
          System.out.println(" Lemma = " + word.getLemma());
          System.out.println(" Gloss = " + word.getSynset().getGloss());
          System.out.println("===========================================");*/
          WordnetStemmer wordnetStemmer = new WordnetStemmer(dict);

          Arrays.stream(edu.mit.jwi.item.POS.values()).forEach(pos -> {
               List<String> words =  wordnetStemmer.findStems("letters", pos);

               words.stream().forEach(w -> {
                    IIndexWord idxWordz = dict.getIndexWord(w, pos);
                    IWordID wordIDz = idxWordz.getWordIDs().get(0);
                    IWord wordz = dict.getWord(wordIDz);

                    ISynset synset = wordz . getSynset () ;

                    System.out.println("Id = " + pos.name());
                    System.out.println(" Lemma = " + wordz.getLemma());
                    System.out.println(" Gloss = " + wordz.getSynset().getGloss());
                    System.out.print(" Synonyms = ");
                    // iterate over words associated with the synset
       /*             for (IWord xx : synset.getWords()) {
                         System.out.print(xx.getLemma() + ", ");
                    }*/
                  String zz =  synset.getWords().stream()
                            .map(IWord::getLemma)
                            .collect(Collectors.joining(", "));

                    System.out.println(zz);
                    System.out.println("\n===========================================");
               });
          });



     }

}
