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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
//@RequestMapping("/login")
public class LoginController {
    @ModelAttribute("loginForm")
    public LoginForm createForm(
                @RequestParam(value="login_email",    required=false) String email,
                @RequestParam(value="login_password", required=false) String password,
                @RequestParam(value="login_url",      required=false) String redirectUrl
                ) {
        LoginForm loginForm = new LoginForm();

        loginForm.setEmail(email);
        loginForm.setPassword(password);
        loginForm.setUrl(redirectUrl);

        return loginForm;
    }

    @RequestMapping
    public String showForm(@ModelAttribute("loginForm") LoginForm loginForm, HttpServletRequest request) {
        if (StringUtils.isEmpty(loginForm.getUrl())) {
            String referer = request.getHeader("referer");

            if (!StringUtils.isEmpty(referer) && !referer.contains("/login")) {
                loginForm.setUrl(referer);
            }
        }

        return "pages/login";
    }

    @RequestMapping(params = "submit")
    public String processForm(@ModelAttribute("loginForm") LoginForm loginForm) {

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
