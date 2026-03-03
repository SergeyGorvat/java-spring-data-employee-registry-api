package employee.registry.api.service;

import employee.registry.api.dto.CreateEmployeeDto;
import employee.registry.api.dto.UpdateEmployeeDto;
import employee.registry.api.exception.EmployeeNotFoundException;
import employee.registry.api.model.Employee;
import employee.registry.api.projection.EmployeeProjection;
import employee.registry.api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;

    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public List<EmployeeProjection> getAllProjections() {
        return employeeRepository.findAllProjections();
    }

    @Transactional
    public Employee createEmployee(CreateEmployeeDto dto) {
        Employee employee = Employee.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .position(dto.getPosition())
                .salary(dto.getSalary())
                .department(departmentService.getDepartmentById(dto.getDepartmentId()))
                .build();

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Long id, UpdateEmployeeDto dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPosition(dto.getPosition());
        employee.setSalary(dto.getSalary());
        employee.setDepartment(departmentService.getDepartmentById(dto.getDepartmentId()));

        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }

        employeeRepository.deleteById(id);
    }
}
