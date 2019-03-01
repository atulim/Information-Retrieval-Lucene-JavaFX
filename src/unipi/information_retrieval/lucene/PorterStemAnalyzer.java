package unipi.information_retrieval.lucene;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.util.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PorterStemAnalyzer extends Analyzer {

    private Reader reader;
    private String STOP_WORD_FILE = "/common_words";

    public PorterStemAnalyzer() {
        // we initialize the Objects which are used to read the common_words file
        InputStream file = getClass().getResourceAsStream(STOP_WORD_FILE);
        reader = IOUtils.getDecodingReader(file, StandardCharsets.UTF_8);
    }
    private CharArraySet loadStopWordSet() throws IOException {
        //We load the stop words from the common_words file.
        try {
            return WordlistLoader.getWordSet(reader);
        } finally {
            IOUtils.close(reader);
        }
    }

    @Override
    protected TokenStreamComponents createComponents(String s) {
        try {
            // we create a letter tokenizer
            Tokenizer source = new LetterTokenizer();
            source.setReader(reader);
            // We create a LowerCaseFilter which normalizes the token text to lower case
            TokenStream filter = new LowerCaseFilter(source);
            CharArraySet mystopwords = null;
            // we load the stop words
            mystopwords = loadStopWordSet();
            // we read the file
            filter = new StopFilter(filter, mystopwords);
            // we create the PorterStemFilter
            filter = new PorterStemFilter(filter);
            // we return the filter
            return new TokenStreamComponents(source, filter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

