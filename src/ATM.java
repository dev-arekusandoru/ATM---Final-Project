import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

/**
 * @author: Alexandru Muresan
 **/

public class ATM implements ActionListener {

    // stores all accounts and users read from data files
    public static ArrayList<Checking> accounts = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();

    // stores string representation of current user accounts
    // and current account transaction history
    public static DefaultListModel<String> accountsModel = new DefaultListModel<>();
    public static DefaultListModel<String> historyModel = new DefaultListModel<>();

    // stores current user and account
    public static User currentUser = null;
    public static Checking currentAccount = null;

    // theme colors(potential theme customization)
    public static Color themeC = new Color(255, 255, 255);
    public static Color themeCinv = new Color(0, 0, 0);

    ////////---LOGIN FRAME VARS---////////
    JFrame loginFrame;
    Button login;
    Button newUser;

    JTextField loginUsername;
    JPasswordField loginPassword;

    ////////---NEW USER FRAME VARS---////////
    JFrame newUserFrame;

    JTextField newUserUsername;
    JPasswordField newUserPassword;

    Button createUser;

    ////////---MAIN FRAME VARS---////////
    JFrame mainFrame;

    JLabel userInfoLabel;

    public static String username;
    public static String password;

    JButton logout;

    JList accountViewer;

    JScrollPane accountScroll;

    Button view;
    Button create;
    Button delete;

    JButton settings;

    ////////---ACCOUNT FRAME VARS---////////
    JFrame accountFrame;
    JLabel accountInfoLabel;
    JLabel accountBalanceLabel;

    JList historyViewer;

    Button deposit;
    Button withdraw;
    Button transfer;

    ////////---SETTINGS FRAME VARS---////////
    JFrame settingsFrame;

    Button changeUsername;
    Button changePassword;
    Button deleteAccount;

    //GUI constructor
    public ATM() {
        //set font to arial
        setUIFont(new FontUIResource(new Font("Arial", 0, 15)));
        //set optionpanes to theme
        updateTheme(themeC, themeCinv);
////////////////////////////////////---Login Window---////////////////////////////////////
        loginFrame = new JFrame();
        loginFrame.setTitle("Login");

        //sets the location of the initial window
        loginFrame.setLocation(400, 150);
        //mainFrame.setPreferredSize(new Dimension(250, 175));
        loginFrame.setResizable(false);

        //exits the program when the window is closed
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //gets the main content panel
        JPanel mainLoginPanel = (JPanel) loginFrame.getContentPane();

        //sets the layout of the mainPanel
        mainLoginPanel.setLayout(new BoxLayout(mainLoginPanel, BoxLayout.Y_AXIS));

        //creates and sets an empty border
        Border emptyBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        mainLoginPanel.setBorder(emptyBorder);

        //
        //login welcome panel
        JPanel loginWelcomePanel = new JPanel();
        Border emptyWelcomeBorder = BorderFactory.createEmptyBorder(0, 0, 15, 0);
        loginWelcomePanel.setBorder(emptyWelcomeBorder);
        loginWelcomePanel.add(new JLabel("Please sign in or create a new account to continue."));

        //
        //Assignment type selection
        JPanel loginInput = new JPanel();
        loginInput.setLayout(new GridLayout(2, 1));

        TitledBorder t1 = new TitledBorder("Username");
        t1.setTitleColor(themeCinv);

        loginUsername = new JTextField(15);
        loginUsername.setBorder(t1);
        loginPassword = new JPasswordField(15);
        loginPassword.setBorder(new TitledBorder("Password"));

        loginInput.add(loginUsername);
        loginInput.add(loginPassword);
        //
        //login button panel
        JPanel loginButtonPanel = new JPanel();
        loginButtonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        login = new Button("Login", 55, 30);
        login.addActionListener(this);

        newUser = new Button("New User", 80, 30);
        newUser.addActionListener(this);

        loginButtonPanel.add(newUser);
        loginButtonPanel.add(login);

        //add all the panels in order
        mainLoginPanel.add(loginWelcomePanel);
        mainLoginPanel.add(loginInput);
        mainLoginPanel.add(loginButtonPanel);

        //finish frame
        loginFrame.pack();
        loginFrame.setVisible(true);
//////////////////////////////////---Main User Window---//////////////////////////////////
        mainFrame = new JFrame();
        mainFrame.setTitle("Bank Application");

        //sets the location of the initial window
        mainFrame.setLocation(400, 150);
        mainFrame.setPreferredSize(new Dimension(400, 450));
        mainFrame.setResizable(false);

        //exits the program when the window is closed
        mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //gets the main content panel
        JPanel mainPanel = (JPanel) mainFrame.getContentPane();
        //sets the layout of the mainPanel
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //creates and sets an empty border
        mainPanel.setBorder(emptyBorder);

        //
        // create and update user info panel
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.X_AXIS));
        userInfoPanel.setBorder(new EmptyBorder(0, 0, 15, 0));


        userInfoLabel = new JLabel("Welcome");
        // set up logout button
        logout = new JButton("Logout");
        logout.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent evt) {
                logout.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        logout.setIcon(
                // icon scaling from user 'tirz'
                // via https://stackoverflow.com/questions/16343098/resize-a-picture-to-fit-a-jlabel/16345968
                new ImageIcon(new ImageIcon("icons/logout_icon.png").getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        logout.setHorizontalTextPosition(AbstractButton.LEADING);
        logout.setPreferredSize(new Dimension(82, 40));
        logout.setBorder(new EmptyBorder(0, 0, 0, 0));

        logout.addActionListener(this);

        userInfoPanel.add(userInfoLabel);
        userInfoPanel.add(Box.createHorizontalGlue());
        userInfoPanel.add(logout);

        //
        // display user's account list
        JPanel userAccountsPanel = new JPanel();
        Border accountBorder = new TitledBorder("Accounts");

        accountViewer = new JList<String>(accountsModel);

        accountViewer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        accountViewer.setPreferredSize(new Dimension(325, 200));

        // create scroll view for larger number of accounts
        accountScroll = new JScrollPane(accountViewer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        if (accountsModel.size() <= 8) {
            accountScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }
        // hide scroll bar
        accountScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        accountScroll.setBorder(accountBorder);
        userAccountsPanel.add(accountScroll);


        //
        // create and set up button panel for main frame
        // layout based on GridBagLayout constraints
        JPanel mainButtons = new JPanel();
        mainButtons.setLayout(new GridBagLayout());

        view = new Button("View Account", 55, 28);
        create = new Button("Create", 55, 28);
        delete = new Button("Delete", 55, 28);

        GridBagConstraints c = new GridBagConstraints();

        view.addActionListener(this);
        create.addActionListener(this);
        delete.addActionListener(this);

        c.insets = new Insets(5, 8, 0, 8);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        mainButtons.add(view, c);

        c.gridy = 1;
        c.gridwidth = 1;
        mainButtons.add(create, c);

        c.gridx = 1;
        mainButtons.add(delete, c);

        // set up and instantiate settings button and panel
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.X_AXIS));

        settings = new JButton("");
        settings.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                settings.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent evt) {
                settings.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        settings.setIcon(new ImageIcon(new ImageIcon("icons/settings_icon.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        settings.setPreferredSize(new Dimension(40, 40));
        settings.setBorder(new EmptyBorder(0, 0, 0, 0));
        settings.addActionListener(this);

        settingsPanel.add(Box.createHorizontalGlue());
        settingsPanel.add(settings);

        //add panels to main panel
        mainPanel.add(userInfoPanel);
        mainPanel.add(userAccountsPanel);
        mainPanel.add(mainButtons);
        mainPanel.add(settingsPanel);

        // pack and hide frame
        mainFrame.pack();
        mainFrame.setVisible(false);

//////////////////////////////////---Account View Window---//////////////////////////////////
        accountFrame = new JFrame();
        accountFrame.setTitle("Bank Application");

        //sets the location of the initial window
        accountFrame.setLocation(450, 200);
        accountFrame.setPreferredSize(new Dimension(400, 325));
        accountFrame.setResizable(false);

        //exits the program when the window is closed
        accountFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //gets the main content panel
        JPanel mainAccountPanel = (JPanel) accountFrame.getContentPane();
        //sets the layout of the mainPanel
        mainAccountPanel.setLayout(new BoxLayout(mainAccountPanel, BoxLayout.Y_AXIS));

        //creates and sets an empty border
        mainAccountPanel.setBorder(emptyBorder);

        //
        // create and update account information panel
        JPanel accountInfoPanel = new JPanel();
        accountInfoPanel.setLayout(new GridLayout(2, 2));
        accountInfoLabel = new JLabel("Account Id: ");
        accountBalanceLabel = new JLabel();

        accountInfoPanel.add(accountInfoLabel);
        accountInfoPanel.add(new JLabel());
        accountInfoPanel.add(accountBalanceLabel);
        accountInfoPanel.add(new JLabel());

        // transaction history default list model setup
        JPanel transactionHistoryPanel = new JPanel();
        Border historyBorder = new TitledBorder("Transaction History");

        historyViewer = new JList<String>(historyModel);

        historyViewer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        historyViewer.setPreferredSize(new Dimension(325, 200));

        // create a scroll pane for longer history feed
        JScrollPane historyScroll = new JScrollPane(historyViewer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        if (historyModel.size() <= 8) {
            historyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }
        // hide scroll bar
        historyScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        historyScroll.setBorder(historyBorder);

        transactionHistoryPanel.add(historyScroll);

        //
        // instantiate account interaction buttons
        // and add action listeners
        JPanel accountInteractionPanel = new JPanel();
        deposit = new Button("Deposit", 70, 28);

        withdraw = new Button("Withdraw", 70, 28);

        transfer = new Button("Transfer", 70, 28);

        deposit.addActionListener(this);
        withdraw.addActionListener(this);
        transfer.addActionListener(this);

        accountInteractionPanel.add(deposit);
        accountInteractionPanel.add(withdraw);
        accountInteractionPanel.add(transfer);


        //add panels to main content panel
        mainAccountPanel.add(accountInfoPanel);
        mainAccountPanel.add(transactionHistoryPanel);
        mainAccountPanel.add(accountInteractionPanel);

        // pack and hide frame
        accountFrame.pack();
        accountFrame.setVisible(false);

//////////////////////////////////---New User Window---//////////////////////////////////
        newUserFrame = new JFrame();
        newUserFrame.setTitle("New User");

        //sets the location of the initial window
        newUserFrame.setLocation(400, 150);
        //mainFrame.setPreferredSize(new Dimension(250, 175));
        newUserFrame.setResizable(false);

        //exits the program when the window is closed
        newUserFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //gets the main content panel
        JPanel newUserMainPanel = (JPanel) newUserFrame.getContentPane();

        //sets the layout of the mainPanel
        newUserMainPanel.setLayout(new BoxLayout(newUserMainPanel, BoxLayout.Y_AXIS));

        //creates and sets an empty border
        newUserMainPanel.setBorder(emptyBorder);

        // welcome text and panel
        JPanel newUserWelcomePanel = new JPanel();
        newUserWelcomePanel.add(new JLabel("Enter a username and password to create your account."));

        // layout and instantiate text input for new user creation
        JPanel newUserInput = new JPanel();
        newUserInput.setLayout(new GridLayout(2, 1));

        newUserUsername = new JTextField(15);
        newUserUsername.setBorder(new TitledBorder("Username"));
        newUserPassword = new JPasswordField(15);
        newUserPassword.setBorder(new TitledBorder("Password"));

        newUserInput.add(newUserUsername);
        newUserInput.add(newUserPassword);

        // create and set up create user button and panel
        JPanel newUserButtonPanel = new JPanel();
        createUser = new Button("Create User", 150, 30);
        createUser.addActionListener(this);

        newUserButtonPanel.add(createUser);

        //add panels to main content panel
        newUserMainPanel.add(newUserWelcomePanel);
        newUserMainPanel.add(newUserInput);
        newUserMainPanel.add(newUserButtonPanel);

        // pack and hide frame
        newUserFrame.pack();
        newUserFrame.setVisible(false);

//////////////////////////////////---Settings Window---//////////////////////////////////
        settingsFrame = new JFrame();
        settingsFrame.setTitle("Settings");

        //sets the location of the initial window
        settingsFrame.setLocation(500, 250);
        settingsFrame.setPreferredSize(new Dimension(175, 150));
        settingsFrame.setResizable(false);

        //exits the program when the window is closed
        settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //gets the main content panel
        JPanel settingsMainPanel = (JPanel) settingsFrame.getContentPane();

        //sets the layout of the mainPanel
        GridLayout g = new GridLayout(3, 1);
        g.setVgap(6);
        settingsMainPanel.setLayout(g);

        //creates and sets an empty border
        settingsMainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // instantiate buttons
        changeUsername = new Button("  Change Username  ", 250, 30);
        changePassword = new Button("  Change Password  ", 150, 30);
        deleteAccount = new Button("  Delete Account  ", 150, 30);

        // add action listeners
        changeUsername.addActionListener(this);
        changePassword.addActionListener(this);
        deleteAccount.addActionListener(this);

        // add subpanels to main content frome
        settingsMainPanel.add(changeUsername);
        settingsMainPanel.add(changePassword);
        settingsMainPanel.add(deleteAccount);

        // pack and hide frame
        settingsFrame.pack();
        settingsFrame.setVisible(false);

    }

    // action listener method to perform actions
    public void actionPerformed(ActionEvent e) {

        // store the source of the action in "control"
        Object control = e.getSource();

        // if the login button is pressed, heck if the information was correct
        // if so set up the main frame and user,
        // if not, reset text entry boxes and display an error
        if (control == login) {
            boolean couldLog = loginCheck();
            if (couldLog) {
                loginFrame.setVisible(false);
                userInfoLabel.setText("Welcome, " + username);
                updateAccounts();
                mainFrame.setVisible(true);
                currentAccount = currentUser.userCheckingAccounts.get(0);
                loginUsername.setText("");
                loginPassword.setText("");
            } else {
                loginUsername.setText("");
                loginPassword.setText("");
                JOptionPane.showMessageDialog(null,
                        "The username and/or password you entered were incorrect.",
                        "User Not Found",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        // if the new user button is pressed, show the appropriate frame
        if (control == newUser) {
            newUserFrame.setVisible(true);
        }

        // if create user button is selected, create a new user based on the conditions:
        // the username is not already in use
        // there was a password entered(length > 0)
        if (control == createUser) {
            boolean success = checkCanUseUsername(newUserUsername.getText());
            if (success && newUserUsername.getText().length() > 0) {
                char[] passChars = newUserPassword.getPassword();
                String[] passStrings = new String[passChars.length];
                for (int i = 0; i < passStrings.length; i++) {
                    passStrings[i] = String.valueOf(passChars[i]);
                }
                String newP = String.join("", passStrings);
                if (newP.length() > 0) {
                    User newU = new User(newUserUsername.getText(), newP);
                    newUserFrame.setVisible(false);
                    JOptionPane.showMessageDialog(null,
                            "Account Created." +
                                    "\nUsername: " + newU.getUsername() +
                                    "\nPassword: " + newU.getPassword(),
                            "Account Created",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a valid password.",
                            "Invalid Password",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "That username is already taken.",
                        "Username Taken",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        // if logout button pressed, hide all frames except login
        // and remove current user
        if (control == logout) {
            loginUsername.setText("");
            loginPassword.setText("");
            mainFrame.setVisible(false);
            accountFrame.setVisible(false);
            settingsFrame.setVisible(false);
            currentUser = null;
            loginFrame.setVisible(true);
        }

        // if view is pressed show the appropriate account frame
        // and update the account history
        if (control == view) {
            if (!accountViewer.isSelectionEmpty()) {
                String[] accString = String.valueOf(accountViewer.getSelectedValue()).split(" ");
                int viewAccNum = Integer.parseInt(accString[1]);
                for (int i = 0; i < currentUser.userCheckingAccounts.size(); i++) {
                    if (viewAccNum == currentUser.userCheckingAccounts.get(i).getAccountNumber()) {
                        currentAccount = currentUser.userCheckingAccounts.get(i);
                    }
                }
                updateHistory();
                accountFrame.setTitle(String.valueOf(currentAccount.getAccountNumber()));
                accountInfoLabel.setText("Account ID: " + currentAccount.getAccountNumber());
                accountBalanceLabel.setText("Balance: $" + currentAccount.getAccountBalance());
                accountFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Please select and account",
                        "No account selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        // if control is pressed guide the user through creating a new account
        if (control == create) {
            String[] options = {"Checking Account", "Saving Account", "Cancel"};
            int input = JOptionPane.showOptionDialog(null,
                    "Choose an account type",
                    "Confirm Action", JOptionPane.YES_NO_OPTION,
                    0,
                    null,
                    options,
                    options[2]);

            if (input == 0) {
                Checking newAccount = new Checking();
                currentUser.addAccount(newAccount);
            }
            if (input == 1) {
                Saving newAccount = new Saving();
                currentUser.addAccount(newAccount);
            }
            updateAccounts();
        }

        // if delete is pressed confirm the action,
        // and if the account is empty, complete deletion
        if (control == delete) {
            String[] accString = String.valueOf(accountViewer.getSelectedValue()).split(" ");
            int deleteAccNum = Integer.parseInt(accString[1]);
            int input = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete account " + deleteAccNum + "?",
                    "Confirm Action", JOptionPane.YES_NO_OPTION);
            if (input == 0) {
                if (currentAccount.getAccountNumber() == deleteAccNum) {
                    accountFrame.setVisible(false);
                }
                currentUser.removeAccount(deleteAccNum);
                updateAccounts();
            }
        }

        // show settings frame when button is pressed
        if (control == settings) {
            settingsFrame.setVisible(true);
        }

        // walk the user through depositing money to an account
        if (control == deposit) {
            String m = JOptionPane.showInputDialog("Enter the amount to deposit");
            try {
                double depositAmount = Double.parseDouble(m);
                currentAccount.deposit(depositAmount);
                updateHistory();
                updateAccounts();
                accountBalanceLabel.setText("Balance: $" + currentAccount.getAccountBalance());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid amount.",
                        "Invalid Amount",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        // walk the user through withdrawing money from an account
        if (control == withdraw) {
            String m = JOptionPane.showInputDialog("Enter the amount to withdraw");
            try {
                double withdrawAmount = Double.parseDouble(m);
                currentAccount.withdraw(withdrawAmount);
                updateHistory();
                updateAccounts();
                accountBalanceLabel.setText("Balance: $" + currentAccount.getAccountBalance());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid amount.",
                        "Invalid Amount",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        // walk the user through transferring money to an account
        if (control == transfer) {
            Checking c;
            String m = JOptionPane.showInputDialog("Enter the account number you\nwould like to transfer to");
            try {
                int transferId = Integer.parseInt(m);
                for (int i = 0; i < accounts.size(); i++) {
                    if (transferId == accounts.get(i).getAccountNumber()) {
                        c = accounts.get(i);
                        String n = JOptionPane.showInputDialog("Enter the amount to transfer.");
                        try {
                            double transferAmount = Integer.parseInt(n);
                            currentAccount.transfer(c, transferAmount);
                            break;
                        } catch (Exception d) {
                            JOptionPane.showMessageDialog(null,
                                    "Please enter a valid amount",
                                    "Invalid Amount",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    if (transferId != accounts.get(i).getAccountNumber() && i == accounts.size() - 1) {
                        JOptionPane.showMessageDialog(null,
                                "Account Not Found",
                                "Invalid Id",
                                JOptionPane.WARNING_MESSAGE);
                    }

                }
                updateHistory();
                updateAccounts();
                accountBalanceLabel.setText("Balance: $" + currentAccount.getAccountBalance());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid ID",
                        "Invalid Id",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        // walk the user through changing username
        if (control == changeUsername) {
            String cU = JOptionPane.showInputDialog("Enter a potential new username.");
            if (cU != null && cU.length() > 0) {
                currentUser.changeUsername(cU);
                if (!(username.equals(currentUser.getUsername()))) {
                    username = currentUser.getUsername();
                    userInfoLabel.setText("Welcome, " + username);
                    JOptionPane.showMessageDialog(null, "Account Username successfully changed to " + cU, "Username Changed", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        // walk the user through changing password
        if (control == changePassword) {
            String currentPass = JOptionPane.showInputDialog("Enter a your current password");
            if (currentPass.equals(currentUser.getPassword())) {
                String newPass = JOptionPane.showInputDialog("Enter a new password");
                String confirmPass = JOptionPane.showInputDialog("Confirm Password");
                if (newPass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(null, "Account password successfully changed to " + newPass, "Password Changed", JOptionPane.INFORMATION_MESSAGE);
                    currentUser.changePassword(newPass);
                } else {
                    JOptionPane.showMessageDialog(null, "Passwords did not match.", "Password Error", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Incorrect Password.",
                        "Password Error",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // delete the user account if all accounts are emptied
        if (control == deleteAccount) {
            int confirmDelete = JOptionPane.showConfirmDialog(null,
                    "Confirm ",
                    "Confirm Action",
                    JOptionPane.OK_CANCEL_OPTION);
            if (confirmDelete == 0) {
                deleteUser(currentUser.getUsername());
            }
        }

        //update data after every action,
        // rather than worrying about calling after appropriate actions
        writeToDataFile();
    }

    // pull username and login from text boxes
    // and find if the username exists, if so
    // then check if the password entered is correct for the user
    // if successful, return true; if not, return false
    public boolean loginCheck() {
        username = loginUsername.getText();
        char[] passChars = loginPassword.getPassword();
        String[] passStrings = new String[passChars.length];
        for (int i = 0; i < passStrings.length; i++) {
            passStrings[i] = String.valueOf(passChars[i]);
        }
        password = String.join("", passStrings);

        for (int i = 0; i < users.size(); i++) {
            if (username.equals(users.get(i).getUsername())) {
                if (password.equals(users.get(i).getPassword())) {
                    currentUser = users.get(i);
                    return true;
                }
            }

        }
        return false;
    }

    // check if the username passed in is already in use
    public static boolean checkCanUseUsername(String u) {
        for (int i = 0; i < users.size(); i++) {
            if (u.equals(users.get(i).getUsername())) {
                return false;
            }
        }
        return true;
    }

    // write current data from accounts and users
    // to their respective data file
    // in an easily parsable format for reading in
    public void writeToDataFile() {
        Out out = new Out("accountData.txt");
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i) instanceof Saving) {
                Saving c = (Saving) accounts.get(i);
                String toWrite = c.getAccountNumber() + "," + c.getAccountBalance() +
                        "," + c.getTransactionHistory() + ",s," + c.getLastMonthChecked() +
                        "," + c.getNumOfTransactions();
                out.println(toWrite);
            } else {
                Checking c = accounts.get(i);
                String toWrite = c.getAccountNumber() + "," + c.getAccountBalance() +
                        "," + c.getTransactionHistory() + ",c";
                out.println(toWrite);
            }
        }
        Out userOut = new Out("userData.txt");
        for (int i = 0; i < users.size(); i++) {
            String toWriteUser = users.get(i).getUsername() + "," + users.get(i).getPassword() +
                    "," + users.get(i).accountIdsToStore;
            userOut.println(toWriteUser);
        }
    }

    // clear and repopulate the Default List Model
    // that store user account information
    // and refresh the JList that displays it
    public void updateAccounts() {
        accountsModel.clear();
        for (int i = 0; i < currentUser.userCheckingAccounts.size(); i++) {
            if (currentUser.userCheckingAccounts.get(i) instanceof Saving) {
                accountsModel.addElement("Saving: " + currentUser.userCheckingAccounts.get(i).getAccountNumber()
                        + "  -  $" + currentUser.userCheckingAccounts.get(i).getAccountBalance());
            } else {
                accountsModel.addElement("Checking: " + currentUser.userCheckingAccounts.get(i).getAccountNumber()
                        + "  -  $" + currentUser.userCheckingAccounts.get(i).getAccountBalance());
            }
        }
        accountScroll.updateUI();
    }

    // clear and repopulate the Default List Model
    // that store account transaction history
    // and refresh the JList that displays it
    public void updateHistory() {
        historyModel.clear();
        String[] h = currentAccount.getTransactionHistory().split("@");
        for (int i = 0; i < h.length; i++) {
            historyModel.addElement(h[i]);
        }
        historyViewer.updateUI();
    }

    // used to theme all elements of the UI(potential theming option)
    // first parameter passed in is used as panel backround color
    // second parameter is for text and contrasting UI elements
    public void updateTheme(Color themeC, Color themeCinv) {
        UIManager UI = new UIManager();
        UI.put("OptionPane.background", new ColorUIResource(themeC));
        UI.put("OptionPane.messageForeground", new ColorUIResource(themeCinv));
        UI.put("Panel.background", new ColorUIResource(themeC));
        UI.put("Label.foreground", new ColorUIResource(themeCinv));
        UI.put("TitledBorder.titleColor", new ColorUIResource(themeCinv));
        UI.put("ScrollPane.background", new ColorUIResource(themeC));
        UI.put("List.foreground", new ColorUIResource(themeCinv));
        UI.put("List.background", new ColorUIResource(themeC));
        UI.put("Button.foreground", new ColorUIResource(themeCinv));
        UI.put("TextField.background", new ColorUIResource(themeC));
        UI.put("TextField.foreground", new ColorUIResource(themeCinv));
        UI.put("PasswordField.background", new ColorUIResource(themeC));
        UI.put("PasswordField.foreground", new ColorUIResource(themeCinv));
    }

    // sets the fond of all UI elements(credit linked in method)
    public void setUIFont(FontUIResource f) {
        // font change method from Kumar Mitra
        // via: https://stackoverflow.com/questions/12730230/set-the-same-font-for-all-component-java
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                FontUIResource orig = (FontUIResource) value;
                Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
                UIManager.put(key, new FontUIResource(font));
            }
        }
    }

    // used to iterate through users ArrayList
    // and delete user if all accounts are empty
    public void deleteUser(String username) {
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            // find the user based on username
            if (u.getUsername().equals(username)) {
                for (int j = 0; j < u.userCheckingAccounts.size(); j++) {
                    // check that all accounts are empty
                    if (u.userCheckingAccounts.get(j).getAccountBalance() != 0) {
                        JOptionPane.showMessageDialog(null,
                                "Please empty all accounts before deleting user account",
                                "Account Error",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
                mainFrame.setVisible(false);
                loginFrame.setVisible(true);
                settingsFrame.setVisible(false);
                accountFrame.setVisible(false);

                for (int j = 0; j < u.userCheckingAccounts.size(); j++) {
                    u.removeAccount(u.userCheckingAccounts.get(i).getAccountNumber());
                }

                // remove the user
                users.remove(i);
                // display confirmation
                JOptionPane.showMessageDialog(null,
                        "User successfully deleted",
                        "User Deleted",
                        JOptionPane.INFORMATION_MESSAGE);
                return;


            }
        }
    }

    public static void main(String[] args) {

        // populate accounts ArrayList from accountData.txt
        In accountsIn = new In("accountData.txt");
        String[] storedAccountData = accountsIn.readAllLines();
        for (int i = 0; i < storedAccountData.length; i++) {
            String[] tempNewAccount = storedAccountData[i].split(",");
            // store to accounts List
            // checks if account needs to be a checking or saving
            if (tempNewAccount[3].equals("c")) {
                new Checking(Integer.parseInt(tempNewAccount[0]),
                        Double.parseDouble(tempNewAccount[1]),
                        tempNewAccount[2]);
            } else if (tempNewAccount[3].equals("s")) {
                new Saving(Integer.parseInt(tempNewAccount[0]),
                        Double.parseDouble(tempNewAccount[1]),
                        tempNewAccount[2],
                        Integer.parseInt(tempNewAccount[4]),
                        Integer.parseInt(tempNewAccount[5]));
            }
        }

        // populate users ArrayList from userData.txt
        In userIn = new In("userData.txt");
        String[] storedUserData = userIn.readAllLines();
        for (int i = 0; i < storedUserData.length; i++) {
            String[] tempNewUser = storedUserData[i].split(",");
            // store to userData
            new User(tempNewUser[0], tempNewUser[1], tempNewUser[2]);
        }


        // calls the constructor to run the program
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ATM();
            }
        });

    }
}