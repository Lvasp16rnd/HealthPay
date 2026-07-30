package com.healthpay.appointment_service.application.dto;

import com.healthpay.appointment_service.domain.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAppointmentStatusRequest {

    private AppointmentStatus status;

}
