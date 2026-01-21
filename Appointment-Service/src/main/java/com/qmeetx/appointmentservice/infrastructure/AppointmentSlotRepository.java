package com.qmeetx.appointmentservice.infrastructure;

import com.qmeetx.appointmentservice.domain.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {
    
    List<AppointmentSlot> findByOrganizationIdAndStartTimeBetween(Long organizationId, LocalDateTime start, LocalDateTime end);

    @Lock(LockModeType.PESSIMISTIC_WRITE) 
    Optional<AppointmentSlot> findById(Long id);
}
