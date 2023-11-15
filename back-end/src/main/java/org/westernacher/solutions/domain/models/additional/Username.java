package org.westernacher.solutions.domain.models.additional;

import org.springframework.security.core.Authentication;

import java.util.Map;

public class Username
{

    public static String get(Authentication authentication)
    {
        String username = "Guest";

        if(authentication != null)
        {
            username = authentication.getName();
        }

        return username;
    }
}
