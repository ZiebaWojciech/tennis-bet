package pl.coderslab.tennis_bet.sportDataFeed.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class TennisGame {
    @Id
    private int id;

    private boolean inPlay;

    private int playerOnePoints;
    private int playerTwoPoints;

    @ManyToOne
    private Player tennisGameWinner;
}