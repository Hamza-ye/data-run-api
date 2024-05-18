package org.nmcpye.datarun.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.datarun.domain.Team;
import org.nmcpye.datarun.repository.TeamRepository;
import org.nmcpye.datarun.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.nmcpye.datarun.domain.Team}.
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    private final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team save(Team team) {
        log.debug("Request to save Team : {}", team);
        return teamRepository.save(team);
    }

    @Override
    public Team update(Team team) {
        log.debug("Request to update Team : {}", team);
        team.setIsPersisted();
        return teamRepository.save(team);
    }

    @Override
    public Optional<Team> partialUpdate(Team team) {
        log.debug("Request to partially update Team : {}", team);

        return teamRepository
            .findById(team.getId())
            .map(existingTeam -> {
                if (team.getTeamLeader() != null) {
                    existingTeam.setTeamLeader(team.getTeamLeader());
                }
                if (team.getTeamNo() != null) {
                    existingTeam.setTeamNo(team.getTeamNo());
                }
                if (team.getTeamNote() != null) {
                    existingTeam.setTeamNote(team.getTeamNote());
                }
                if (team.getTeamMobile() != null) {
                    existingTeam.setTeamMobile(team.getTeamMobile());
                }
                if (team.getTeamNoOfTeamWorkers() != null) {
                    existingTeam.setTeamNoOfTeamWorkers(team.getTeamNoOfTeamWorkers());
                }
                if (team.getMobility() != null) {
                    existingTeam.setMobility(team.getMobility());
                }
                if (team.getCreatedBy() != null) {
                    existingTeam.setCreatedBy(team.getCreatedBy());
                }
                if (team.getCreatedDate() != null) {
                    existingTeam.setCreatedDate(team.getCreatedDate());
                }
                if (team.getLastModifiedBy() != null) {
                    existingTeam.setLastModifiedBy(team.getLastModifiedBy());
                }
                if (team.getLastModifiedDate() != null) {
                    existingTeam.setLastModifiedDate(team.getLastModifiedDate());
                }

                return existingTeam;
            })
            .map(teamRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> findAll() {
        log.debug("Request to get all Teams");
        return teamRepository.findAll();
    }

    public Page<Team> findAllWithEagerRelationships(Pageable pageable) {
        return teamRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Team> findOne(Long id) {
        log.debug("Request to get Team : {}", id);
        return teamRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.deleteById(id);
    }
}
