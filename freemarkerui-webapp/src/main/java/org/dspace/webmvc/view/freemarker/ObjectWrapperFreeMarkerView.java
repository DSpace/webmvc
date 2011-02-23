package org.dspace.webmvc.view.freemarker;

import freemarker.template.SimpleHash;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Override for Spring 3.x to ensure that the correct BeansWrapper is passed to the SimpleHash.
 */
public class ObjectWrapperFreeMarkerView extends FreeMarkerView {

    // Uncomment this method if targeting Spring 3.x
/*
	protected SimpleHash buildTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		SimpleHash hash =  super.buildTemplateModel(model, request, response);
		hash.setObjectWrapper(getObjectWrapper());
		return hash;
	}
*/
}