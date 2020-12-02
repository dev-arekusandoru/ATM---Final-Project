import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Alexandru Muresan
 **/

// Saving is a subclass of Checking that puts a limit
// of 6 transactions per month on the account
public class Saving extends Checking {

    // stores the current number of transactions for the month
    private int numOfTransactions = 0;
    private static final int maxNumOfTransaction = 6;

    // the last month that a transaction was processed,
    // updated every transaction to reset number of transactions appropriately
    private int lastMonthChecked;

    // constructors including lastMonthChecked and numOfTransactions
    public Saving() {
        super();
        lastMonthChecked = Integer.parseInt(super.transactionHistory.substring(3, 5));
    }

    public Saving(int accountNum, double amount, String history, int lmc, int not) {
        super(accountNum, amount, history);
        lastMonthChecked = lmc;
        numOfTransactions = not;
    }

    // deposit if transaction limit has not been reached
    public void deposit(double amount) {
        resetNumOfTransactions();
        if (numOfTransactions < maxNumOfTransaction) {
            super.deposit(amount);
            numOfTransactions++;
        } else {
            JOptionPane.showMessageDialog(null,
                    "You have reached your transaction limit for the month.",
                    "Transaction Overflow",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // transfer if transaction limit has not been reached
    public void transfer(Checking c, double amount) {
        resetNumOfTransactions();
        if (numOfTransactions < maxNumOfTransaction) {
            super.transfer(c, amount);
            numOfTransactions++;
        } else {
            JOptionPane.showMessageDialog(null,
                    "You have reached your transaction limit for the month.",
                    "Transaction Overflow",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // withdraw if transaction limit has not been reached
    public void withdraw(double amount) {
        resetNumOfTransactions();
        if (numOfTransactions < maxNumOfTransaction) {
            super.withdraw(amount);
            numOfTransactions++;
        } else {
            JOptionPane.showMessageDialog(null,
                    "You have reached your transaction limit for the month.",
                    "Transaction Overflow",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // self-evident get methods
    public int getNumOfTransactions() {
        return numOfTransactions;
    }

    ;

    public int getLastMonthChecked() {
        return lastMonthChecked;
    }

    // check if the current month is different than
    // the last month checked, and reset numOfTransactions appropriately
    public void resetNumOfTransactions() {
        int currentMonth = Integer.parseInt(new SimpleDateFormat("dd-MM-yyyy : HH:mm").format(new Date()).substring(3, 5));

        if ((currentMonth - lastMonthChecked) >= 1) {
            lastMonthChecked = currentMonth;
            numOfTransactions = 0;
        }
    }
}
