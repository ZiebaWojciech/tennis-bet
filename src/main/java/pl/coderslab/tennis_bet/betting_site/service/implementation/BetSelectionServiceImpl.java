package pl.coderslab.tennis_bet.betting_site.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.tennis_bet.betting_site.entity.BetSelection;
import pl.coderslab.tennis_bet.betting_site.entity.BetTicket;
import pl.coderslab.tennis_bet.betting_site.entity.enumUtil.BetSelectionStatus;
import pl.coderslab.tennis_bet.betting_site.entity.enumUtil.BetSelectionType;
import pl.coderslab.tennis_bet.betting_site.repository.BetSelectionRepository;
import pl.coderslab.tennis_bet.betting_site.service.BetSelectionService;
import pl.coderslab.tennis_bet.betting_site.entity.TennisMatch;
import pl.coderslab.tennis_bet.betting_site.service.TennisMatchService;
import pl.coderslab.tennis_bet.sport_events_data.entity.enumUtil.EventStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class BetSelectionServiceImpl implements BetSelectionService {
    @Autowired
    BetSelectionRepository betSelectionRepository;
    @Autowired
    TennisMatchService tennisMatchService;


    @Override
    public BetTicket addNewBetSelectionToBetTicket(BetTicket betTicket, String betSelectionType, BigDecimal odd, TennisMatch tennisMatch) {
        BetSelection betSelection = new BetSelection();
        betSelection.setTemporalId(UUID.randomUUID());
        betSelection.setBetSelectionStatus(BetSelectionStatus.PENDING);
        betSelection.setTennisMatch(tennisMatch);
        betSelection.setOdd(odd);
        betSelection.setBetTicket(betTicket);
        switch (betSelectionType) {
            case "PlayerOne":
                betSelection.setBetSelectionType(BetSelectionType.PLAYER_ONE_WINS);
                break;
            case "PlayerTwo":
                betSelection.setBetSelectionType(BetSelectionType.PLAYER_TWO_WINS);
                break;
        }
        betTicket.addBetSelection(betSelection);
        return betTicket;
    }

    @Override
    public boolean isBetUniqueForEvent(BetTicket betTicket, TennisMatch tennisMatch) {
        return betTicket.getBetSelections().stream().noneMatch(v -> v.getTennisMatch().getId() == tennisMatch.getId());
    }

    @Override
    public void resolveBetAfterEventStatusChange(BetSelection betSelection) {
        TennisMatch tennisMatch = betSelection.getTennisMatch();
        if(tennisMatch.getStatus() == EventStatus.CANCELLED){
            betSelection.setBetSelectionStatus(BetSelectionStatus.VOID);
        } else if(tennisMatch.getStatus() == EventStatus.COMPLETED){
            if(tennisMatch.get)
            betSelection.setBetSelectionStatus(BetSelectionStatus.SUBMITTED);

    }
    }
}
