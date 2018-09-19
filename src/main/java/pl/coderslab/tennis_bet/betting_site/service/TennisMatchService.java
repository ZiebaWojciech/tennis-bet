package pl.coderslab.tennis_bet.betting_site.service;

import org.hibernate.context.TenantIdentifierMismatchException;
import pl.coderslab.tennis_bet.betting_site.entity.TennisMatch;

import java.util.List;

public interface TennisMatchService {
    TennisMatch getOne(int id);
    List<TennisMatch> getAll();
    List<TennisMatch> getUpcomingTennisMatches();

    TennisMatch save(TennisMatch tennisMatch);

    boolean isEventStatusChanged(TennisMatch updatedTennisMatch);
}
