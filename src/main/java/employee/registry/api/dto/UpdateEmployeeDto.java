package employee.registry.api.dto;

import employee.registry.api.model.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateEmployeeDto {

    @NotBlank(message = "Имя не должно быть пустым")
    private String firstName;

    @NotBlank(message = "Фамилия не должна быть пустой")
    private String lastName;

    @NotBlank(message = "Должность не должна быть пустой")
    private String position;

    @NotNull(message = "Зарплата не должна быть пустой")
    @Positive(message = "Зарплата должна быть больше нуля")
    private BigDecimal salary;

    @NotNull(message = "Идентификатор отдела не должен быть пустым")
    @Positive(message = "Идентификатор отдела должен быть положительным числом")
    private Long departmentId;
}
