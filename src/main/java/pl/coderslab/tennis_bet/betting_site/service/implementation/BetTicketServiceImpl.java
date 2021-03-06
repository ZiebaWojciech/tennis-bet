package pl.coderslab.tennis_bet.betting_site.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.tennis_bet.betting_site.entity.BetSelection;
import pl.coderslab.tennis_bet.betting_site.entity.BetTicket;
import pl.coderslab.tennis_bet.betting_site.entity.Odd;
import pl.coderslab.tennis_bet.betting_site.entity.User;
import pl.coderslab.tennis_bet.betting_site.entity.enumUtil.BetSelectionResult;
import pl.coderslab.tennis_bet.betting_site.entity.enumUtil.BetSelectionStatus;
import pl.coderslab.tennis_bet.betting_site.entity.enumUtil.BetTicketResult;
import pl.coderslab.tennis_bet.betting_site.entity.enumUtil.BetTicketStatus;
import pl.coderslab.tennis_bet.betting_site.repository.BetTicketRepository;
import pl.coderslab.tennis_bet.betting_site.service.BetTicketService;
import pl.coderslab.tennis_bet.betting_site.service.OddsService;
import pl.coderslab.tennis_bet.betting_site.service.WalletService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class BetTicketServiceImpl implements BetTicketService {
    private final BetTicketRepository betTicketRepository;
    private WalletService walletService;
    private OddsService oddsService;

    @Autowired
    public BetTicketServiceImpl(BetTicketRepository betTicketRepository) {
        this.betTicketRepository = betTicketRepository;
    }
    @Autowired
    public void setOddsService(OddsService oddsService) {
        this.oddsService = oddsService;
    }
    @Autowired
    public void setWalletService(WalletService walletService) {
        this.walletService = walletService;
    }
    @Override
    public List<BetTicket> getAllByUser(User user) {
        return betTicketRepository.findAllByUser(user);
    }

    @Override
    public List<BetTicket> getAllByUserAndWonAndNotCashed(User user) {
        return betTicketRepository.findAllByUserAndBetTicketResultEqualsAndBetTicketStatusEquals(user, BetTicketResult.WON, BetTicketStatus.ENDED_NOT_CASHED);
    }

    @Override
    public BetTicket getOne(int id) {
        return betTicketRepository.getOne(id);
    }

    @Override
    public BetTicket save(BetTicket betTicket) {
        return betTicketRepository.save(betTicket);
    }

    @Override
    public void submitTicket(BigDecimal stake, BetTicket betTicket) {
        betTicket.setStake(stake);
        betTicket.setTotalOdd(calculateTotalOdd(betTicket));
        betTicket.setUncheckedBetSelectionsCounter(betTicket.getBetSelections().size());
        betTicket.setBetTicketStatus(BetTicketStatus.SUBMITTED);
        betTicket.setBetTicketResult(BetTicketResult.ONGOING);
        betTicket.getBetSelections().forEach(v -> v.setBetSelectionStatus(BetSelectionStatus.SUBMITTED));
        betTicket.getBetSelections().forEach(v -> v.setBetSelectionResult(BetSelectionResult.ONGOING));
        walletService.payForTicket(betTicket.getUser(), stake);
        save(betTicket);
    }

    @Override
    public boolean hasOddsChanged(BetTicket betTicket) {
        for (BetSelection betSelection : betTicket.getBetSelections()) {
            Odd currentOdds = oddsService.getOddsOfEvent(betSelection.getTennisMatch());
            switch (betSelection.getBetSelectionType()) {
                case PLAYER_ONE_WINS:
                    if (betSelection.getOdd().compareTo(currentOdds.getPlayerOneWinningOdd()) == 0) return false;
                case PLAYER_TWO_WINS:
                    if (betSelection.getOdd().compareTo(currentOdds.getPlayerTwoWinningOdd()) == 0) return false;
            }
        }
        return true;
    }

    @Override
    public void removeBetSelectionFromTicket(BetTicket betTicket, UUID temporalId) {
        BetSelection betSelection = betTicket.getBetSelections().stream().filter(v -> v.getTemporalId().compareTo(temporalId) == 0).findFirst().get();
        betTicket.removeBetSelection(betSelection);
    }

    @Override
    public boolean isTicketCompleted(BetTicket betTicket) {
        return betTicket.getUncheckedBetSelectionsCounter() == 0;
    }

    @Override
    public void resolveBetTicket(BetTicket betTicket) {
        betTicket.setBetTicketStatus(BetTicketStatus.ENDED_NOT_CASHED);
        List<BetSelection> betSelections = betTicket.getBetSelections();
        boolean isAnyBetSelectionLost = betSelections.stream().anyMatch(v -> v.getBetSelectionResult() == BetSelectionResult.LOST);
        boolean isAllBetSelectionVoid= betSelections.stream().allMatch(v -> v.getBetSelectionResult() == BetSelectionResult.VOID);
        if(isAnyBetSelectionLost){
            betTicket.setBetTicketResult(BetTicketResult.LOST);
        } else if(isAllBetSelectionVoid){
            betTicket.setBetTicketResult(BetTicketResult.CANCELLED);
        } else {
            betTicket.setBetTicketResult(BetTicketResult.WON);
        }
        save(betTicket);
    }

    @Override
    public void cashOutTicket(BetTicket betTicket, User user) {
        betTicket.setBetTicketStatus(BetTicketStatus.ENDED_AND_CASHED);
        save(betTicket);
        BigDecimal cashOutValue = calculateTotalOdd(betTicket).multiply(betTicket.getStake());
        walletService.cashOutTicket(user,cashOutValue);
    }

    @Override
    public void cashOutAllPossibleTickets(List<BetTicket> betTickets, User user) {
        for(BetTicket betTicket : betTickets){
            cashOutTicket(betTicket, user);
        }
    }

    @Override
    public BigDecimal calculateTotalOdd(BetTicket betTicket) {
        BigDecimal totalOdd = BigDecimal.ONE;
        for(BetSelection betSelection : betTicket.getBetSelections()){
            if(betSelection.getBetSelectionResult() != BetSelectionResult.VOID){
                totalOdd = totalOdd.multiply(betSelection.getOdd());
            }
        }

        return totalOdd.setScale(2,BigDecimal.ROUND_HALF_UP);
    }


}
