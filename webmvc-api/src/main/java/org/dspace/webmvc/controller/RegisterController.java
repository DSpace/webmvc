package org.dspace.webmvc.controller;

import com.sun.mail.smtp.SMTPAddressFailedException;
import org.apache.commons.lang.StringUtils;
import org.dspace.authenticate.AuthenticationManager;
import org.dspace.core.*;
import org.dspace.eperson.AccountManager;
import org.dspace.eperson.EPerson;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Past;
import java.io.IOException;
import java.util.Hashtable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.dspace.webmvc.bind.annotation.RequestAttribute;

import java.sql.SQLException;

import org.springframework.ui.ModelMap;

import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class RegisterController {

    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(RegisterController.class);

    /**
     * The "enter e-mail" step
     */
    public static final int ENTER_EMAIL_PAGE = 1;

    /**
     * The "enter personal info" page, for a registering user
     */
    public static final int PERSONAL_INFO_PAGE = 2;

    /**
     * The simple "enter new password" page, for user who's forgotten p/w
     */
    public static final int NEW_PASSWORD_PAGE = 3;

    /**
     * true = registering users, false = forgotten passwords
     */
    private boolean registering;

    /**
     * ldap is enabled To change this, go edit dspace.cfg in installation drive and have netbeans point to it
     * By default is true. LDAP allows the user to decide if they want registration token
     */
    private boolean ldap_enabled = ConfigurationManager.getBooleanProperty("ldap.enable");

    //private ModelMap model;

    private Context context = null;


    @RequestMapping("/register/**")
    protected String register(@RequestAttribute Context context, ModelMap model, @RequestParam(value="step",required=false, defaultValue = "0") int step,
                              HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        // set all incoming encoding to UTF-8
        request.setCharacterEncoding("UTF-8");

        String token = request.getParameter("token");
        //ldap_enabled = ConfigurationManager.getBooleanProperty("ldap.enable");
        request.getSession().setAttribute("register", "true");

        if (request.getMethod().equals("POST")) {
            return submit(context, model, step, request, response);
        }

        if (token == null) { //First registration step: Key in email
            if (ldap_enabled) {
                return "pages/register/new-ldap-user";
            }
            
            return "pages/register/new-user";

        }//end if (token==null)
        else {


            // We have a token. Find out who the it's for
            String email = AccountManager.getEmail(context, token);

            EPerson eperson = null;

            if (email != null) {
                eperson = EPerson.findByEmail(context, email);

                if (eperson != null) {

                    model.addAttribute("eperson", eperson);
                    String lastName = eperson.getLastName();
                    String firstName = eperson.getFirstName();
                    String phone = eperson.getMetadata("phone");
                    //Locale localeVariable = UIUtil.getSessionLocale(request);

                    if (phone == null) phone = "";
                    String language = eperson.getMetadata("language");
                    if (language == null) language = "";

                    firstName = Utils.addEntities(firstName);
                    lastName = Utils.addEntities(lastName);
                    phone = Utils.addEntities("phone");
                    language = Utils.addEntities("language");

                    model.addAttribute("firstName", firstName);
                    model.addAttribute("lastName", lastName);
                    model.addAttribute("phone", phone);
                    model.addAttribute("language", language);
                    //model.addAttribute("localeVariable", localeVariable);

                }//end if 

            }//end if email != null

            // Both forms need an EPerson object (if any)
            //request.setAttribute("eperson", eperson);


            // And the token
            //request.setAttribute("token", token);
            model.addAttribute("token", token);
            Locale[] supportedLocales = I18nUtil.getSupportedLocales();
            model.addAttribute("supportedLocales", supportedLocales);

            if (email != null) {
                // Indicate if user can set password
                boolean setPassword = AuthenticationManager.allowSetPassword(context, request, email);
                //addAttribute requires a string, object pair
                model.addAttribute("setpassword", Boolean.valueOf(setPassword));
                return "pages/register/registration-form";
            } else {// Duff token!
                return "pages/register/invalid-token";
            }

        }//end else

    }//end doregister

    @RequestMapping("/forgot/**")
    protected String forgot(@RequestAttribute Context context, ModelMap model, @RequestParam(value="step",required=false, defaultValue = "0") int step,
                            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        // set all incoming encoding to UTF-8
        request.setCharacterEncoding("UTF-8");
        String token = request.getParameter("token");
        request.getSession().setAttribute("register", null);

        if (request.getMethod().equals("POST")) {
            return submit(context, model, step, request, response);
        }

        if (token == null) {
            return "pages/register/forgot-password";
        } else {

            // We have a token. Find out who the it's for
            String email = AccountManager.getEmail(context, token);
            EPerson eperson = null;

            if (email != null) {
                eperson = EPerson.findByEmail(context, email);
            }

            // Both forms need an EPerson object (if any)
            request.setAttribute("eperson", eperson);

            // And the token
            request.setAttribute("token", token);

            if (eperson != null) {


                return "pages/register/new-password";

            } else {

                return "pages/register/invalid-token";

            }//end else
        }//end else token
    }

    protected String submit(@RequestAttribute Context context, ModelMap model, int step, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        String returnPath = "pages/home";

        switch (step) {
            case ENTER_EMAIL_PAGE:
                returnPath = processEnterEmail(context, model, request, response);
                break;

            case PERSONAL_INFO_PAGE:
                returnPath = processPersonalInfo(context, model, request, response);

                break;

            case NEW_PASSWORD_PAGE:
                returnPath = processNewPassword(context, model, request, response);
                break;

            default:
                // TODO Add integrity error in MVC style - remove the code below
                // log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                // JSPManager.showIntegrityError(request, response);
        }

        //view resolver cannot deal with a String object
        return returnPath;
        // return "pages/home";
    }

    private String processEnterEmail(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException, SQLException, AuthorizeException {

        Object checkRegister = request.getSession().getAttribute("register");
        if (checkRegister == null) {
            registering = false;
        } else {
            registering = true;
            request.getSession().setAttribute("request", null);
        }

        String email = request.getParameter("email");

        if (email == null || email.length() > 64) {
            // Malformed request or entered value is too long.
            email = "";
        } else {
            email = email.toLowerCase().trim();
        }

        String netid = request.getParameter("netid");
        String password = request.getParameter("password");

        model.addAttribute("netid", netid);
        model.addAttribute("password", password);
        model.addAttribute("email", email);

        EPerson eperson = EPerson.findByEmail(context, email);

        EPerson eperson2 = null;
        if (netid != null) {
            eperson2 = EPerson.findByNetid(context, netid.toLowerCase());
        }

        try {
            if (registering) {
                // If an already-active user is trying to register, inform them so
                if ((eperson != null && eperson.canLogIn()) || (eperson2 != null && eperson2.canLogIn())) {
                    log.info(LogManager.getHeader(context, "already_registered", "email=" + email));
                    return "pages/register/already-registered";
                } else {
                    // Find out from site authenticator whether this email can
                    // self-register
                    boolean canRegister = AuthenticationManager.canSelfRegister(context, request, email);

                    if (canRegister) {
                        //-- registering by email
                        if ((!ldap_enabled) || (netid == null) || (netid.trim().equals(""))) {
                            // OK to register.  Send token.
                            log.info(LogManager.getHeader(context, "sendtoken_register", "email=" + email));

                            try {
                                AccountManager.sendRegistrationInfo(context, email);
                            } catch (javax.mail.SendFailedException e) {
                                if (e.getNextException() instanceof SMTPAddressFailedException) {
                                    // If we reach here, the email is email is invalid for the SMTP server (i.e. fbotelho).
                                    log.info(LogManager.getHeader(context, "invalid_email", "email=" + email));
                                    model.addAttribute("retry", Boolean.TRUE);
                                    return "pages/register/new-user";
                                } else {
                                    throw e;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            context.complete();
                            return "pages/register/registration-sent";
                        } else {
                            //-- registering by netid
                            //--------- START LDAP AUTH SECTION -------------
                            if (password != null && !password.equals("")) {
                                //int returnValue = AuthenticationManager.authenticate(context, netid, password, null, request);
                                //if(returnValue!=AuthenticationManager.)

                                String ldap_provider_url = ConfigurationManager.getProperty("ldap.provider_url");
                                String ldap_id_field = ConfigurationManager.getProperty("ldap.id_field");
                                String ldap_search_context = ConfigurationManager.getProperty("ldap.search_context");

                                // Set up environment for creating initial context
                                Hashtable env = new Hashtable(11);
                                env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                                env.put(javax.naming.Context.PROVIDER_URL, ldap_provider_url);

                                // Authenticate 
                                env.put(javax.naming.Context.SECURITY_AUTHENTICATION, "simple");
                                env.put(javax.naming.Context.SECURITY_PRINCIPAL, ldap_id_field + "=" + netid + "," + ldap_search_context);
                                env.put(javax.naming.Context.SECURITY_CREDENTIALS, password);

                                try {
                                    // Create initial context
                                    DirContext ctx = new InitialDirContext(env);

                                    // Close the context when we're done
                                    ctx.close();
                                } catch (NamingException e) {
                                    // If we reach here, supplied email/password was duff.
                                    log.info(LogManager.getHeader(context, "failed_login", "netid=" + netid + e));
                                    return "pages/login/ldap-incorrect";
                                }
                            }
                            //--------- END LDAP AUTH SECTION -------------
                            Locale[] supportedLocales = I18nUtil.getSupportedLocales();
                            model.addAttribute("supportedLocales", supportedLocales);
                            return "pages/register/registration-form";
                        }
                    } else {
                        return "pages/register/cannot-register";
                    }
                }
            } else {
                if (eperson == null) {
                    // Invalid email address
                    log.info(LogManager.getHeader(context, "unknown_email", "email=" + email));

                    //request.setAttribute("retry", Boolean.TRUE);
                    model.addAttribute("retry", Boolean.TRUE);

                    /*JSPManager.showJSP(request, response,
                    "/register/forgot-password.jsp");*/

                    return "pages/register/forgot-password";
                } else if (!eperson.canLogIn()) {

                    // Can't give new password to inactive user
                    log.info(LogManager.getHeader(context, "unregistered_forgot_password", "email=" + email));

                    return "pages/register/inactive-account";
                } else if (eperson.getRequireCertificate() && !registering) {

                    // User that requires certificate can't get password
                    log.info(LogManager.getHeader(context, "certificate_user_forgot_password", "email=" + email));

                    return "pages/error/require-certificate";
                } else {
                    // OK to send forgot pw token.
                    log.info(LogManager.getHeader(context, "sendtoken_forgotpw", "email=" + email));

                    AccountManager.sendForgotPasswordInfo(context, email);

                    // Context needs completing to write registration data
                    context.complete();
                    return "pages/register/password-token-sent";
                }
            }
        } catch (AddressException ae) {
            // Malformed e-mail address
            log.info(LogManager.getHeader(context, "bad_email", "email="
                    + email));

            //request.setAttribute("retry", Boolean.TRUE);
            model.addAttribute("retry", Boolean.TRUE);

            if (registering) {
                if (ldap_enabled) {
                    //JSPManager.showJSP(request, response, "/register/new-ldap-user.jsp");
                    return "pages/register/new-ldap-user";
                } else {
                    //JSPManager.showJSP(request, response, "/register/new-user.jsp");
                    return "pages/register/new-user";
                }
            } else {
                //JSPManager.showJSP(request, response, "/register/forgot-password.jsp");
                return "pages/register/forgot-password";
            }
        } catch (MessagingException me) {
            // Some other mailing error
            log.info(LogManager.getHeader(context, "error_emailing", "email=" + email), me);

            // TODO Add internal error handling - probably should just throw an exception
            // JSPManager.showInternalError(request, response);
        }

        return "";
    }//end process email

    private String processPersonalInfo(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException, SQLException, AuthorizeException {
        
        // Get the token
        String token = request.getParameter("token");
        String email = AccountManager.getEmail(context, token);
        String netid = request.getParameter("netid");
        
        if ((netid != null) && (email == null)) {
            email = request.getParameter("email");
        }

        // If the token isn't valid, show an error
        if (email == null && netid == null) {
            log.info(LogManager.getHeader(context, "invalid_token", "token=" + token));

            return "pages/register/invalid-token";
        }

        // If the token is valid, we create an eperson record if need be
        EPerson eperson = null;
        if (email != null) {
            eperson = EPerson.findByEmail(context, email);
        }
        EPerson eperson2 = null;
        if (netid != null) {
            eperson2 = EPerson.findByNetid(context, netid.toLowerCase());
        }
        if (eperson2 != null) {
            eperson = eperson2;
        }
                
        if (eperson == null) {
            // Need to create new eperson
            // FIXME: TEMPORARILY need to turn off authentication, as usually
            // only site admins can create e-people
            context.turnOffAuthorisationSystem();
            
            eperson = EPerson.create(context);
            eperson.setEmail(email);

            if (netid != null) {
                eperson.setNetid(netid.toLowerCase());
                
            }
            
            eperson.update();
            context.restoreAuthSystemState();
        }

        // Now set the current user of the context
        // to the user associated with the token, so they can update their
        // info
        context.setCurrentUser(eperson);
        
        // Set the user profile info
        boolean infoOK = updateUserProfile(eperson, request);

        eperson.setCanLogIn(true);
        eperson.setSelfRegistered(true);

        // Give site auth a chance to set/override appropriate fields
        AuthenticationManager.initEPerson(context, request, eperson);

        // If the user set a password, make sure it's OK
        boolean passwordOK = true;
        if (!eperson.getRequireCertificate() && netid == null && AuthenticationManager.allowSetPassword(context, request, eperson.getEmail())) {
            passwordOK = confirmAndSetPassword(eperson, request);
        }

        if (infoOK && passwordOK) {
            // All registered OK.
            log.info(LogManager.getHeader(context, "usedtoken_register", "email=" + eperson.getEmail()));

            // delete the token
            if (token != null) {
                AccountManager.deleteToken(context, token);
            }

            // Update user record
            eperson.update();

            // request.setAttribute("eperson", eperson);
            model.addAttribute("eperson", eperson);
            model.addAttribute("epersonName", eperson.getFirstName());

            //JSPManager.showJSP(request, response, "/register/registered.jsp");
            context.complete();
            
            return "pages/register/registered";
        } else {
            request.setAttribute("token", token);
            request.setAttribute("eperson", eperson);
            request.setAttribute("netid", netid);
            request.setAttribute("missingfields", Boolean.valueOf(!infoOK));
            request.setAttribute("passwordproblem", Boolean.valueOf(!passwordOK));

            // Indicate if user can set password
            boolean setPassword = AuthenticationManager.allowSetPassword(context, request, email);
            model.addAttribute("setpassword", Boolean.valueOf(setPassword));

            // Changes to/creation of e-person in DB cancelled
            context.abort();
            
            return "pages/register/registration-form";
        }
    }

    private String processNewPassword(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException, SQLException, AuthorizeException {

        // Get the token
        String token = request.getParameter("token");

        // Get the eperson associated with the password change
        EPerson eperson = AccountManager.getEPerson(context, token);

        // If the token isn't valid, show an error
        if (eperson == null) {
            log.info(LogManager.getHeader(context, "invalid_token", "token=" + token));

            return "pages/register/invalid-token";
        }

        // If the token is valid, we set the current user of the context
        // to the user associated with the token, so they can update their
        // info
        context.setCurrentUser(eperson);

        // Confirm and set the password
        boolean passwordOK = confirmAndSetPassword(eperson, request);

        if (passwordOK) {
            log.info(LogManager.getHeader(context, "usedtoken_forgotpw", "email=" + eperson.getEmail()));

            eperson.update();
            AccountManager.deleteToken(context, token);

            context.complete();
            return "pages/register/password-changed";
        } else {
            model.addAttribute("passwordproblem", Boolean.TRUE);
            model.addAttribute("token", token);
            model.addAttribute("eperson", eperson);

            return "pages/register/new-password";
        }
    }

    /**
     * Update a user's profile information with the information in the given
     * request. This assumes that authentication has occurred. This method
     * doesn't write the changes to the database (i.e. doesn't call update.)
     *
     * @param eperson the e-person
     * @param request the request to get values from
     * @return true if the user supplied all the required information, false if
     *         they left something out.
     */
    public static boolean updateUserProfile(EPerson eperson, HttpServletRequest request) {
        // Get the parameters from the form
        String lastName = request.getParameter("last_name");
        String firstName = request.getParameter("first_name");
        String phone = request.getParameter("phone");
        String language = request.getParameter("language");

        // Update the eperson
        eperson.setFirstName(firstName);
        eperson.setLastName(lastName);
        eperson.setMetadata("phone", phone);
        eperson.setLanguage(language);

        // Check all required fields are there
        return (!StringUtils.isEmpty(lastName) && !StringUtils.isEmpty(firstName));
    }

    /**
     * Set an eperson's password, if the passwords they typed match and are
     * acceptible. If all goes well and the password is set, null is returned.
     * Otherwise the problem is returned as a String.
     *
     * @param eperson the eperson to set the new password for
     * @param request the request containing the new password
     * @return true if everything went OK, or false
     */
    public static boolean confirmAndSetPassword(EPerson eperson, HttpServletRequest request) {
        // Get the passwords
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("password_confirm");

        // Check it's there and long enough
        if ((password == null) || (password.length() < 6)) {
            return false;
        }

        // Check the two passwords entered match
        if (!password.equals(passwordConfirm)) {
            return false;
        }

        // Everything OK so far, change the password
        eperson.setPassword(password);

        return true;
    }
}
    
   
    
    
    
    

