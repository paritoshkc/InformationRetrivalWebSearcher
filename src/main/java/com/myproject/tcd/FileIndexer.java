package com.myproject.tcd;

import utils.Constants;

import java.io.*;
import java.util.*;

public class FileIndexer {


    public List<Map<String, String>> parseCran()
    {
        List<Map<String, String>> dataList= new ArrayList<Map<String, String>>();
        File file=new File("src/main/resources/cran/cran.all.1400");

        try {
            FileReader reader= new FileReader(file);
            //Create a dictionary to store the values
            Map<String, String> cranDict = new HashMap<String, String>();

            String sentence;
            String next=Constants.ID;
            int lineNum=0;


//            initializing all fields in the document to zero and iterating over each line to create document set
            String id = "";
            String title = "";
            String author = "";
            String bibliography = "";
            String content = "";

            //using scanner to read line by line
            Scanner scanner= new Scanner(file);
            scanner.useDelimiter("\n");

            while(scanner.hasNextLine()) {
                sentence = scanner.nextLine();
                lineNum++;
                String[] words = sentence.split("\\s+");
                switch (words[0]) {
                    //storing only ids of the documents
                    case Constants.ID:
                        if (lineNum > 1) {
                            cranDict.put("ID", id);
                            cranDict.put("Content", content);
                            dataList.add(cranDict);
                            cranDict = new HashMap<String, String>();
                        }
                        id = words[1];
                        content = "";
                        next = Constants.TITLE;
                        break;
                    case Constants.TITLE:
                        next = Constants.AUTHOR;
                        break;

                    case Constants.AUTHOR:
                        if (next != Constants.AUTHOR) {
                        }
                        cranDict.put("Title", title);
                        title = "";
                        next = Constants.BIBLIOGRAPHY;
                        break;

                    case Constants.BIBLIOGRAPHY:
                        cranDict.put("Author", author);
                        author = "";
                        next = Constants.CONTENT;
                        break;

                    case Constants.CONTENT:
                        cranDict.put("Bibliography", bibliography);
                        bibliography = "";
                        next = Constants.ID;
                        break;
                    default:
                        //add all the contents of one document together.
                        switch (next) {
                            case ".A":
                                title += sentence + " ";
                                break;

                            case ".B":
                                author += sentence + " ";
                                break;

                            case ".W":
                                bibliography += sentence + " ";
                                break;

                            case ".I":
                                content += sentence + " ";
                                break;
                        }

                }
            }
            cranDict.put("ID", id);
            cranDict.put("Content", content);
            dataList.add(cranDict);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public List<Map<String,String>> parseQuery()
    {

        List<Map<String,String>> queryList= new ArrayList<>();
        String nextline1="";
        try {
            Scanner scanner = new Scanner(new File("src/main/resources/cran/cran.qry"));
            String query = "";
            String line = "";
            int queryNum = 1;
            while (scanner.hasNextLine()) {

                Map<String, String> querymap = new HashMap<>();
                if (nextline1.equals("") )line = scanner.nextLine();
                else line=nextline1;


                String words[] = line.split("\\s+");
               // if ( words[0].equals(Constants.CONTENT)||words[1] != "365") {
                    switch (words[0]) {
                        case Constants.ID:
                            String nextline = scanner.nextLine();
                            if (nextline.equals(Constants.CONTENT)) {
                                querymap.put("ID", Integer.toString(queryNum));
                                queryNum++;
                                query = scanner.nextLine();
                                nextline1 = scanner.nextLine();
                                if (nextline1.split("\\s+")[0].equals(Constants.ID)) {
//                                    query.replace("?","").replace("*","");
                                    querymap.put("QUERY", query);
                                    query = "";
                                    queryList.add(querymap);
                                    continue;
                                }
                                else {
                                    while (!nextline1.split("\\s+")[0].equals(Constants.ID) && scanner.hasNextLine()) {
                                        query = query +" "+ nextline1;
                                        nextline1 = scanner.nextLine();
                                    }
                                    if(!scanner.hasNextLine())
                                        query=query+" "+nextline1;
                                }
//                                query.replace("?","").replace("*","");
                                querymap.put("QUERY", query);
                                query = "";
                                queryList.add(querymap);
                            }
                            break;

                    }
                //}
            }

            } catch(FileNotFoundException e){
                e.printStackTrace();
            }

        return queryList;
    }



public static void main(String args[])
{
    FileIndexer fileIndexer= new FileIndexer();
    List<Map<String, String>> list = fileIndexer.parseQuery();
    System.out.println(list.size());
    for (Map<String,String> mapobj:list
         ) {
        System.out.println(mapobj.get("ID"));
        System.out.println(mapobj.get("QUERY"));
    }

}
    }
