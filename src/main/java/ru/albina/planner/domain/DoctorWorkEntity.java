package ru.albina.planner.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.albina.planner.dto.TypeModality;
import ru.albina.planner.dto.medical.Modality;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "doctor_work")
public class DoctorWorkEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_schedule_id", nullable = false)
    private DoctorScheduleEntity doctorSchedule;

    @Size(max = 30)
    @Column(name = "modality", length = 30)
    @Enumerated(EnumType.STRING)
    private Modality modality;

    @Size(max = 30)
    @Column(name = "type_modality", length = 30)
    @Enumerated(EnumType.STRING)
    private TypeModality typeModality;

    @Column(name = "used_hours")
    private Double usedHours;

    @Column(name = "used_extra_hours")
    private Double usedExtraHours;

}