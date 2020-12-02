
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Alexandru Muresan
 **/

public class Checking {

    // critical account info(id, balance, history)
    private final int accountNumber;
    private double accountBalance = 0.0;
    protected String transactionHistory = "";

    // create an empty Checking account(via user input)
    public Checking() {
        int randNum = (int) (Math.random() * 9999) + 1;
        while (!checkAccountNumber(randNum)) {
            randNum = (int) (Math.random() * 9999) + 1;
        }
        accountNumber = randNum;
        transactionHistory += new SimpleDateFormat("dd-MM-yyyy : HH:mm").format(new Date()) + " - Account created";
        ATM.accounts.add(this);
    }

    // create an empty Checking Account via file parsing
    public Checking(int accountNum, double amount, String history) {
        accountNumber = accountNum;
        accountBalance = amount;
        transactionHistory = history;
        ATM.accounts.add(this);
    }

    // check that the account id passed in does not already exist
    private boolean checkAccountNumber(int num) {
        for (int i = 0; i < ATM.accounts.size(); i++) {
            if (ATM.accounts.get(i).getAccountNumber() == num) {
                return false;
            }
        }
        return true;
    }

    // self-evident get() methods
    public int getAccountNumber() {
        return accountNumber;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public String getTransactionHistory() {
        return transactionHistory;
    }

    // deposit the amount, and update transaction history
    public void deposit(double amount) {
        accountBalance += amount;

        transactionHistory += ("@" + new SimpleDateFormat("dd-MM-yyyy : HH:mm").format(new Date()) + " - deposited $" + amount);
    }

    // check if account has enough money
    // if so, withdraw money and update history
    public void withdraw(double amount) {
        if (accountBalance >= amount) {
            accountBalance -= amount;

            transactionHistory += ("@" + new SimpleDateFormat("dd-MM-yyyy : HH:mm").format(new Date()) + " - withdrew $" + amount);
        } else {
            JOptionPane.showMessageDialog(null,
                    "You do not have enough money.",
                    "Not Enough Money",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // transfer money to to the specified account
    // (receiving account verified in ATM.actionPerformed)
    public void transfer(Checking c, double amount) {
        withdraw(amount);
        c.deposit(amount);
        c.transactionHistory += ("@" + new SimpleDateFormat("dd-MM-yyyy : HH:mm").format(new Date()) + " - received $" + amount + " from " + getAccountNumber());
        transactionHistory += ("@" + new SimpleDateFormat("dd-MM-yyyy : HH:mm").format(new Date()) + " - transferred $" + amount + " to " + c.getAccountNumber());
    }

}
