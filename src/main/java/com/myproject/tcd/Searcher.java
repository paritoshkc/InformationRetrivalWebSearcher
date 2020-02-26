package com.myproject.tcd;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Searcher {

    private int HITS_PER_PAGE=100;

    public void searchCranQueries(List<Map<String, String>> cranQueryList)
    {
        int i=0;
        Map<String, List<String>> resultDict;
        resultDict = new HashMap<String, List<String>>();
        try {
            Directory directory= FSDirectory.open(Paths.get("E:\\Copied code\\InformationRetrival\\src\\main\\resources\\index"));
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);
            isearcher.setSimilarity(new BM25Similarity());
            Analyzer analyzer;
            analyzer=new EnglishAnalyzer();
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

                Files.write(Paths.get("output/results.txt"), resFileContent, Charset.forName("UTF-8"));
                System.out.println("Results written to output/results.txt to be used in TREC Eval.");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }


    public static void main(String args[])
    {

        FileParser fileIndexer= new FileParser();
        Indexer indexer= new Indexer();

        List<Map<String,String>> cranQueryList= fileIndexer.parseQuery();
        List<Map<String,String>> cranList= fileIndexer.parseDoc();
        indexer.createIndex(cranList);
        Searcher searcher = new Searcher();
        searcher.searchCranQueries(cranQueryList);

    }

}
