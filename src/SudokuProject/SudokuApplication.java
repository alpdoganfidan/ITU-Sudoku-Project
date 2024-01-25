package SudokuProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import DatabaseConnection.Database_UserProcess;

public class SudokuApplication extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private String username;

    public SudokuApplication() {
    	loginScreen();
    }

    private void changeScreen() {
        getContentPane().removeAll(); // Önceki bileşenleri temizle
        revalidate();
        repaint();
    }
    
    private void loginScreen() {
        setTitle("Sudoku Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Log in");
        JButton signupButton = new JButton("Sign up");

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(usernameLabel, constraints);

        constraints.gridx = 1;
        panel.add(usernameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(passwordLabel, constraints);

        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel.add(loginButton, constraints);

        constraints.gridy = 3;
        panel.add(signupButton, constraints);

        add(panel, BorderLayout.CENTER);

        pack();
        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Burada kullanıcı adı ve şifre ile veritabanı kontrol ediliyor
                if (Database_UserProcess.checkCredentials(username, password)) {
                    // Giriş başarılıysa ilgili ekranı gösterin
                    //JOptionPane.showMessageDialog(null, "Log in success!");
                    mainScreen(username);
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong username or password!");
                }
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Burada kullanıcı adının veritabanında kontrol edilmesi gerekiyor
                if (username.length()<6){
                	JOptionPane.showMessageDialog(null, "Username must be at least 6 characters long!");
                }
                else if (Database_UserProcess.checkUsername(username)) {
                    JOptionPane.showMessageDialog(null, "This username is already in use!");
                } else {
                    // Burada kullanıcı veritabanına kaydediliyor
                	Database_UserProcess.registerUser(username, password);
                    JOptionPane.showMessageDialog(null, "Sign up success!");
                }
            }
        });
    }
    
    private void mainScreen(String kullaniciAdi) {
        changeScreen();

        setTitle("Sudoku Main Menu");
        // İlgili ekranı burada oluşturun
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel label = new JLabel("Welcome, " + kullaniciAdi + "!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));

        JButton newGameButton = new JButton("New Game");
        JButton loadGameButton = new JButton("Load Game");
        JButton statisticsButton = new JButton("Statistics");
        JButton cikisButton = new JButton("Quit Game");
        
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	newGameScreen();
            }
        });
        
        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	SudokuInitialGUI.loadSudoku("58",username);
            }
        });
        
        cikisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Uygulamayı kapat
                System.exit(0);
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(label, constraints);

        constraints.gridy = 1;
        panel.add(newGameButton, constraints);

        constraints.gridy = 2;
        panel.add(loadGameButton, constraints);

        constraints.gridy = 3;
        panel.add(statisticsButton, constraints);
        
        constraints.gridy = 4;
        panel.add(cikisButton, constraints);

        add(panel, BorderLayout.CENTER);

        pack();
    }

    private void newGameScreen() {

    	changeScreen();
    	
    	setTitle("Sudoku New Game");
    	
    	JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);
    	
        JButton easyButton = new JButton("EASY");
        JButton mediumButton = new JButton("MEDIUM");
        JButton hardButton = new JButton("HARD");
        JButton veryHardButton = new JButton("VERY HARD");
        JButton insaneButton = new JButton("INSANE");
        JButton mainMenuButton = new JButton("ANA MENUYE DÖN");

        easyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // EASY seviyesinde Sudoku oyununu başlatmak için ilgili metod çağrılır
            	SudokuInitialGUI.newSudoku(username,Difficulty.EASY);
            }
        });

        mediumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // MEDIUM seviyesinde Sudoku oyununu başlatmak için ilgili metod çağrılır
            	SudokuInitialGUI.newSudoku(username,Difficulty.MEDIUM);
            }
        });

        hardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // HARD seviyesinde Sudoku oyununu başlatmak için ilgili metod çağrılır
            	SudokuInitialGUI.newSudoku(username,Difficulty.HARD);
            }
        });

        veryHardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // VERY_HARD seviyesinde Sudoku oyununu başlatmak için ilgili metod çağrılır
            	SudokuInitialGUI.newSudoku(username, Difficulty.VERY_HARD);
            }
        });

        insaneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // INSANE seviyesinde Sudoku oyununu başlatmak için ilgili metod çağrılır
            	SudokuInitialGUI.newSudoku(username,Difficulty.INSANE);
            }
        });

        mainMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainScreen(username);
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(easyButton, constraints);

        constraints.gridy = 1;
        panel.add(mediumButton, constraints);

        constraints.gridy = 2;
        panel.add(hardButton, constraints);

        constraints.gridy = 3;
        panel.add(veryHardButton, constraints);
        
        constraints.gridy = 4;
        panel.add(insaneButton, constraints);

        constraints.gridy = 5;
        panel.add(mainMenuButton, constraints);
        
        add(panel, BorderLayout.CENTER);
        

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SudokuApplication();
            }
        });
    }
}