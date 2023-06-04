package com.kikopolis.wordcloudworker.repository;

import com.kikopolis.wordcloudworker.entity.WordCount;
import org.springframework.data.repository.CrudRepository;

public interface WordCountRepository extends CrudRepository<WordCount, Long> { }
