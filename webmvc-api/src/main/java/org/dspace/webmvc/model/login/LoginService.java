package org.dspace.webmvc.model.login;

import org.dspace.core.Context;
import org.dspace.eperson.EPerson;

public interface LoginService {
    void createUserSession(Context context, EPerson person);

    public String getInterruptedRequestURL();
}
