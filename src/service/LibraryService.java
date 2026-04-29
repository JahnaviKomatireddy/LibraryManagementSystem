package service;
import db.DatabaseConnector;
import java.sql.*;
import java.util.Date;

public class LibraryService {
    public String addBook(String title, String author) {
        if (title == null || title.trim().isEmpty() ||
        author == null || author.trim().isEmpty()) {
        return "Invalid input!";
    }
        try (Connection con = DatabaseConnector.getConnection()) {
            String check = "SELECT quantity FROM books WHERE title=? AND author=?";
            PreparedStatement ps1 = con.prepareStatement(check);
            ps1.setString(1, title);
            ps1.setString(2, author);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
               String update = "UPDATE books SET quantity = quantity + 1 WHERE title=? AND author=?";
               PreparedStatement ps2 = con.prepareStatement(update);
               ps2.setString(1, title);
               ps2.setString(2, author);
               ps2.executeUpdate();

               return "Book count updated";
        }
            
            String query = "INSERT INTO books (title, author, available) VALUES (?, ?, true)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.executeUpdate();
            return "Book added successfully!";
    } 
        catch (Exception e) {
        return e.getMessage();
    }
}
    public String removeBook(int id) {
        try (Connection con = DatabaseConnector.getConnection()) {
            String query = "DELETE FROM books WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return (rows > 0) ? "Book removed successfully!" : "Book not found!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String updateBook(int id, String title, String author) {
        try (Connection con = DatabaseConnector.getConnection()) {
            String query = "UPDATE  books SET title=?, author=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, id);
            int rows = ps.executeUpdate();
            return (rows > 0) ? "Book updated successfully!" : "Book not found!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String issueBook(int id) {
        try (Connection con = DatabaseConnector.getConnection()) {
            String   check = "SELECT quantity FROM books WHERE id=?";
            PreparedStatement ps1 = con.prepareStatement(check);
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("quantity");
                if (quantity > 0) {
                String student = javax.swing.JOptionPane.showInputDialog("Enter Student Name:");
                if (student == null || student.trim().isEmpty()) {
                    return "Student name required!";
                }

                String update = "UPDATE books SET quantity = quantity - 1, issue_date=CURDATE(), "
                        + "due_date=DATE_ADD(CURDATE(), INTERVAL 7 DAY) WHERE id=?";
                PreparedStatement ps = con.prepareStatement(update);
                ps.setInt(1, id);
                ps.executeUpdate();
                String insert = "INSERT INTO issued_books (book_id, student_name, "
                        + "issue_date, due_date) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY))";
                PreparedStatement ps3 = con.prepareStatement(insert);
                ps3.setInt(1, id);
                ps3.setString(2, student);
                ps3.executeUpdate();

                return "Book issued to " + student;
            } else {
                return "Book not available!";
            }}
            return "Book not found";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String returnBook(int id) {
        try (Connection con = DatabaseConnector.getConnection()) {
            String query = "SELECT due_date FROM issued_books WHERE id=? AND return_date IS NULL";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Date dueDate = rs.getDate("due_date");
                Date today = new Date(System.currentTimeMillis());
                long diff = today.getTime() - dueDate.getTime();
                long daysLate = diff / (1000 * 60 * 60 * 24);
                int fine = 0;
                if (daysLate > 0) {
                    fine = (int) daysLate * 50;
                } 
                String updateIssue = "UPDATE issued_books SET return_date = CURDATE() "
                        + "WHERE book_id=? AND return_date IS NULL";
                 PreparedStatement ps2 = con.prepareStatement(updateIssue);
                ps2.setInt(1, id);
                ps2.executeUpdate();
                
                String update = "UPDATE books SET quantity = quantity + 1 WHERE id=?";
                PreparedStatement ps3 = con.prepareStatement(update);
                ps3.setInt(1, id);
                ps3.executeUpdate();
                if (daysLate > 0) {
                return "Late by " + daysLate + " days. Fine: ₹" + fine;
            } else {
                return "Returned on time. No fine.";
            }
            } else {
                return "Book not found!";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String getAllBooks() {
        StringBuilder sb = new StringBuilder();

        try (Connection con = DatabaseConnector.getConnection()) {

            String query = "SELECT * FROM books";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                int quantity = rs.getInt("quantity");
                sb.append("\nID: ").append(rs.getInt("id")).append("\n");
                sb.append("Title: ").append(rs.getString("title")).append("\n");
                sb.append("Author: ").append(rs.getString("author")).append("\n");
                sb.append("Quantity: ").append(quantity).append("\n");
                sb.append("Status: ").append(quantity > 0 ? "Available" : "Out of Stock\n");
            }

        } catch (Exception e) {
            return e.getMessage();
        }

        if (sb.length() == 0) {
            return "No books available!";
        }

        return sb.toString();
    }
    public String searchBook(String keyword) {
    StringBuilder sb = new StringBuilder();
    try (Connection con = DatabaseConnector.getConnection()) {
        String query = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            sb.append("ID: ").append(rs.getInt("id")).append("\n");
            sb.append("Title: ").append(rs.getString("title")).append("\n");
            sb.append("Author: ").append(rs.getString("author")).append("\n");
            sb.append("Status: ")
              .append(rs.getBoolean("available") ? "Available" : "Issued").append("\n");
        }

    } catch (Exception e) {
        return e.getMessage();
    }

    return sb.toString();
}
}