/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_libraries;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author phats
 */
public class Menu extends ArrayList<String> {

    String msg = "";
    public static Scanner sc = new Scanner(System.in);

    public Menu(String msg) {
        super();
        this.msg = msg;
    }

    public int getUserChoice() {
        int choice = 0;
        boolean redo = true;
        System.out.println(msg);
        do {
            for (int i = 0; i < this.size(); i++) {
                System.out.println(i + 1 + ". " + this.get(i));
            }
            System.out.println(this.size() + 1 + ". Leave");
            try {
                System.out.println("Your choice(1-" + (this.size() + 1) + "):");
                choice = Integer.parseInt(sc.nextLine());
                if (redo = (choice < 1 || choice > this.size() + 1)) {
                    System.out.println("\n*****************************************");
                    System.out.println("* Input out of range! Please try again! *");
                    System.out.println("*****************************************\n");
                }
            } catch (Exception e) {
                System.out.println("\n******************************");               
                System.out.println("* Please, input numper only! *");
                System.out.println("******************************\n");
            }
        } while (redo);
        return choice;
    }
}
