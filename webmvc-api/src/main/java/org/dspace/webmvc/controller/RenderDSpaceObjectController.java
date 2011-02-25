package org.dspace.webmvc.controller;

import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.webmvc.model.TrailEntry;
import org.dspace.webmvc.utils.DSpaceRequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RenderDSpaceObjectController {
    @RequestMapping("/renderObject")
    public String renderObject(ModelMap model, HttpServletRequest request) throws SQLException {
        DSpaceObject dso = DSpaceRequestUtils.getScopeObject(request);

        List<TrailEntry> trailList = buildTrail(dso.getParentObject());
        trailList.add(TrailEntry.createWithDSpaceObject(dso));
        model.addAttribute("trailList", trailList);

        if (dso != null) {
            switch (dso.getType()) {
                case Constants.COLLECTION:
                    model.addAttribute("collection", (Collection) dso);
                    return "pages/collection";

                case Constants.COMMUNITY:
                    model.addAttribute("community", (Community) dso);
                    return "pages/community";

                case Constants.ITEM:
                    model.addAttribute("item", (Item) dso);
                    return "pages/item";

                default:
                    return "pages/unknowntype";
            }
        }

        return "pages/invalidhandle";
    }

    private List<TrailEntry> buildTrail(DSpaceObject dso) throws SQLException {
        if (dso != null) {
            List<TrailEntry> trail = buildTrail(dso.getParentObject());
            trail.add(TrailEntry.createWithDSpaceObject(dso));
            return trail;
        }

        return new ArrayList<TrailEntry>();
    }
}
