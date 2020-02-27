package com.myproject.tcd;

import utils.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class FileParser {

    public List<Map<String,String>> parseDoc()
    {
        List<Map<String, String>> dataList= new ArrayList<Map<String, String>>();
        File file=new File("src/main/resources/cran/cran.all.1400");
        try {
            FileReader reader= new FileReader(file);
            Map<String, String> cranDict = new HashMap<String, String>();

            String sentence;
            String token=Constants.ID;
            int lineNum=0;
            String id = "";
            String title = "";
            String author = "";
            String bibliography = "";
            String content = "";
            Scanner scanner= new Scanner(file);
            while (scanner.hasNextLine())
            {
                lineNum++;
                sentence = scanner.nextLine();
                String[] words = sentence.split("\\s+");
                if (words[0].equals(Constants.ID))
                {
                    if (lineNum > 1) {
                        cranDict.put("ID", id);
                        cranDict.put("Content", content);
                        dataList.add(cranDict);
                        cranDict = new HashMap<String, String>();
                    }
                    id = words[1];
                    content = "";
                    token = Constants.TITLE;

                }
                else if (words[0].equals(Constants.TITLE))
                {
                    token = Constants.AUTHOR;
                }
                else if (words[0].equals(Constants.AUTHOR))
                {
                    if (token != Constants.AUTHOR) {
                    }
                    cranDict.put("Title", title);
                    title = "";
                    token = Constants.BIBLIOGRAPHY;
                }
                else if (words[0].equals(Constants.BIBLIOGRAPHY))
                {
                    cranDict.put("Author", author);
                    author = "";
                    token = Constants.CONTENT;
                }
                else if (words[0].equals(Constants.CONTENT))
                {
                    cranDict.put("Bibliography", bibliography);
                    bibliography = "";
                    token = Constants.ID;
                }

                else
                {
                    if (token.equals(".A"))
                        title += sentence + " ";
                    else if (token.equals(".B"))
                        author += sentence + " ";
                    else if (token.equals(".W"))
                        bibliography += sentence + " ";
                    else if (token.equals(".I"))
                        content += sentence + " ";

                }

            }

            cranDict.put("ID", id);
            cranDict.put("Content", content);
            dataList.add(cranDict);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Create a dictionary to store the values


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
                            } else {
                                while (!nextline1.split("\\s+")[0].equals(Constants.ID) && scanner.hasNextLine()) {
                                    query = query + " " + nextline1;
                                    nextline1 = scanner.nextLine();
                                }
                                if (!scanner.hasNextLine())
                                    query = query + " " + nextline1;
                            }
//                                query.replace("?","").replace("*","");
                            querymap.put("QUERY", query);
                            query = "";
                            queryList.add(querymap);
                        }
                        break;

                }
            }

        } catch(FileNotFoundException e){
            e.printStackTrace();
        }

        return queryList;
    }



    public static void main(String args[])
    {

        FileParser fileParser= new FileParser();
        List<Map<String, String>> list = fileParser.parseDoc();
        System.out.println(list.size());
        for (Map<String,String> mapobj:list
        ) {
            System.out.println(mapobj.get("ID"));
            System.out.println(mapobj.get("Content"));
        }

    }
}
