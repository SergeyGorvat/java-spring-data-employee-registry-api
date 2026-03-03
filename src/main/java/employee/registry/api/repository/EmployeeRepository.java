package employee.registry.api.repository;

import employee.registry.api.model.Employee;
import employee.registry.api.projection.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByDepartmentId(Long departmentId);

    @Query("""
            SELECT CONCAT(e.firstName, ' ', e.lastName) AS fullName,
                   e.position                            AS position,
                   e.department.name                     AS departmentName
            FROM Employee e
            """)
    List<EmployeeProjection> findAllProjections();
}
