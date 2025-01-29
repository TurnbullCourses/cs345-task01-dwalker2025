package edu.ithaca.dturnbull.bank;

import java.util.Arrays;

public class BankAccount {

    private String email;
    private double balance;

    /**
     * @throws IllegalArgumentException if email is invalid
     */
    public BankAccount(String email, double startingBalance) {
        if (isEmailValid(email)) {
            this.email = email;
            this.balance = startingBalance;
        } else {
            throw new IllegalArgumentException("Email address: " + email + " is invalid, cannot create account");
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
    }

    /**
     * @post reduces the balance by amount if amount is non-negative and smaller
     *       than balance
     */
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount < 0) {
            throw new IllegalArgumentException("Withdrawal amount cannot be negative");
        }
        String numStr = String.valueOf(amount);
        int decimalIndex = numStr.indexOf('.');

        if (numStr.length() - decimalIndex - 1 > 2) {
            throw new InsufficientFundsException("Has to be a valid dollar amount!");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Not enough money");
        }
        balance -= amount;
        System.out.println("You withdrew $" + amount + ". Your balance is " + balance + ".");
    }

    public void deposit(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount cannot be negative or zero.");
        }
        String numStr = String.valueOf(amount);
        int decimalIndex = numStr.indexOf('.');

        if (numStr.length() - decimalIndex - 1 > 2) {
            throw new InsufficientFundsException("Has to be a valid dollar amount!");
        }
        balance += amount;
        System.out.println("You deposited $" + amount + ". Your balance is " + balance + ".");
    }

    public void transfer(BankAccount sender_account, BankAccount recipient_account, double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount cannot be negative or zero");
        }
        if (sender_account.equals(recipient_account)) {
            throw new IllegalArgumentException("Can't deposit into same account.");
        }
        String numStr = String.valueOf(amount);
        int decimalIndex = numStr.indexOf('.');

        if (numStr.length() - decimalIndex - 1 > 2) {
            throw new IllegalArgumentException("Has to be a valid dollar amount!");
        }

        if (sender_account.balance < amount) {
            throw new InsufficientFundsException("Not enough money in your account to transfer.");
        }

        sender_account.balance -= amount;
        recipient_account.balance += amount;


    }



    public static boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        // Split email into prefix and domain
        String[] emailParts = email.split("@");

        if (email.indexOf('@') == -1 || emailParts.length != 2) {
            return false;
        }

        // Remove empty parts (in case of malformed input like "@example.com")
        emailParts = Arrays.stream(emailParts)
                .filter(part -> !part.isEmpty())
                .toArray(String[]::new);

        if (emailParts.length != 2) { // If there is more than one @ symbol, invalid email address
            return false;
        }

        String prefix = emailParts[0]; // Get prefix part of email
        String domain = emailParts[1]; // Get domain part of email

        return isPrefixValid(prefix) && isDomainValid(domain);
    }

    private static boolean isPrefixValid(String prefix) {
        if (prefix.isEmpty()) {
            return false;
        }

        // Invalid characters in the prefix
        String invalidPrefixChars = "(),:;<>@[\\]\" ";

        for (char c : prefix.toCharArray()) {
            if (invalidPrefixChars.indexOf(c) != -1) { // Check if character is in the invalid set
                return false;
            }
        }

        // If prefix starts or ends with a special character
        if (prefix.startsWith(".") || prefix.startsWith("-") || prefix.startsWith("_") ||
                prefix.endsWith(".") || prefix.endsWith("-") || prefix.endsWith("_")) {
            return false;
        }

        // Check if there are any consecutive special characters
        for (int i = 0; i < prefix.length() - 1; i++) {
            char current = prefix.charAt(i);
            char next = prefix.charAt(i + 1);
            if ((current == '.' || current == '-' || current == '_') &&
                    (next == '.' || next == '-' || next == '_')) {
                return false;
            }
        }

        return true;
    }

    private static boolean isDomainValid(String domain) {
        if (domain.isEmpty()) {
            return false;
        }

        // Invalid characters in the domain
        String invalidDomainChars = "(),:;<>@[]\\\" ";

        for (char c : domain.toCharArray()) {
            if (invalidDomainChars.indexOf(c) != -1) { // Check if character is in the invalid set
                return false;
            }
        }

        // Check if domain contains a period and is correctly formatted
        int lastPeriodIndex = domain.lastIndexOf('.');
        if (lastPeriodIndex == -1 || lastPeriodIndex == domain.length() - 1 || lastPeriodIndex == 0) {
            return false;
        }

        // Split domain into parts
        String[] domainParts = domain.split("\\.");
        String topLevelDomain = domainParts[domainParts.length - 1];

        // Check that the top-level domain is at least 2 characters long
        if (topLevelDomain.length() < 2) {
            return false;
        }

        // Ensure top-level domain contains only letters
        for (char c : topLevelDomain.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        // Check all subdomain parts
        for (int i = 0; i < domainParts.length - 1; i++) {
            String subDomain = domainParts[i];

            for (char c : subDomain.toCharArray()) {
                if (!Character.isLetterOrDigit(c) && c != '-') {
                    return false;
                }
            }

            // Subdomains should not start or end with a dash
            if (subDomain.startsWith("-") || subDomain.endsWith("-")) {
                return false;
            }
        }

        return true;
    }
}