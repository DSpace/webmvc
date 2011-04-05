package org.dspace.webmvc.bind.support;

import org.dspace.core.Context;
import org.dspace.webmvc.utils.DSpaceRequestUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Custom argument resolver, to bind specific DSpace objects to controller method parameters
 *
 * This allows us to return objects created in specific ways, without needing to annotate the parameters
 * Otherwise, Spring may just decide to create them for us!!
 */
public class DSpaceArgumentResolver implements WebArgumentResolver {
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

        // Check if the parameter is a DSpace Context
        if (methodParameter.getParameterType().isAssignableFrom(Context.class)) {
            // Retrieve the DSpace context from the request scope (will have been created by the request filter)
            return DSpaceRequestUtils.getDSpaceContext(webRequest);
        }

        return UNRESOLVED;
	}
}
