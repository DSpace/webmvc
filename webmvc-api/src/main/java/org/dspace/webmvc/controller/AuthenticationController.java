package org.dspace.webmvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class AuthenticationController {
    @RequestMapping()
    public String handleAuthorizeException() {
        return "redirect:/login";
    }
}
