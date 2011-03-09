package org.dspace.webmvc.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LicenseEditController {
    @ModelAttribute("licenseForm")
    public LicenseForm createForm() {
        return new LicenseForm();
    }

    @RequestMapping
    public String showForm(LicenseForm licenseForm) {
        return "pages/admin/license";
    }

    @RequestMapping(params = "submit")
    public String processForm(LicenseForm licenseForm) {
        return showForm(licenseForm);
    }


    public static class LicenseForm {
        private String licenseText;

        public String getLicenseText() {
            return licenseText;
        }

        public void setLicenseText(String pLicenseText) {
            this.licenseText = pLicenseText;
        }
    }
}
