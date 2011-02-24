package org.dspace.webmvc.controller;

import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.webmvc.utils.DSpaceRequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RenderDSpaceObjectController {
    @RequestMapping("/renderObject")
    public String renderObject(ModelMap model, HttpServletRequest request) {
        DSpaceObject dso = DSpaceRequestUtils.getScopeObject(request);

        if (dso != null) {
            switch (dso.getType()) {
                case Constants.COLLECTION:
                    model.addAttribute("collection", (Collection)dso);
                    return "pages/collection";

                case Constants.COMMUNITY:
                    model.addAttribute("community", (Community)dso);
                    return "pages/community";

                case Constants.ITEM:
                    model.addAttribute("item", (Item)dso);
                    return "pages/item";

                default:
                    return "pages/unknowntype";
            }
        }

        return "pages/invalidhandle";
    }
}
