package com.qmeetx.appointmentservice;

import com.qmeetx.appointmentservice.domain.Appointment;
import com.qmeetx.appointmentservice.domain.AppointmentSlot;
import com.qmeetx.appointmentservice.domain.AppointmentStatus;
import com.qmeetx.appointmentservice.infrastructure.AppointmentRepository;
import com.qmeetx.appointmentservice.infrastructure.AppointmentSlotRepository;
import com.qmeetx.appointmentservice.infrastructure.AppointmentEventProducer;
import com.qmeetx.qmeetxshared.events.AppointmentBookedEvent;
import com.qmeetx.qmeetxshared.events.AppointmentCheckedInEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@ActiveProfiles("test")
class AppointmentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentSlotRepository slotRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @MockBean
    private AppointmentEventProducer eventProducer;

    @BeforeEach
    void setup() {
        appointmentRepository.deleteAll();
        slotRepository.deleteAll();
    }

    @Test
    void testFullAppointmentFlow_10_Users() throws Exception {
        Long organizationId = 1L;
        Long providerId = 101L;

        // 1. Create Slots for 10 Users
        for (int i = 0; i < 10; i++) {
            AppointmentSlot slot = AppointmentSlot.builder()
                    .organizationId(organizationId)
                    .providerId(providerId)
                    .startTime(LocalDateTime.now().plusHours(i + 1))
                    .endTime(LocalDateTime.now().plusHours(i + 1).plusMinutes(30))
                    .isBooked(false)
                    .build();
            slotRepository.save(slot);
        }

        List<AppointmentSlot> slots = slotRepository.findAll();
        assertEquals(10, slots.size(), "Should have 10 slots created");

        // 2. Simulate 10 Users Booking Appointments
        for (int i = 0; i < 10; i++) {
            Long customerId = (long) (200 + i);
            AppointmentSlot slot = slots.get(i);

            // User books appointment
            mockMvc.perform(post("/api/appointments/book")
                            .param("slotId", slot.getId().toString())
                            .param("customerId", customerId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("BOOKED"));
        }

        // Verify all slots booked and events published
        assertEquals(10, appointmentRepository.count(), "Should have 10 appointments");
        verify(eventProducer, times(10)).publishAppointmentBooked(any(AppointmentBookedEvent.class));
        
        // 3. Simulate Check-In for 5 Users (Auto-Join Queue Flow)
        List<Appointment> appointments = appointmentRepository.findAll();
        for (int i = 0; i < 5; i++) {
            Appointment appt = appointments.get(i);
            
            mockMvc.perform(post("/api/appointments/" + appt.getId() + "/check-in"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("CHECKED_IN"));
        }
        verify(eventProducer, times(5)).publishAppointmentCheckedIn(any(AppointmentCheckedInEvent.class));
        
        // 4. Simulate Cancellation for 2 Users
        for (int i = 5; i < 7; i++) {
            Appointment appt = appointments.get(i);
            
            mockMvc.perform(post("/api/appointments/" + appt.getId() + "/cancel")
                            .param("reason", "Changed mind"))
                    .andExpect(status().isOk());
            
            // Verify status
            Appointment cancelled = appointmentRepository.findById(appt.getId()).get();
            assertEquals(AppointmentStatus.CANCELLED, cancelled.getStatus());
        }
        
        System.out.println("End-to-End Test for 10 Users completed successfully.");
    }
}
