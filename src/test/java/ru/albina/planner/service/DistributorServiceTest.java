package ru.albina.planner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.albina.planner.dto.planner.DoctorDto;
import ru.albina.planner.dto.planner.PlannerDto;
import ru.albina.planner.dto.planner.WorkloadDto;
import ru.albina.planner.dto.reference.WeekNumberResult;
import ru.albina.planner.service.planner.scheduler.DistributorService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DistributorServiceTest {

    @InjectMocks
    private DistributorService distributorService;


    @Test
    void should() throws JsonProcessingException {
        final var data = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(
                        """
                                {"month":"2023-06-01", "monthlyHours":159,"doctors":[{"id":"b2bd1f04-6ad2-4daf-a6ba-3b08e60bcbda","hours":12,"rate":0.4,"modality":["DENSITOMETER"],"optionalModality":["FLG","RG","MRT_U","MMG","KT","KT_U2","KT_U","MRT_U2","MRT"],"workDays":[1,7],"absenceSchedules":["2024-06-06","2024-06-07","2024-06-08","2024-06-09","2024-06-10"],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"fb3226d2-9843-4ec1-9da8-0d6cc75b4ab7","hours":5,"rate":0,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG","RG","MRT_U","MMG","KT","KT_U2","KT_U","MRT_U2","MRT"],"workDays":null,"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"fb4592ec-3e46-40ad-8420-9af847f1384c","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG","RG","MRT_U","MMG","KT","KT_U2","KT_U","MRT_U2","MRT"],"workDays":null,"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"18d05afc-c1ab-4ced-b0da-c23a6d8c9fe6","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG","RG","MRT_U","MMG","KT","KT_U2","KT_U","MRT_U2","MRT"],"workDays":null,"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"840745c6-a6f1-4d27-bde8-87633f834fce","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG","RG","MRT_U","MMG","KT","KT_U2","KT_U","MRT_U2","MRT"],"workDays":null,"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"021acf4f-cfc5-468a-8c49-5fdfbf727821","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG","RG","MRT_U","MMG","KT","KT_U2","KT_U","MRT_U2","MRT"],"workDays":null,"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"bd4f83c8-ff18-4c59-8364-224fb37c42bd","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG","RG","MRT_U","MMG","KT","KT_U2","KT_U","MRT_U2","MRT"],"workDays":null,"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"18190fa7-d04a-47ec-a423-33c6dea96ca8","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG","RG","MRT_U","MMG","KT","KT_U2","KT_U","MRT_U2","MRT"],"workDays":null,"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"78992f9e-c892-4c33-afe2-0d6a1e644485","hours":4,"rate":1,"modality":["DENSITOMETER"],"optionalModality":["FLG"],"workDays":[1],"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"5999657f-efcf-4d6f-9070-31abcb6e696f","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG"],"workDays":[],"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"433cec1c-67be-4f1a-b598-795066323e6f","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG"],"workDays":[],"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"ec52200b-474a-42b7-857e-472ffc1fd676","hours":1,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG"],"workDays":[1],"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"948677bf-ac61-42b8-a253-1c24571c4841","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG"],"workDays":[],"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"e69e1a56-15f1-4224-b7e1-977ebdf37fb9","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG"],"workDays":[],"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"97ab71e4-7046-42b0-a231-fb2e1b79d00a","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG"],"workDays":[],"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"562ce644-4cc0-441b-8599-cbf16500e646","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG"],"workDays":[],"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"cc6cd60d-5b05-44ba-a863-078b63bba380","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG"],"workDays":[],"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"4d2c5f12-de2f-4ba1-b46f-79cecd169215","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG"],"workDays":[],"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"4ba7cdb8-b440-4161-9031-f147a687ea60","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG"],"workDays":[],"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}},{"id":"48924e43-c8b0-4c8b-b38e-627cb9d82af8","hours":8,"rate":1,"modality":["KT","KT_U2","KT_U"],"optionalModality":["FLG"],"workDays":[],"absenceSchedules":[],"performances":{"FLG":181,"RG":49,"MRT_U":15,"MMG":49,"KT":15,"DENSITOMETER":84,"KT_U2":11,"KT_U":16,"MRT_U2":10,"MRT":12}}],"workload":[{"week":22,"modality":"DENSITOMETER","value":1801},{"week":22,"modality":"MRT_U","value":955},{"week":22,"modality":"MRT_U2","value":4},{"week":22,"modality":"MRT","value":2095},{"week":22,"modality":"RG","value":66736},{"week":22,"modality":"MMG","value":20064},{"week":22,"modality":"KT_U","value":692},{"week":22,"modality":"KT_U2","value":785},{"week":22,"modality":"KT","value":4877},{"week":22,"modality":"FLG","value":21963},{"week":23,"modality":"DENSITOMETER","value":1853},{"week":23,"modality":"MRT_U","value":940},{"week":23,"modality":"MRT_U2","value":12},{"week":23,"modality":"MRT","value":2000},{"week":23,"modality":"RG","value":66782},{"week":23,"modality":"MMG","value":19248},{"week":23,"modality":"KT_U","value":712},{"week":23,"modality":"KT_U2","value":801},{"week":23,"modality":"KT","value":4786},{"week":23,"modality":"FLG","value":21128},{"week":24,"modality":"DENSITOMETER","value":1671},{"week":24,"modality":"MRT_U","value":789},{"week":24,"modality":"MRT_U2","value":9},{"week":24,"modality":"MRT","value":2014},{"week":24,"modality":"RG","value":58669},{"week":24,"modality":"MMG","value":16114},{"week":24,"modality":"KT_U","value":607},{"week":24,"modality":"KT_U2","value":669},{"week":24,"modality":"KT","value":4301},{"week":24,"modality":"FLG","value":17444},{"week":25,"modality":"DENSITOMETER","value":1810},{"week":25,"modality":"MRT_U","value":939},{"week":25,"modality":"MRT_U2","value":6},{"week":25,"modality":"MRT","value":2221},{"week":25,"modality":"RG","value":64272},{"week":25,"modality":"MMG","value":18021},{"week":25,"modality":"KT_U","value":733},{"week":25,"modality":"KT_U2","value":914},{"week":25,"modality":"KT","value":4737},{"week":25,"modality":"FLG","value":20509},{"week":26,"modality":"DENSITOMETER","value":1854},{"week":26,"modality":"MRT_U","value":924},{"week":26,"modality":"MRT_U2","value":6},{"week":26,"modality":"MRT","value":2182},{"week":26,"modality":"RG","value":62669},{"week":26,"modality":"MMG","value":17448},{"week":26,"modality":"KT_U","value":683},{"week":26,"modality":"KT_U2","value":825},{"week":26,"modality":"KT","value":4780},{"week":26,"modality":"FLG","value":19070}],"weekNumbers":[{"startDate":"2023-06-12","endDate":"2023-06-18","weekNumber":24},{"startDate":"2023-06-26","endDate":"2023-07-02","weekNumber":26},{"startDate":"2023-06-05","endDate":"2023-06-11","weekNumber":23},{"startDate":"2023-06-19","endDate":"2023-06-25","weekNumber":25},{"startDate":"2023-05-29","endDate":"2023-06-04","weekNumber":22}],"schedule":[{"weekNumber":22,"doctors":[]},{"weekNumber":23,"doctors":[]},{"weekNumber":24,"doctors":[]},{"weekNumber":25,"doctors":[{"id":"b2bd1f04-6ad2-4daf-a6ba-3b08e60bcbda","days":[{"date":"2023-06-25","extraHours":2,"forceSchedule":true,"tasks":[{"modality":"RG","hours":3,"extraHours":null}]}]}]},{"weekNumber":26,"doctors":[]}]}
                                """,
                        PlannerDto.class
                );

        final var plan = this.distributorService.distributeDoctors(data);

        // assertThat(plan.get(LocalDate.of(2023,6,18)))

    }


    @Test
    void goodData() throws JsonProcessingException {
        final var doctor = UUID.randomUUID();
        final var plan = this.distributorService.distributeDoctors(
                PlannerDto.builder()
                        .month(LocalDate.of(2023, 6,1))
                        .doctors(
                                List.of(
                                        DoctorDto.builder()
                                                .id(doctor)
                                                .hours(5d)
                                                .rate(1d)
                                                .modality(Set.of("DENSITOMETER"))
                                                .optionalModality(Set.of("FLG"))
                                                .workDays(List.of(4, 7))
                                                .absenceSchedules(List.of(
                                                        LocalDate.of(2024, 6, 6),
                                                        LocalDate.of(2024, 6, 7),
                                                        LocalDate.of(2024, 6, 8),
                                                        LocalDate.of(2024, 6, 9),
                                                        LocalDate.of(2024, 6, 10))
                                                )
                                                .performances(Map.of(
                                                        "FLG", 1,
                                                        "RG", 1,
                                                        "MRT_U", 1,
                                                        "MMG", 1,
                                                        "KT", 1,
                                                        "DENSITOMETER", 1,
                                                        "KT_U2", 1,
                                                        "KT_U", 1,
                                                        "MRT_U2", 1,
                                                        "MRT", 1))
                                                .build(),
                                        DoctorDto.builder()
                                                .id(UUID.randomUUID())
                                                .hours(1d)
                                                .rate(1d)
                                                .modality(Set.of("FLG"))
                                                .optionalModality(Set.of())
                                                .workDays(List.of(4, 7))
                                                .absenceSchedules(List.of(
                                                        LocalDate.of(2024, 6, 6),
                                                        LocalDate.of(2024, 6, 7),
                                                        LocalDate.of(2024, 6, 8),
                                                        LocalDate.of(2024, 6, 9),
                                                        LocalDate.of(2024, 6, 10))
                                                )
                                                .performances(Map.of(
                                                        "FLG", 1,
                                                        "RG", 1,
                                                        "MRT_U", 1,
                                                        "MMG", 1,
                                                        "KT", 1,
                                                        "DENSITOMETER", 1,
                                                        "KT_U2", 1,
                                                        "KT_U", 1,
                                                        "MRT_U2", 1,
                                                        "MRT", 1))
                                                .build()
                                )
                        )
                        .workload(
                                List.of(
                                        WorkloadDto.builder()
                                                .value(10L)
                                                .modality("DENSITOMETER")
                                                .week(1)
                                                .build(),
                                        WorkloadDto.builder()
                                                .value(10L)
                                                .modality("FLG")
                                                .week(1)
                                                .build(),
                                        WorkloadDto.builder()
                                                .value(10L)
                                                .modality("DENSITOMETER")
                                                .week(2)
                                                .build()
                                )
                        )
                        .monthlyHours(100d)
                        .weekNumbers(
                                List.of(
                                        WeekNumberResult.builder()
                                                .weekNumber(1)
                                                .startDate(LocalDate.of(2023, 6,1))
                                                .endDate(LocalDate.of(2023, 6,7))
                                                .build(),
                                        WeekNumberResult.builder()
                                                .weekNumber(2)
                                                .startDate(LocalDate.of(2023, 6,8))
                                                .endDate(LocalDate.of(2023, 6,14))
                                                .build()
                                )
                        )
                        .schedule(
                                List.of(

                                )
                        )
                .build());

        // assertThat(plan.get(LocalDate.of(2023,6,18)))

    }
}