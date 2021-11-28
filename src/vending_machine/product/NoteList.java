/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vending_machine.product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author phats
 */
public class NoteList extends ArrayList<Note>{
    public String dbFile = "notelist.txt";

    public void setDbFile(String dbFile) {
        this.dbFile = dbFile;
    }

    public String getDbFile() {
        return dbFile;
    }
    
    public boolean loadFromFile(String fName) {
        FileReader fr = null;
        BufferedReader br = null;
        String line = null;
        StringTokenizer stk = null;
        try {
            fr = new FileReader(fName);
            br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                line.trim();
                if (line.length() > 0) {
                    stk = new StringTokenizer(line, ";");
                    String denomination = stk.nextToken().trim();                   
                    int value = Integer.parseInt(stk.nextToken().trim());                   
                    Note note = new Note(denomination, value);
                    this.add(note);
                }
            }
            br.close();
            fr.close();
            System.out.println("Load from file DONE!");
        } catch (Exception e) {
            System.out.println("Load from file error!");
        }
        return true;
    }
}
