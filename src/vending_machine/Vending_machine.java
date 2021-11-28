/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vending_machine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import my_libraries.Menu;
import vending_machine.product.Note;
import vending_machine.product.NoteList;
import vending_machine.product.Product;
import vending_machine.product.ProductList;

/**
 *
 * @author phats
 */
public class Vending_machine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // set up first time
        ProductList product_list = new ProductList();
        product_list.loadFromFile(product_list.getDbFile());
        NoteList note_list = new NoteList();
        note_list.loadFromFile(note_list.getDbFile());
        boolean leave = false;
        boolean next = false;
        boolean cont = false;
        boolean comeback = false;
        double win_rate = 0.1;
        int choice;
        int sub_choice;
        int balance = 0;
        int purchase_count = 0;
        int promotion_limit = 0;
        String last_product_id = "";
        LocalDate localDate = LocalDate.now();
        // create Menu
        Menu note_menu = new Menu("Choose notes to insert to the Machine or come back tomorrow:");
        for (Note n : note_list) {
            note_menu.add(n.getDenomination() + " VND");
        }
        note_menu.add("Come back tomorrow");

        Menu sub_note_menu = new Menu("Continue inserting notes or choose product");
        sub_note_menu.add("Continue");
        sub_note_menu.add("Choose product");

        Menu main_menu = new Menu("\n************MENU************\n"
                + "Select an option below:");
        for (Product p : product_list) {
            main_menu.add(p.getName());
        }
        main_menu.add("Insert more notes");
        main_menu.add("Come back tomorrow");

        Menu sub_menu = new Menu("Your balance is insufficient!");
        sub_menu.add("Insert more notes");
        sub_menu.add("Choose another product");

        // start to interact with the machine
        do {
            // set promotion win rate
            if (comeback) {
                if (promotion_limit < 50000) {
                    if (win_rate*1.5 < 1) {
                        win_rate *= 1.5;
                    } else {
                        // if change > 1: win rate = 100% 
                        win_rate = 1;
                    }
                }
                // reset promotion budget
                promotion_limit = 0;
                comeback = false;
            }

            // do insert notes
            do {
                next = false;
                cont = false;
                // choose note menu
                System.out.println("\n\n<**************************************************************** " + dateToString(localDate) + " ***>\n"
                        + "                         Welcome to Mini Vending Machine\n");
                System.out.println("Balance: " + balance + " VND");
                choice = note_menu.getUserChoice();
                if (choice <= note_list.size()) {
                    balance += note_list.get(choice - 1).getValue();
                    // sub_menu: continue insert or come to product 
                    System.out.println("\n\n<**************************************************************** " + dateToString(localDate) + " ***>\n"
                            + "                         Welcome to Mini Vending Machine\n");
                    System.out.println("Balance: " + balance + " VND");
                    sub_choice = sub_note_menu.getUserChoice();
                    if (sub_choice == 2) {
                        cont = true; // switch to main_menu
                    } else if (sub_choice == 3) {
                        leave = true; // quit program
                    }
                } else if (choice == note_list.size() + 1) {    
                    // come back tomorrow
                    cont = true;
                    comeback = true;
                    localDate = localDate.plusDays(1);
                    next = true;
                    if (balance > 0) {
                        System.out.println("Remaining change: " + balance + " VND");
                        balance = 0;
                    }

                    System.out.println("------------Thank you!------------\n");
                    System.out.println("Press Enter to continue...");
                    new java.util.Scanner(System.in).nextLine();
                } else if (choice == note_menu.size() + 1) {                   
                    leave = true;
                }

            } while (!leave && !cont);

            // Select drinks (Product)
            if (!leave && !next) {
                next = false;
                do {
                    System.out.println("\n\n<**************************************************************** " + dateToString(localDate) + " ***>\n"
                            + "                          Welcome to Mini Vending Machine\n");
                    System.out.println("Balance: " + balance + " VND");
                    choice = main_menu.getUserChoice();
                    if (choice <= product_list.size()) {
                        // Check if balance is enough
                        if (product_list.get(choice - 1).getPrice() <= balance) {
                            //Drop product, update balance
                            balance = balance - product_list.get(choice - 1).getPrice();
                            System.out.println("Product dropped down below! Balance: " + balance + " VND");
                            purchase_count++;

                            //Check promote condition
                            if (product_list.get(choice - 1).getId().equals(last_product_id)) {
                                if (purchase_count == 3) {
                                    //Check if win the promotion
                                    if (checkPromotion(win_rate)) {
                                        //Check limit of promotion
                                        if ((promotion_limit + product_list.get(choice - 1).getPrice()) <= 50000) {
                                            System.out.println("******************************************************");
                                            System.out.println("* Congratulation!!! You won a same product for free! *");
                                            System.out.println("******************************************************\n");
                                            promotion_limit += product_list.get(choice - 1).getPrice();
                                        } else {
                                            System.out.println("Today's promotion of this product has ended. Come back tomorrow!");
                                        }
                                        // reset win_rate
                                        win_rate = 0.1;
                                    } else {
                                        System.out.println("******************************************************");
                                        System.out.println("*               Good luck next time!                 *");
                                        System.out.println("******************************************************\n");
                                    }
                                    // after promotion reset
                                    purchase_count = 0;
                                }
                            } else {
                                // if not the same id -> reset count
                                purchase_count = 1;
                            }
                            //save last product id
                            last_product_id = product_list.get(choice - 1).getId();
                            //if promote budget available for all product -> put an offer 
                            if (promotion_limit <= 30000) {
                                System.out.println("Do " + (3 - purchase_count) + " more purchases of the same product to have " + (Math.round(win_rate * 100)) + "% chance of winning a free one!");
                            }
                            System.out.println("Press Enter to continue...");
                            new java.util.Scanner(System.in).nextLine();
                        } else {
                            // balance is not enough
                            System.out.println("\n\n<**************************************************************** " + dateToString(localDate) + " ***>\n"
                                    + "                         Welcome to Mini Vending Machine\n");
                            System.out.println("Balance: " + balance + " VND");
                            //Balance not enough, drop 2 option: insert more notes || choose other product
                            sub_choice = sub_menu.getUserChoice();
                            if (sub_choice == 1) {
                                //Back to insert notes menu
                                next = true;
                            } else if (sub_choice == 3) {
                                leave = true;
                            }
                        }
                    } else if (choice == product_list.size() + 1) {
                        //Back to insert notes menu
                        next = true;
                    } else if (choice == product_list.size() + 2) {     
                        // come back tomorrow
                        comeback = true;
                        localDate = localDate.plusDays(1);
                        next = true;
                        if (balance > 0) {
                            System.out.println("Remaining change: " + balance + " VND");
                            balance = 0;
                        }

                        System.out.println("------------Thank you!------------\n");
                        System.out.println("Press Enter to continue...");
                        new java.util.Scanner(System.in).nextLine();
                    } else if (choice == main_menu.size() + 1) {
                        leave = true;
                    }
                } while (!leave && !next);
            }
        } while (!leave);

        // ramaining change and Goodbye if leave
        System.out.println("");
        if (balance > 0) {
            System.out.println("Remaining change: " + balance + " VND");
            balance = 0;
        }

        System.out.println("------------Thank you!------------\n");
    }

    // My library
    public static boolean checkPromotion(double change) {
        double x = Math.random();
        return (x <= change);
    }

    public static String dateToString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return localDate.format(formatter);
    }
}
