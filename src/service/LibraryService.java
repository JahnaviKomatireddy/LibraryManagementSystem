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
        String query = "INSERT INTO books (title, author, available) VALUES (?, ?, true)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, title);
        ps.setString(2, author);
        ps.executeUpdate();
        return "Book added successfully!";
    } catch (Exception e) {
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
            String   check = "SELECT available FROM books WHERE id=?";
            PreparedStatement ps1 = con.prepareStatement(check);
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();
            if (rs.next() && rs.getBoolean("available")) {
                String query = "UPDATE books SET available=false, issue_date=CURDATE(), due_date=DATE_ADD(CURDATE(), INTERVAL 7 DAY) WHERE id=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
                return "Book issued and Due in 7 days.";
            } else {
                return "Book not available!";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String returnBook(int id) {
        try (Connection con = DatabaseConnector.getConnection()) {
            String query = "SELECT due_date FROM books WHERE id=?";
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
                sb.append("\nID: ").append(rs.getInt("id")).append("\n");
                sb.append("Title: ").append(rs.getString("title")).append("\n");
                sb.append("Author: ").append(rs.getString("author")).append("\n");
                sb.append("Status: ")
                  .append(rs.getBoolean("available") ? "Available" : "Issued");
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