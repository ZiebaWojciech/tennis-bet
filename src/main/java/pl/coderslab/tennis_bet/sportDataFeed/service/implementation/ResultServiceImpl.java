package pl.coderslab.tennis_bet.sportDataFeed.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.tennis_bet.sportDataFeed.entity.TennisMatch;
import pl.coderslab.tennis_bet.sportDataFeed.entity.Result;
import pl.coderslab.tennis_bet.sportDataFeed.entity.TennisGame;
import pl.coderslab.tennis_bet.sportDataFeed.entity.TennisSet;
import pl.coderslab.tennis_bet.sportDataFeed.repository.ResultRepository;
import pl.coderslab.tennis_bet.sportDataFeed.service.ResultService;
import pl.coderslab.tennis_bet.sportDataFeed.service.TennisGameService;
import pl.coderslab.tennis_bet.sportDataFeed.service.TennisSetService;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;

    private TennisGameService tennisGameService;
    private TennisSetService tennisSetService;

    @Autowired
    public ResultServiceImpl(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @Autowired
    public void setTennisGameService(TennisGameService tennisGameService) {
        this.tennisGameService = tennisGameService;
    }

    @Autowired
    public void setTennisSetService(TennisSetService tennisSetService) {
        this.tennisSetService = tennisSetService;
    }

    @Override
    public Result getOne(int id) {
        return resultRepository.getOne(id);
    }

    @Override
    public Result getOneByEvent(TennisMatch tennisMatch) {
        return resultRepository.getByTennisMatch(tennisMatch);
    }

    @Override
    public List<Result> getAll() {
        return resultRepository.findAll();
    }

    @Override
    public Result save(Result result) {
        return resultRepository.save(result);
    }


    @Override
    public TennisSet getCurrentSet(Result result) {
        return result.getSets().stream()
                .filter(TennisSet::isInPlay)
                .findAny()
                .orElse(null);
    }

    @Override
    public TennisGame getCurrentGame(Result result) {
        return getCurrentSet(result).getGames().stream()
                .filter(TennisGame::isInPlay)
                .findAny()
                .orElse(null);
    }

}
