import javax.swing.*;
import java.util.ArrayList;

/**
 * @author: Alexandru Muresan
 **/

public class User {

    // store user credentials
    private String username;
    private String password;

    // store accounts as a List, and also a string of ids
    // to be parsed when instantiating User object
    public ArrayList<Checking> userCheckingAccounts = new ArrayList<>();
    public String accountIdsToStore = " ";

    // stores current account
    public Checking currentAccount = null;

    // create an empty User(via user input)
    public User(String u, String p) {
        username = u;
        password = p;
        ATM.users.add(this);
    }

    //create a User via file parsing
    public User(String u, String p, String s) {
        username = u;
        password = p;
        accountIdsToStore = s;
        if (accountIdsToStore.length() >= 4) {
            populateUserAccounts();
        }
        ATM.users.add(this);
    }

    // parse accountIdsToStore and find correlating
    // accounts from file, then populate userCheckingAccounts accordingly
    private void populateUserAccounts() {
        if (accountIdsToStore.charAt(0) == ' ') {
            accountIdsToStore = accountIdsToStore.substring(1);
        }
        if (accountIdsToStore.length() > 1) {
            String[] ids = accountIdsToStore.split("l");
            for (int k = 0; k < ids.length; k++) {
                for (int i = 0; i < ATM.accounts.size(); i++) {
                    if (ids[k].equals(String.valueOf(ATM.accounts.get(i).getAccountNumber()))) {
                        userCheckingAccounts.add(ATM.accounts.get(i));
                    }
                }
            }
        }
        if (userCheckingAccounts.size() >= 1) {
            currentAccount = userCheckingAccounts.get(0);
        } else {
            StdOut.println("No matching accounts were found... erasing log");
            accountIdsToStore = " ";
        }
    }

    // self-evident get() methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // change username if input is not already taken
    public void changeUsername(String nU) {
        boolean canUse = ATM.checkCanUseUsername(nU);
        String input = "";
        if (canUse) {
            username = nU;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Username", "That username is already taken.", JOptionPane.WARNING_MESSAGE);
        }
    }

    // change password to value passed in(verified in ATM.actionPerformed())
    public void changePassword(String newPass) {
        password = newPass;
    }

    // add a new account to the userCheckingAccounts
    // as well as updating accountIdsToStore
    public void addAccount(Checking c) {
        userCheckingAccounts.add(c);
        accountIdsToStore += String.valueOf(c.getAccountNumber()) + "l";
        if (userCheckingAccounts.size() == 1) {
            currentAccount = userCheckingAccounts.get(0);
        }
    }

    // if an account with the passed in id exists under the user,
    // check that the account is empty, and if it is delete the account
    public void removeAccount(int id) {
        Checking c;
        String[] ids = accountIdsToStore.split("l");

        for (int i = 0; i < userCheckingAccounts.size(); i++) {
            c = userCheckingAccounts.get(i);
            if ((c.getAccountNumber() == id)) {
                if (c.getAccountBalance() == 0) {
                    for (int j = 0; j < ATM.accounts.size(); j++) {
                        if (ATM.accounts.get(j).getAccountNumber() == id) {
                            ATM.accounts.remove(j);
                        }
                    }
                    userCheckingAccounts.remove(i);
                    ids[i] = "";
                    String newIds = String.join("l", ids);
                    accountIdsToStore = newIds;
                    return;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Transfer or withdraw all money before closing account.",
                            "Account Balance Error",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

}
