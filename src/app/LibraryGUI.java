package app;
import service.UserService;
import service.LibraryService;
import javax.swing.*;
import java.awt.*;

public class LibraryGUI {
    static UserService userservice = new UserService();
    static LibraryService libraryservice = new LibraryService();
    
    public static void main(String[] args) {  
        JFrame loginpage = new JFrame("Library Login");
        loginpage.setSize(400, 300);
        loginpage.setLayout(null);
        loginpage.setLocationRelativeTo(null);
        loginpage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 25);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 25);
        JTextField username = new JTextField();
        username.setBounds(150, 50, 180, 25);
        JPasswordField password = new JPasswordField();
        password.setBounds(150, 100, 180, 25);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(140, 160, 100, 30);
        
        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(140, 200, 100, 30);

        loginpage.add(userLabel);
        loginpage.add(username);
        loginpage.add(passLabel);
        loginpage.add(password);
        loginpage.add(loginBtn);
        loginpage.add(registerBtn);
        loginBtn.addActionListener(e -> {
            String user = username.getText();
            String pass = new String(password.getPassword());
            if(userservice.login(user,pass)) {
                JOptionPane.showMessageDialog(loginpage, "Login Successful!");
                loginpage.dispose();
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(loginpage, "Invalid Login");
            }
        });
                registerBtn.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Enter Username:");
            String pass = JOptionPane.showInputDialog("Enter Password:");
            if (user == null || user.trim().isEmpty() ||pass == null || pass.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Fields cannot be empty!");
        return;
    }

    String msg = userservice.register(user, pass);
    JOptionPane.showMessageDialog(null, msg);
});
                loginpage.setVisible(true);

    }
    
    public static void showDashboard() {
        JFrame frame = new JFrame(" Library Dashboard");
        frame.setSize(550, 450);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        JLabel title = new JLabel("Library Management System", JLabel.CENTER);
        title.setBounds(100, 20, 350, 30);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton add = new JButton("Add Book");
        add.setBounds(50, 80, 150, 40);
        JButton view = new JButton("View Books");
        view.setBounds(300, 80, 150, 40);
        JButton issue = new JButton("Issue Book");
        issue.setBounds(50, 150, 150, 40);
        JButton ret = new JButton("Return Book");
        ret.setBounds(300, 150, 150, 40);
        JButton search = new JButton("Search Book");
        search.setBounds(50, 220, 150, 40);
        JButton remove = new JButton("Remove Book");
        remove.setBounds(300, 220, 150, 40);
        JButton update = new JButton("Update Book");
        update.setBounds(50, 290, 150, 40);
        JButton exit = new JButton("Exit");
        exit.setBounds(300, 290, 150, 40);
        frame.add(title);
        frame.add(add);
        frame.add(view);
        frame.add(issue);
        frame.add(ret);
        frame.add(search);
        frame.add(remove);
        frame.add(update);
        frame.add(exit);
        
        add.addActionListener(e -> {
            String t = JOptionPane.showInputDialog("Enter Title:");
            String a = JOptionPane.showInputDialog("Enter Author:");
            String msg = libraryservice.addBook(t, a);
            JOptionPane.showMessageDialog(null, msg);
        });

        view.addActionListener(e -> {
            String data = libraryservice.getAllBooks();
            JTextArea area = new JTextArea(data);
            area.setEditable(false);
            JOptionPane.showMessageDialog(null, new JScrollPane(area));
        });

        issue.addActionListener(e -> {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID:"));
            String msg = libraryservice.issueBook(id);
            JOptionPane.showMessageDialog(null, msg);
        });

        ret.addActionListener(e -> {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID:"));
            String msg = libraryservice.returnBook(id);
            JOptionPane.showMessageDialog(null, msg);
        });

        search.addActionListener(e -> {
            String key = JOptionPane.showInputDialog("Enter keyword:");
            String data = libraryservice.searchBook(key); 
            JTextArea area = new JTextArea(data);
            JOptionPane.showMessageDialog(null, new JScrollPane(area));
        });

        remove.addActionListener(e -> {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID:"));
            String msg = libraryservice.removeBook(id);
            JOptionPane.showMessageDialog(null, msg);
        });

        update.addActionListener(e -> {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID:"));
            String t = JOptionPane.showInputDialog("Enter New Title:");
            String a = JOptionPane.showInputDialog("Enter New Author:");

            String msg = libraryservice.updateBook(id, t, a);
            JOptionPane.showMessageDialog(null, msg);
        });
        exit.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }
}