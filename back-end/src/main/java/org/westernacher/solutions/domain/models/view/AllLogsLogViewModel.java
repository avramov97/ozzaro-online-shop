package org.westernacher.solutions.domain.models.view;

import org.westernacher.solutions.domain.entities.Operation;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class AllLogsLogViewModel
{
    private String id;

    private String user;

    @Enumerated(EnumType.STRING)
    private Operation operation;

    private String tableName;

    private String date;

    public AllLogsLogViewModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
