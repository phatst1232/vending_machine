/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vending_machine.product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author phats
 */
public class ProductList extends ArrayList<Product>{
    public String dbFile = "productlist.txt";

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
                    String id = stk.nextToken().trim();
                    String name = stk.nextToken().trim();
                    int price = Integer.parseInt(stk.nextToken().trim());                   
                    Product product = new Product(id, name, price);
                    this.add(product);
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
