package com.ems.api.repository;

import com.ems.api.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaRepo extends JpaRepository<Agenda,Long> {
    List<Agenda> findAllByEventId(Long eventId);
}
