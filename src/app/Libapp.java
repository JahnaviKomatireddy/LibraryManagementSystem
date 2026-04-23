/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;
import service.*;
import java.util.Scanner;

public class Libapp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        LibraryService lib = new LibraryService();

        System.out.println("1. Register\n2. Login");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            System.out.print("Username: ");
            String u = sc.nextLine();
            System.out.print("Password: ");
            String p = sc.nextLine();
            userService.register(u, p);
        }

        System.out.print("Login Username: ");
        String u = sc.nextLine();
        System.out.print("Password: ");
        String p = sc.nextLine();

        if (userService.login(u, p)) {
            while (true) {
               System.out.println("Choose a option");
               System.out.println("Warning: View Books before adding new books");
               System.out.println("1. Add Book");
               System.out.println("2. View Book");
               System.out.println("3. Issue Book");
               System.out.println("4. Return Book");
               System.out.println("5. Search Book");
               System.out.println("6. Exit System");
               int ch = sc.nextInt();

                switch (ch) {
                    case 1:
                        sc.nextLine();
                        System.out.print("Title: ");
                        String t = sc.nextLine();
                        System.out.print("Author: ");
                        String a = sc.nextLine();
                        System.out.println(lib.addBook(t, a));
                        break;

                    case 2:
                        System.out.println(lib.getAllBooks());
                        break;

                    case 3:
                        System.out.print("ID: ");
                        int issueId = sc.nextInt();
                        System.out.println(lib.issueBook(issueId));
                        break;

                    case 4:
                        System.out.print("ID: ");
                        int returnId = sc.nextInt();
                        System.out.println(lib.returnBook(returnId));
                        break;

                    case 5:
                        sc.nextLine();
                        System.out.print("Keyword: ");
                        String key = sc.nextLine();
                        System.out.println(lib.searchBook(key));
                        break;

                    case 6:
                        System.exit(0);
                   }
            }
        }
    }
}