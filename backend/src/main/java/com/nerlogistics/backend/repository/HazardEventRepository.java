package com.nerlogistics.backend.repository;

import com.nerlogistics.backend.entity.HazardEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HazardEventRepository extends JpaRepository<HazardEvent, Long> {
    List<HazardEvent> findByStatus(String status);
}
