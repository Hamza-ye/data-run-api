package org.nmcpye.datarun.service.impl;

import org.nmcpye.datarun.domain.Assignment;
import org.nmcpye.datarun.repository.AssignmentRepositoryCustom;
import org.nmcpye.datarun.security.SecurityUtils;
import org.nmcpye.datarun.service.AssignmentServiceCustom;
import org.nmcpye.datarun.utils.CodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Primary
@Transactional
public class AssignmentServiceImplCustom
    extends AssignmentServiceImpl implements AssignmentServiceCustom {

    private final Logger log = LoggerFactory.getLogger(AssignmentServiceImplCustom.class);

    AssignmentRepositoryCustom assignmentRepositoryCustom;

    public AssignmentServiceImplCustom(AssignmentRepositoryCustom assignmentRepositoryCustom) {
        super(assignmentRepositoryCustom);
        this.assignmentRepositoryCustom = assignmentRepositoryCustom;
    }

    @Override
    public Assignment save(Assignment assignment) {
        if (assignment.getUid() == null || assignment.getUid().isEmpty()) {
            assignment.setUid(CodeGenerator.generateUid());
        }
        return super.save(assignment);
    }

    public Page<Assignment> findAllWithEagerRelationships(Pageable pageable) {
        return assignmentRepositoryCustom.findAllWithEagerRelationshipsByUser(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Assignment> findOne(Long id) {
        log.debug("Request to get Assignment : {}", id);
        return assignmentRepositoryCustom.findOneWithEagerRelationshipsByUser(id);
    }
}
