package com.groupnine.banku;


public abstract class Account {
    private String accountNumber;
    private String accountName;
    private Double availableBalance;
    private Double interestRate;

    public Account(String accountNumber, String accountName, Double availableBalance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.availableBalance = availableBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountBalance() {
        return availableBalance;
    }

    public void setAccountBalance(Double balance) {
        this.availableBalance = availableBalance;
    }

    public void updateInterestRateTo(Double interestRate){

    }
    public Double getInterestRate(){

    }

}
