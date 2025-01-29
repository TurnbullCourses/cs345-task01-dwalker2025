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

    public static boolean isEmailValid(String email) {

        String[] emailParts = email.split("@"); // split at @ symbol to get seperate prefix and domain
        if (email.indexOf('@') == -1 || emailParts.length != 2){
            return false;
        }
        emailParts = Arrays.stream(emailParts)
                .filter(part -> !part.isEmpty())
                .toArray(String[]::new);
        if (emailParts.length != 2) { // if there is more than one @ symbol invalid email address
            return false;
        }
        String prefix = emailParts[0]; // get prefix part of email address
        String domain = emailParts[1]; // get domain part of email address

        // validate prefix
        if (!isPrefixValid(prefix)) {
            return false;
        }

        // validate domain
        if (!isDomainValid(domain)) { // if domain is invalid
            return false;
        }

        return true;
    }

    private static boolean isPrefixValid(String prefix) {
        // if prefix is empty
        if (prefix.isEmpty()) {
            return false;
        }
        String invalidPrefixChrs = "(),:;<>@[\\]";
        for (int i = 0; i < prefix.length() - 1; i++){
            char current_pre = prefix.charAt(i);  // current chr in prefix
            for (int j = 0; i < invalidPrefixChrs.length() - 1; i++){
                char current_inv = prefix.charAt(j); // current chr in invalid chr string
                if (current_pre == current_inv ){
                    return false;
                }
            }
        }
        // if prefix starts or ends with a special character
        if (prefix.startsWith(".") || prefix.startsWith("-") || prefix.startsWith("_") ||
                prefix.endsWith(".") || prefix.endsWith("-") || prefix.endsWith("_")) {
            return false;
        }

        // check if all charcters are valid
        for (char c : prefix.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '.' && c != '-' && c != '_') {
                return false;
            }
        }

        // if there are any consecutive special characters
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
        // if domain is empty
        if (domain.length() == 0) {
            return false;
        }

        // check if domain contains a period
        int lastPeriodIndex = domain.lastIndexOf('.');
        if (lastPeriodIndex == -1 || lastPeriodIndex == domain.length() - 1 || lastPeriodIndex == 0) {
            return false;
        }
        String invalidDomainChars = "(),:;<>@[]\\\" ";
        for (int i = 0; i < domain.length() - 1; i++){
            char current_dom = domain.charAt(i);  // current chr in domain
            for (int j = 0; i < invalidDomainChars.length() - 1; i++){
                char domain_inv = domain.charAt(j); // current chr in invalid chr string
                if (current_dom == domain_inv ){
                    return false;
                }
            }
        }

        // creates separate domain parts
        String[] domainParts = domain.split("\\.");
        // if there are more than 2 parts
        if (domainParts.length != 2) {
            return false;
        }
        String subDomain = domainParts[0];
        String topLevelDomain = domainParts[1];

        // check that the top level domain is valid
        if (topLevelDomain.length() < 2) {
            return false;
        }
        for (char c : topLevelDomain.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        // check if all charcters are valid in subdomain
        for (char c : subDomain.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '-') {
                return false;
            }
        }

        // check that subdomain does not start or end with a dash
        if (subDomain.startsWith("-") || subDomain.endsWith("-")) {
            return false;
        }

        // if there are any consecutive dashes in subdomain
        for (int i = 0; i < subDomain.length() - 1; i++) {
            char current = subDomain.charAt(i);
            char next = subDomain.charAt(i + 1);
            if ((current == '-') && (next == '-')) {
                return false;
            }

        }

        return true;
    }
}