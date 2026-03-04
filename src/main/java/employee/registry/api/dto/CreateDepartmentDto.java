package employee.registry.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDepartmentDto {

    @NotBlank(message = "Название отдела не должно быть пустым")
    private String name;
}
