package ru.albina.planner.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "doctor_schedule")
@Accessors(chain = true)
public class DoctorScheduleEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "work_schedule_id", nullable = false)
    private WorkScheduleEntity workSchedule;

    @NotNull
    @Column(name = "manual_extra_hours", nullable = false)
    private Double manualExtraHours;

    @NotNull
    @Column(name = "force_schedule", nullable = false)
    private Boolean forceSchedule = false;

    @BatchSize(size = 9000)
    @OneToMany(mappedBy = "doctorSchedule", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<DoctorWorkEntity> doctorWorks = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorScheduleEntity that = (DoctorScheduleEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}