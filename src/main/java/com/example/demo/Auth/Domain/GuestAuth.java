package com.example.demo.Auth.Domain;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class GuestAuth {
    public static String getIdentifier(Cookie cookie, HttpServletResponse response){

        if(cookie != null){
            System.out.println("value is : " + cookie.getValue());
            return cookie.getValue();
        }else{
            System.out.println("cookie is empty....");
            Cookie clientId = new Cookie("clientId", UUID.randomUUID().toString());
            clientId.setPath("/");
            clientId.setMaxAge(60*60*24*30);
            response.addCookie(clientId);

            return clientId.getValue();
        }
    }
}
