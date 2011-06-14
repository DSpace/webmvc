package org.dspace.webmvc.model.login;

import org.dspace.core.Context;
import org.dspace.eperson.EPerson;

public interface LoginService {
    void removeUserSession(Context context);

    void createUserSession(Context context, EPerson person);

    String getInterruptedRequestURL();
}
