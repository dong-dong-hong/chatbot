package com.chatbot.project.config;

import java.security.Principal;

public class StompPrincipal implements Principal {
    private final String name;

    private final String token;

    public StompPrincipal(String name, String token) {
        this.name = name;
        this.token = token;
    }


    @Override
    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "StompPrincipal{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

}
