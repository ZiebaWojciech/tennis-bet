package pl.coderslab.tennis_bet.sport_events_data.service;

import pl.coderslab.tennis_bet.betting_site.entity.Result;
import pl.coderslab.tennis_bet.sport_events_data.dto.ResultDTO;


public interface ResultDTOService {
    Result convertResultDtoToEntity(ResultDTO resultDTO);
}


