/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import java.sql.Date;

/**
 *
 * @author jahna
 */
public class LibraryBook {
    private int id;
    private String title;
    private String author;
    private boolean available;
    private Date issueDate;
    private Date dueDate;

    public LibraryBook(int id, String title, String author, boolean available, Date issueDate, Date dueDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = available;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }
    
}
