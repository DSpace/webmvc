package org.dspace.webmvc.bind.support;

import org.apache.commons.lang.StringUtils;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.lang.annotation.Annotation;

/**
 * Custom argument resolver to bind request attributes to parameters in controller methods.
 *
 * To use, simply annotate the parameter you wish to be bound to a request parameter with @RequestAttribute
 *
 * The value parameter is optional - if specified, it is the name of the attribute to bind.
 * Otherwise, it will use the name of the parameter.
 */
public class RequestAttributeArgumentResolver implements WebArgumentResolver {
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

        // Iterate through the annotations of the parameter (we will only bind annotated parameters)
        for (Annotation paramAnn : (Annotation[])methodParameter.getParameterAnnotations()) {
            String attributeName = null;

            // If the annotation is RequestAttribute
            if (RequestAttribute.class.isInstance(paramAnn)) {

                // Get the value parameter from the annotation as the name of the attribute to bind
                RequestAttribute attribute = (RequestAttribute) paramAnn;
                attributeName = attribute.value();

                // If we don't have an attribute name, use the name of the parameter
                if (StringUtils.isEmpty(attributeName)) {
                    attributeName = methodParameter.getParameterName();
                }

                // Return the named request attribute
                if (!StringUtils.isEmpty(attributeName)) {
                    return webRequest.getAttribute(attributeName, WebRequest.SCOPE_REQUEST);
                }
            }
		}

        return UNRESOLVED;
	}
}

