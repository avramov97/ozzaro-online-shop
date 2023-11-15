package org.westernacher.solutions.repository;

import org.westernacher.solutions.domain.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogsRepository extends JpaRepository<Log, Integer>
{
    Log findLogById(String id);
    void deleteLogById(String id);
    void deleteLogsById(List<String> list);
}
