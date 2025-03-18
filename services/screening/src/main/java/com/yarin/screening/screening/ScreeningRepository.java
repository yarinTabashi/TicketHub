package com.yarin.screening.screening;

import com.yarin.screening.dtos.ScreeningResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ScreeningRepository extends JpaRepository<Screening, Integer> {
    // Return ScreeningResponse and not screening, so that it's not fetch the avaliabilty map, to do it easier method
    List<ScreeningResponse> findByMovieId(Integer movieId);
}

