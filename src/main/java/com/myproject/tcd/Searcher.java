package com.myproject.tcd;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Searcher {

    private int HITS_PER_PAGE=100;
    private  String filePath="";

    public String searchCranQueries(List<Map<String, String>> cranQueryList, Indexer indexer)
    {
        int i=0;
        Map<String, List<String>> resultDict;
        resultDict = new HashMap<String, List<String>>();
        try {

            List<String> stopWordList = Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "she'd", "she'll", "she's", "should", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves");
            CharArraySet stopWordSet = new CharArraySet( stopWordList, true);

            Directory directory= FSDirectory.open(Paths.get("E:\\Copied code\\InformationRetrival\\src\\main\\resources\\index"));
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);
            isearcher.setSimilarity(indexer.getSimilarity());
            Analyzer analyzer= indexer.getAnalyzer();
            IndexWriterConfig config= new IndexWriterConfig(analyzer);
            List<String> resFileContent = new ArrayList<String>();
            for (Map<String, String> cranQuery:
            cranQueryList ) {
                i++;
                MultiFieldQueryParser queryParser= new MultiFieldQueryParser( new String[]{"Title","Bibliography","Author","Content"},analyzer);
                Query query = queryParser.parse(queryParser.escape(cranQuery.get("QUERY")));
                TopDocs topDocs = isearcher.search(query,HITS_PER_PAGE);
                ScoreDoc[] hits= topDocs.scoreDocs;
                List<String> resultList = new ArrayList<String>();
                for(int j = 0; j < hits.length; j++) {

                    int docId = hits[j].doc;
                    Document doc = isearcher.doc(docId);
                    resultList.add(doc.get("ID"));
                    resFileContent.add(cranQuery.get("ID") + " 0 " + doc.get("ID") + " 0 " + hits[j].score + " STANDARD");
                }
                resultDict.put(Integer.toString(i + 1), resultList);
                File outputDir = new File("output");
                if (!outputDir.exists()) outputDir.mkdir();
                filePath="output/results"+"_"+indexer.getAnalyzerString().replace(" ","")+"_"+indexer.getSimilarityString().replace(" ","")+".txt";

                Files.write(Paths.get(filePath), resFileContent, Charset.forName("UTF-8"));
//                System.out.println("Results written to output/results.txt to be used in TREC Eval.");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return filePath;
    }


    public static void main(String args[])
    {

//        FileParser fileIndexer= new FileParser();
//        Indexer indexer= new Indexer();
//
//        List<Map<String,String>> cranQueryList= fileIndexer.parseQuery();
//        List<Map<String,String>> cranList= fileIndexer.parseDoc();
//        indexer.createIndex(cranList);
//        Searcher searcher = new Searcher();
//        searcher.searchCranQueries(cranQueryList);

    }

}
