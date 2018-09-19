package pl.coderslab.tennis_bet.sport_events_data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.tennis_bet.betting_site.entity.TennisMatch;
import pl.coderslab.tennis_bet.sport_events_data.entity.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer> {
    Result getByTennisMatch(TennisMatch tennisMatch);
}