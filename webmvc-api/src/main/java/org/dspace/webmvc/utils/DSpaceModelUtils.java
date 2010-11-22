package org.dspace.webmvc.utils;

import org.dspace.webmvc.view.helpers.NavigationHelper;
import org.springframework.web.servlet.ModelAndView;

public class DSpaceModelUtils {

    public static void addNavigationHelpers(ModelAndView mav) {
        mav.addObject("navigation", new NavigationHelper());
    }
}
