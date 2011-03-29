package org.dspace.webmvc.controller;

import org.apache.commons.lang.StringUtils;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.core.I18nUtil;
import org.dspace.webmvc.utils.DSpaceRequestUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class LicenseEditController {
    @ModelAttribute("licenseForm")
    public LicenseForm createForm(Context context) {
        LicenseForm licenseForm = new LicenseForm();
        licenseForm.setLicenseText(ConfigurationManager.getLicenseText(I18nUtil.getDefaultLicense(context)));
        return licenseForm;
    }

    @ModelAttribute("context")
    public Context getContext(HttpServletRequest request) {
        return DSpaceRequestUtils.getDSpaceContext(request);
    }

    @RequestMapping
    public String showForm(Context context, LicenseForm licenseForm) throws AuthorizeException {
        if (context == null || context.getCurrentUser() == null) {
            throw new AuthorizeException("Not Authorized to edit the license");
        }

        return "pages/admin/license";
    }

    @RequestMapping(params = "submit")
    public String processForm(Context context, @Valid LicenseForm licenseForm, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            ConfigurationManager.writeLicenseFile(I18nUtil.getDefaultLicense(context), licenseForm.getLicenseText());
            return "pages/admin/license-saved";
        }

        return "pages/admin/license";
    }


    public static class LicenseForm {
        @NotEmpty
        private String licenseText;

        public String getLicenseText() {
            return licenseText;
        }

        public void setLicenseText(String pLicenseText) {
            this.licenseText = pLicenseText;
        }
    }
}