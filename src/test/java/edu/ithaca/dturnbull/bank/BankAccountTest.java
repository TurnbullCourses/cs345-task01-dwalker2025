package edu.ithaca.dturnbull.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void getBalanceTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals(200, bankAccount.getBalance(), 0.001);
    }

    @Test
    void withdrawTest() throws InsufficientFundsException {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        bankAccount.withdraw(100);

        assertEquals(100, bankAccount.getBalance(), 0.001);
        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(300));
    }

    @Test
    void isEmailValidTest() {
        // prefix tests
        assertTrue(BankAccount.isEmailValid("a@b.com")); // valid email address
        assertFalse(BankAccount.isEmailValid("")); // empty string
        assertFalse(BankAccount.isEmailValid("abc..def@mail.com")); // consecutive special characters
        // JR: Equivalence class, multiple consecutive special characters. Border case,
        // 2 consecutive special characters.
        assertFalse(BankAccount.isEmailValid(".abc@mail.com")); // starting email with punctuation
        // JR: Equivalence class, starting or ending email with special character.
        // Border case, starting email with a special character.
        assertFalse(BankAccount.isEmailValid("abc#def@mail.com")); // no hashtags
        // JR: Equivalence class, email with invalid special characters. Border case,
        // email with a hashtag.

        // domain tests
        assertTrue(BankAccount.isEmailValid("abc.def@mail.cc")); // valid email
        assertFalse(BankAccount.isEmailValid("abc.def@mail.c")); // last portion of domain must be atleast 2 characters
        // JR: Equivalence class, domain with less than 2 characters. Border case,
        // domain with 1 character.
        assertFalse(BankAccount.isEmailValid("abc.def@mail#archive.com")); // no hashtags
        // JR: Equivalence class, domain with invalid special characters. Border case,
        // domain with a hashtag.
        assertFalse(BankAccount.isEmailValid("abc.def@mail")); // no . + atleast 2 characters at end
        // JR: Equivalence class, domain without a period. Border case, domain without a
        // period
        assertFalse(BankAccount.isEmailValid("abc.def@mail..com")); // consecutive special characters
        // JR: Equivalence class, domain with multiple periods. Border case, domain with
        // 2 periods.

        // JR: These tests are good, but I would recommend adding a few more:
        // - Test for an email address's prefix with a special character at the end
        // - Test for an email address without an "@" symbol
        // - Test for an email address with multiple "@" symbols
        // - Test for an email address with an empty prefix
        // - Test for an email address with an empty domain
        // - Test the email adress's domain for dashes at the beginning or end and
        // multiple consecutive dashes

    }

    @Test
    void constructorTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance(), 0.001);
        // check for exception thrown correctly
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("", 100));
    }

}