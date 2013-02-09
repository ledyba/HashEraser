package hasheraser;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.*;

public class EraserPrompt {
    public EraserPrompt() {
    }

    public static void main(String[] args) {
        System.out.println("HashEraser v1.0 written by PSI.");
        System.out.println("----");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        File folder = null;
        while (folder == null) {
            try {
                System.out.print("Input folder: ");
                String str = br.readLine();
                if(str.equals(""))continue;
                if(!(str.endsWith("/") || str.endsWith("\\")))str+="/";
                folder = new File(str);
            } catch (IOException ex4) {
                ex4.printStackTrace();
            }
            if (!folder.isDirectory()) {
                System.out.println("Not folder!");
                folder = null;
            }
        }
        try {
            System.out.println("Folder detected:" + folder.getCanonicalPath());
        } catch (IOException ex5) {
            ex5.printStackTrace();
        }
        boolean r = false;
        System.out.println("\n----\nInput options separated by a space.");
        System.out.println("r : match directories recursively.\n");
        try {
            System.out.print("Input options: ");
            String str = br.readLine();
            String op[] = str.split(" ");
            for(int i=0;i<op.length;i++){
                if(op[i].equals("r")){
                    r = true;
                }
            }
        } catch (IOException ex4) {
            ex4.printStackTrace();
        }
        Match match = new Match(folder, r);
        File[][] list = match.match();
        for (int i = 0; i < list.length; i++) {
            System.out.println("\n-----------");
            System.out.println("These files are overlapped.");
            for (int j = 0; j < list[i].length; j++) {
                try {
                    System.out.println(j + ":" + list[i][j].getCanonicalPath());
                } catch (IOException ex3) {
                    ex3.printStackTrace();
                }
            }
            System.out.println("Which files do you erase?");
            System.out.println(
                    "Input numbers separated by a space such as:1 2 3");
            String item = "";
            try {
                System.out.print("?:");
                item = br.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            String num[] = item.split(" ");
            for (int k = 0; k < num.length; k++) {
                int num_f = 0;
                try {
                    num_f = Integer.parseInt(num[k]);
                } catch (NumberFormatException ex2) {
                    continue;
                }
                try {
                    System.out.print(list[i][num_f].getCanonicalPath());
                } catch (IOException ex6) {
                    ex6.printStackTrace();
                }
                if (list[i][num_f].delete()) {
                    System.out.println(": ERASED");
                }else{
                    System.out.println(": NOT ERASED");
                }
            }
        }
        System.out.println("\nAll done^^");
        try {
            br.close();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
    }
}
