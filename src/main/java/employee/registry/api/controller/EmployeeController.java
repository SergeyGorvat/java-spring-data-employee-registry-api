package employee.registry.api.controller;

import employee.registry.api.dto.CreateEmployeeDto;
import employee.registry.api.dto.UpdateEmployeeDto;
import employee.registry.api.model.Employee;
import employee.registry.api.projection.EmployeeProjection;
import employee.registry.api.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/projections")
    public ResponseEntity<List<EmployeeProjection>> getAllProjections() {
        return ResponseEntity.ok(employeeService.getAllProjections());
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployee() {
        return ResponseEntity.ok(employeeService.getAllEmployee());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody CreateEmployeeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.createEmployee(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeDto dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
