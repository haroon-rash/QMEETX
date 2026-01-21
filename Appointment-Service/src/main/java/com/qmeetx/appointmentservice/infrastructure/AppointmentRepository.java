package com.qmeetx.appointmentservice.infrastructure;

import com.qmeetx.appointmentservice.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByCustomerId(Long customerId);
    List<Appointment> findByOrganizationIdAndAppointmentDateBetween(Long organizationId, LocalDateTime start, LocalDateTime end);
}
