package com.nerlogistics.backend.service;

import com.nerlogistics.backend.entity.HazardEvent;
import com.nerlogistics.backend.repository.HazardEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HazardService {

    private final HazardEventRepository hazardEventRepository;

    public List<HazardEvent> getAllActiveHazards() {
        return hazardEventRepository.findByStatus("ACTIVE");
    }

    public List<HazardEvent> getAllHazards() {
        return hazardEventRepository.findAll();
    }

    @Transactional
    public HazardEvent createHazard(HazardEvent event) {
        if (event.getOccurredAt() == null) {
            event.setOccurredAt(LocalDateTime.now());
        }
        if (event.getExpiresAt() == null) {
            event.setExpiresAt(LocalDateTime.now().plusDays(3));
        }
        return hazardEventRepository.save(event);
    }

    @Transactional
    public void deactivateHazard(Long id) {
        hazardEventRepository.findById(id).ifPresent(h -> {
            h.setStatus("INACTIVE");
            hazardEventRepository.save(h);
        });
    }
}
