package org.westernacher.solutions.service;

import org.westernacher.solutions.domain.entities.Log;

import java.util.List;

public interface LogService
{
    boolean insertLog(Log log);
    void removeLogsById(List<String> list);
    List<Log> getAll();
    void removeAllLogs();
}
