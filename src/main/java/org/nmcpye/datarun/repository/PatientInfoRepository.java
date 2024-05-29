package org.nmcpye.datarun.repository;

import org.nmcpye.datarun.domain.PatientInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the PatientInfo entity.
 */
@Repository
public interface PatientInfoRepository extends JpaRepository<PatientInfo, Long> {
    default Optional<PatientInfo> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PatientInfo> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PatientInfo> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select patientInfo from PatientInfo patientInfo left join fetch patientInfo.location",
        countQuery = "select count(patientInfo) from PatientInfo patientInfo"
    )
    Page<PatientInfo> findAllWithToOneRelationships(Pageable pageable);

    @Query("select patientInfo from PatientInfo patientInfo left join fetch patientInfo.location")
    List<PatientInfo> findAllWithToOneRelationships();

    @Query("select patientInfo from PatientInfo patientInfo left join fetch patientInfo.location where patientInfo.id =:id")
    Optional<PatientInfo> findOneWithToOneRelationships(@Param("id") Long id);
}