package com.myproject.tcd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class tester {


    public String getString(Scanner scanner,String token)
    {
        while(!scanner.nextLine().contains(token))
        {
            System.out.println();
        }

        return "";
    }
    public static void main(String args[])
    {
        Scanner scanner= null;
        String token=".I";
        List check= new ArrayList<>();
        String newline = "";
        try {
            scanner = new Scanner(new File("src/main/resources/cran/cran.all.1400"));
            String line="";
            while (scanner.hasNext())
            {
                line=scanner.nextLine();
                if (line.contains(token))
                {
                    newline=newline+" "+scanner.nextLine();


                }
            }
            check.add(line);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            for (Object test: check
                 ) {
                System.out.println(test.toString());
            }

        }

    }

}
