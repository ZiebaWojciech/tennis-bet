package pl.coderslab.tennis_bet.betting_site.entity;

import lombok.Getter;
import lombok.Setter;
import pl.coderslab.tennis_bet.betting_site.entity.enumUtil.TransactionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MoneyTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Wallet wallet;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private LocalDateTime time;
    private BigDecimal value;

    public MoneyTransaction() {
    }


    public MoneyTransaction(TransactionType transactionType, LocalDateTime time, BigDecimal value, Wallet wallet) {
        this.transactionType = transactionType;
        this.time = time;
        this.value = value;
        this.wallet = wallet;
    }
}
