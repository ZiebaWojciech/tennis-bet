package pl.coderslab.tennis_bet.sport_events_data.entity;

import lombok.Getter;
import lombok.Setter;
import pl.coderslab.tennis_bet.betting_site.entity.TennisMatch;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Result {
    @Id
    private int id;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<TennisSet> sets = new ArrayList<>();
    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private TennisMatch tennisMatch;
    @ManyToOne
    private Player winner;
    @ManyToOne
    private Player looser;
}

