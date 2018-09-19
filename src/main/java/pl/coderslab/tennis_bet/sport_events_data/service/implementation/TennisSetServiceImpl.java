package pl.coderslab.tennis_bet.sport_events_data.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.tennis_bet.sport_events_data.entity.Result;
import pl.coderslab.tennis_bet.sport_events_data.entity.TennisSet;
import pl.coderslab.tennis_bet.sport_events_data.repository.TennisSetRepository;
import pl.coderslab.tennis_bet.sport_events_data.service.ResultService;
import pl.coderslab.tennis_bet.sport_events_data.service.TennisSetService;

import java.util.List;

@Service
public class TennisSetServiceImpl implements TennisSetService {
    private final TennisSetRepository tennisSetRepository;

    private ResultService resultService;

    @Autowired
    public TennisSetServiceImpl(TennisSetRepository tennisSetRepository) {
        this.tennisSetRepository = tennisSetRepository;

    }


    @Autowired
    public void setResultService(ResultService resultService) {
        this.resultService = resultService;
    }


    @Override
    public TennisSet getOne(int id) {
        return tennisSetRepository.getOne(id);
    }

    @Override
    public List<TennisSet> getAll() {
        return tennisSetRepository.findAll();
    }

    @Override
    public TennisSet save(TennisSet tennisSet) {
        return tennisSetRepository.save(tennisSet);
    }
    @Override
    public long countGamesWonByPlayerOne(Result result) {
        return resultService.getCurrentSet(result).getGames().stream()
                .filter(game -> result.getTennisMatch().getPlayerOne().equals(game.getTennisGameWinner()))
                .count();
    }
    @Override
    public long countGamesWonByPlayerTwo(Result result) {
        return resultService.getCurrentSet(result).getGames().stream()
                .filter(game -> result.getTennisMatch().getPlayerTwo().equals(game.getTennisGameWinner()))
                .count();
    }

}