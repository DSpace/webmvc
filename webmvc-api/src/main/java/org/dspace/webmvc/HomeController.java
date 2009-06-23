package org.dspace.webmvc;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for the DSpace homepage
 */
public class HomeController extends AbstractController
{
    /**
     * Handles the model processing required for the home page.
     *
     * TODO: Figure out something useful for this to do.
     * Until then, return an empty ModelAndView to pass control over to the view
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return new ModelAndView("home");
    }
}
