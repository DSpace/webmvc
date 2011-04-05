package org.dspace.webmvc.bind.support;

import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.lang.annotation.Annotation;

public class RequestAttributeArgumentResolver implements WebArgumentResolver {
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

        for (Annotation paramAnn : (Annotation[])methodParameter.getParameterAnnotations()) {
            String attributeName = null;
            if (RequestAttribute.class.isInstance(paramAnn)) {
                RequestAttribute attribute = (RequestAttribute) paramAnn;
                attributeName = attribute.value();
            }

            if ("".equals(attributeName)) {
                attributeName = methodParameter.getParameterName();
            }

			if (attributeName != null) {
                return webRequest.getAttribute(attributeName, WebRequest.SCOPE_REQUEST);
			}
		}

        return UNRESOLVED;
	}
}

