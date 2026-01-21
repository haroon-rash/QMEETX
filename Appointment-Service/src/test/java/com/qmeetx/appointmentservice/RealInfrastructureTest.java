package com.qmeetx.appointmentservice;

import com.qmeetx.appointmentservice.domain.AppointmentSlot;
import com.qmeetx.appointmentservice.infrastructure.AppointmentRepository;
import com.qmeetx.appointmentservice.infrastructure.AppointmentSlotRepository;
import org.junit.jupiter.api.Test; // Use standard Test
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("real") // Uses application-real.properties
class RealInfrastructureTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentSlotRepository slotRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void verifyRemoteInfrastructure_10Users() throws Exception {
        System.out.println(">>> STARTING REAL INFRASTRUCTURE TEST <<<");
        
        // NOTE: We do NOT clear the DB here to avoid wiping existing data if user didn't intend it.
        // Instead, we use unique providers/slots to avoid conflicts.
        
        Long organizationId = 999L; // Test Org
        Long providerId = 888L; // Test Provider

        // 1. Create Slots for 10 Users
        System.out.println(">>> Creating 10 Slots...");
        for (int i = 0; i < 10; i++) {
            AppointmentSlot slot = AppointmentSlot.builder()
                    .organizationId(organizationId)
                    .providerId(providerId)
                    .startTime(LocalDateTime.now().plusDays(1).plusHours(i)) // Future dates
                    .endTime(LocalDateTime.now().plusDays(1).plusHours(i).plusMinutes(30))
                    .isBooked(false)
                    .build();
            slotRepository.save(slot);
        }

        // 2. Book 10 Appointments
        System.out.println(">>> Booking 10 Appointments...");
        var slots = slotRepository.findByOrganizationIdAndStartTimeBetween(
                organizationId, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        
        for (int i = 0; i < 10 && i < slots.size(); i++) {
            Long customerId = (long) (5000 + i);
            var slot = slots.get(i);

            mockMvc.perform(post("/api/appointments/book")
                            .param("slotId", slot.getId().toString())
                            .param("customerId", customerId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("BOOKED"));
        }
        
        // 3. Check In 5 Users (Triggers Kafka)
        System.out.println(">>> Checking In 5 Users (Triggers Kafka)...");
        var appointments = appointmentRepository.findByCustomerId(5000L); // Just grab one to get ID logic, or find all
        // Better: find by org
        var orgAppointments = appointmentRepository.findByOrganizationIdAndAppointmentDateBetween(
                organizationId, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        for (int i = 0; i < 5 && i < orgAppointments.size(); i++) {
             var appt = orgAppointments.get(i);
             mockMvc.perform(post("/api/appointments/" + appt.getId() + "/check-in"))
                     .andExpect(status().isOk())
                     .andExpect(jsonPath("$.status").value("CHECKED_IN"));
        }

        System.out.println(">>> REAL INFRASTRUCTURE TEST COMPLETED SUCCESSFULLY <<<");
    }
}
