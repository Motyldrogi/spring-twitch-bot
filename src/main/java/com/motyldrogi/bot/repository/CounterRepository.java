package com.motyldrogi.bot.repository;

import java.util.Optional;

import com.motyldrogi.bot.entity.impl.CounterEntityImpl;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CounterRepository extends PagingAndSortingRepository<CounterEntityImpl, Long> {

    Optional<CounterEntityImpl> findByName(String name);

}
