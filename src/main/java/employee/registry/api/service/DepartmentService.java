package employee.registry.api.service;

import employee.registry.api.dto.CreateDepartmentDto;
import employee.registry.api.exception.DepartmentNotFoundException;
import employee.registry.api.model.Department;
import employee.registry.api.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    @Transactional
    public Department createDepartment(CreateDepartmentDto dto) {
        return departmentRepository.save(
                Department.builder().name(dto.getName()).build()
        );
    }

    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new DepartmentNotFoundException(id);
        }

        departmentRepository.deleteById(id);
    }
}
