package com.nerlogistics.backend.repository;

import com.nerlogistics.backend.entity.Road;
import com.nerlogistics.backend.enums.RoadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadRepository extends JpaRepository<Road, Long> {
    List<Road> findByState(String state);
    List<Road> findByStatus(RoadStatus status);
}
