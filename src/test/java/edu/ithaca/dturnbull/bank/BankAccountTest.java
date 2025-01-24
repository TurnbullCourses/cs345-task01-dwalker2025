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
    void withdrawTest() throws InsufficientFundsException{
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        bankAccount.withdraw(100);

        assertEquals(100, bankAccount.getBalance(), 0.001);
        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(300));
    }

    @Test
    void isEmailValidTest(){
        //prefix tests
        assertTrue(BankAccount.isEmailValid( "a@b.com"));   // valid email address
        assertFalse( BankAccount.isEmailValid(""));         // empty string
        assertFalse(BankAccount.isEmailValid("abc..def@mail.com")); // consecutive special characters
        assertFalse(BankAccount.isEmailValid(".abc@mail.com"));  // starting email with punctuation
        assertFalse(BankAccount.isEmailValid("abc#def@mail.com")); // no hashtags

        //domain tests
        assertTrue(BankAccount.isEmailValid( "abc.def@mail.cc")); // valid email
        assertFalse(BankAccount.isEmailValid("abc.def@mail.c")); // last portion of domain must be atleast 2 characters
        assertFalse(BankAccount.isEmailValid("abc.def@mail#archive.com")); // no hashtags
        assertFalse(BankAccount.isEmailValid("abc.def@mail")); // no . + atleast 2 characters at end
        assertFalse(BankAccount.isEmailValid("abc.def@mail..com")); //  consecutive special characters


        
    }

    @Test
    void constructorTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance(), 0.001);
        //check for exception thrown correctly
        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("", 100));
    }

}