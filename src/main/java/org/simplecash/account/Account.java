package org.simplecash.account;

import jakarta.persistence.*;
import org.simplecash.client.Client;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "accounts")
public class Account {

    public static final BigDecimal DEFAULT_OVERDRAFT_LIMIT = BigDecimal.valueOf(1000);
    public static final BigDecimal DEFAULT_INTEREST_RATE = BigDecimal.valueOf(0.03);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    @Column(precision = 19, scale = 2)
    private BigDecimal balance;

    private LocalDate openedAt;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column(precision = 19, scale = 2)
    private BigDecimal overdraftLimit;

    @Column(precision = 5, scale = 4)
    private BigDecimal interestRate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    protected Account() {
    }

    public Account(String accountNumber, AccountType type, Client client) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.client = client;
        this.openedAt = LocalDate.now();
        this.balance = BigDecimal.ZERO;
        if (type == AccountType.CURRENT) {
            this.overdraftLimit = DEFAULT_OVERDRAFT_LIMIT;
        } else if (type == AccountType.SAVINGS) {
            this.interestRate = DEFAULT_INTEREST_RATE;
        }
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDate getOpenedAt() {
        return openedAt;
    }

    public AccountType getType() {
        return type;
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public Client getClient() {
        return client;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
