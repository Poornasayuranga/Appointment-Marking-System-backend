package com.ams.backend.repository;

import com.ams.backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("""
            select d from Doctor d
            where (:search is null or :search = ''
                or lower(d.name) like lower(concat('%', :search, '%'))
                or lower(d.specialization) like lower(concat('%', :search, '%')))
            and (:specialization is null or :specialization = '' or d.specialization = :specialization)
            """)
    List<Doctor> search(@Param("search") String search, @Param("specialization") String specialization);
}
