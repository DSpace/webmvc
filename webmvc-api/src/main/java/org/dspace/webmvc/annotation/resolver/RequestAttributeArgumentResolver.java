package org.dspace.webmvc.annotation.resolver;

import org.dspace.webmvc.annotation.RequestAttribute;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.lang.annotation.Annotation;

public class RequestAttributeArgumentResolver implements WebArgumentResolver {
	public Object resolveArgument(MethodParameter methodParameter,
			NativeWebRequest webRequest) throws Exception {

		String attributeName = null;

		Object paramAnn = null;

		Annotation[] paramAnns = null;

		try {
            paramAnns = (Annotation[])methodParameter.getParameterAnnotations();
		}
		catch (Exception e) {
			throw new IllegalStateException("Failed to introspect method parameter annotations", e);
		}

		for (int j = 0; j < paramAnns.length; j++) {
			paramAnn = paramAnns[j];
			attributeName = getAttribute(paramAnn);
            if ("".equals(attributeName)) {
                attributeName = methodParameter.getParameterName();
            }

			if (attributeName != null) {
				break;
			}
		}

		if (attributeName == null) {
			return UNRESOLVED;
		}

		Object value = getValue(webRequest, attributeName);

		if (paramAnn != null) {
			checkValue(value, paramAnn);
		}

		return value;
	}

	/**
	 * Can be used to validate value based on annotation contents. For example, to check if value is required
	 * @param value the value returned from the request
	 * @param paramAnn matched annotation
	 */
	protected void checkValue(Object value, Object paramAnn) {
	}

	protected String getAttribute(Object paramAnn) {
		String attributeName = null;
		if (RequestAttribute.class.isInstance(paramAnn)) {
			RequestAttribute attribute = (RequestAttribute) paramAnn;
			attributeName = attribute.value();
		}
		return attributeName;
	}

	protected Object getValue(NativeWebRequest webRequest, String attributeName) {
		Object attribute = webRequest.getAttribute(attributeName, WebRequest.SCOPE_REQUEST);
		return attribute;
	}
}

