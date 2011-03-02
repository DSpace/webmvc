/**
 * $Id: $
 * $URL: $
 * *************************************************************************
 * Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 * Licensed under the DuraSpace License.
 *
 * A copy of the DuraSpace License has been included in this
 * distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
 */

package org.dspace.webmvc.controller;

import com.sun.deploy.net.HttpRequest;
import org.apache.commons.lang.StringUtils;
import org.dspace.core.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
//@RequestMapping("/login")
public class LoginController {
    @ModelAttribute("loginForm")
    public LoginForm createForm() {
        return new LoginForm();
    }

    @RequestMapping
    public String showForm(LoginForm loginForm, @RequestHeader(value = "referer", required = false) String referer) {
        if (StringUtils.isEmpty(loginForm.getUrl())) {
            if (!StringUtils.isEmpty(referer) && !referer.contains("/login")) {
                loginForm.setUrl(referer);
            }
        }

        return "pages/login";
    }

    @RequestMapping(params = "submit")
    public String processForm(Context context, LoginForm loginForm) {

        if (!StringUtils.isEmpty(loginForm.getUrl())) {
            return "redirect:" + loginForm.getUrl();
        }

        return "pages/login";
    }

    public class LoginForm {
        private String email;
        private String password;
        private String url;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
