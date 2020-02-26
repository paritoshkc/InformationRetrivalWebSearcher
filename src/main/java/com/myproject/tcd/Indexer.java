package com.myproject.tcd;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.LMSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import utils.Constants;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Indexer {


    public void createIndex(List<Map<String, String>> cranlist)
    {
        try {
            Directory directory= FSDirectory.open(Paths.get("E:\\Copied code\\InformationRetrival\\src\\main\\resources\\index"));
            Analyzer analyzer ;
            analyzer = new EnglishAnalyzer();
            IndexWriterConfig config= new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            config.setSimilarity(new LMDirichletSimilarity());
            IndexWriter iwriter = new IndexWriter(directory, config);
            for (Map<String, String> cranDict:cranlist
                 ) {


                Document document = new Document();
                document.add(new StringField("ID", cranDict.get("ID"), Field.Store.YES));
                document.add(new TextField("Title", cranDict.get("Title"), Field.Store.YES));
                document.add(new TextField("Bibliography", cranDict.get("Bibliography"), Field.Store.YES));
                document.add(new TextField("Author", cranDict.get("Author"), Field.Store.YES));
                document.add(new TextField("Content", cranDict.get("Content"), Field.Store.YES));
                iwriter.addDocument(document);
            }


            iwriter.close();
            directory.close();

            System.out.println("Indexing Finished");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void  main(String args[])
    {
        FileParser fileIndexer= new FileParser();
        List<Map<String,String>> cranlist= fileIndexer.parseDoc();
        Indexer indexer= new Indexer();
        indexer.createIndex(cranlist);

    }

}
