package org.dspace.webmvc.bind.support;

import org.dspace.core.Context;
import org.dspace.webmvc.utils.DSpaceRequestUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

public class DSpaceArgumentResolver implements WebArgumentResolver {
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
        if (methodParameter.getParameterType().isAssignableFrom(Context.class)) {
            return DSpaceRequestUtils.getDSpaceContext(webRequest);
        }

        return UNRESOLVED;
	}
}
