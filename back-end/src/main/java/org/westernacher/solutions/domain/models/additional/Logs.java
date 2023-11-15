package org.westernacher.solutions.domain.models.additional;

import org.westernacher.solutions.domain.entities.Log;
import org.westernacher.solutions.domain.entities.Operation;
import org.springframework.security.core.Authentication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logs {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static Log setupLog(Authentication authentication, String table, Operation operation) {
         Log log = new Log();
         Date date = new Date();
         log.setOperation(operation);
         log.setTableName(table);
         log.setDate(dateFormat.format(date));

         if(authentication != null) {
             log.setUser(authentication.getName());
         }
         else {
             log.setUser("Guest"); // should never come here
         }

         return log;
    }
}
