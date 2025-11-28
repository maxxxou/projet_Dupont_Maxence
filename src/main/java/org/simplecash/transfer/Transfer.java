package org.simplecash.transfer;

import jakarta.persistence.*;
import org.simplecash.account.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    private String label;

    private LocalDateTime executedAt;

    protected Transfer() {
    }

    public Transfer(Account fromAccount, Account toAccount, BigDecimal amount, String label) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.label = label;
        this.executedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getLabel() {
        return label;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }
}
