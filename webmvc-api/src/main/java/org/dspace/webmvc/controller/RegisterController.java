/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller;

import com.sun.mail.smtp.SMTPAddressFailedException;
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
import java.io.IOException;
import java.util.Hashtable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import java.sql.SQLException;
import org.springframework.ui.ModelMap;

import org.apache.log4j.Logger;
import org.dspace.app.webui.util.Authenticate;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.app.webui.util.UIUtil;
import java.util.Locale;

/**
 *
 * @author AdminNUS
 */
@Controller
public class RegisterController{
    
    /** Logger */
    private static Logger log = Logger.getLogger(RegisterController.class);

    /** The "enter e-mail" step */
    public static final int ENTER_EMAIL_PAGE = 1;

    /** The "enter personal info" page, for a registering user */
    public static final int PERSONAL_INFO_PAGE = 2;

    /** The simple "enter new password" page, for user who's forgotten p/w */
    public static final int NEW_PASSWORD_PAGE = 3;

    /** true = registering users, false = forgotten passwords */
    private boolean registering;

    /** ldap is enabled To change this, go edit dspace.cfg in installation drive and have netbeans point to it
     By default is true. LDAP allows the user to decide if they want registration token */
    private boolean ldap_enabled = ConfigurationManager.getBooleanProperty("ldap.enable");
    
    //private ModelMap model;
    
    private Context context = null;

    
    
    protected String register(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {                      
                
                
                System.out.println(request.getParameter("email"));
                
                
                String token = request.getParameter("token");
                //ldap_enabled = ConfigurationManager.getBooleanProperty("ldap.enable");
                request.getSession().setAttribute("register", "true");
                if(token==null){ //First registration step: Key in email
                        
                if (ldap_enabled)
                {
                    //return "pages/register/new-ldap-user";
                }
                    return "pages/register/new-user";
                                     
                }//end if (token==null)
                else{
                
                    System.out.println("we are in token else");
                    // We have a token. Find out who the it's for
            String email = AccountManager.getEmail(context, token);

            EPerson eperson = null;

            if (email != null)
            {
                eperson = EPerson.findByEmail(context, email);
                
                if(eperson!=null){
                    
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
   
            if (email != null)
            {
                // Indicate if user can set password
                boolean setPassword = AuthenticationManager.allowSetPassword(context, request, email);
                //addAttribute requires a string, object pair
                model.addAttribute("setpassword", Boolean.valueOf(setPassword)); 
                return "pages/register/registration-form";
            }
            else
            {// Duff token! 
                return "pages/register/invalid-token";
            }
                
          }//end else                              
                             
    }//end doregister
    
    
    protected String forgot(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException{
        
            String token = request.getParameter("token");
            
            if(token==null){
            
                return "pages/register/forgot-password";
            }
            else{
                
            // We have a token. Find out who the it's for
            String email = AccountManager.getEmail(context, token);
            EPerson eperson = null;

            if (email != null)
            {
                eperson = EPerson.findByEmail(context, email);
            }

            // Both forms need an EPerson object (if any)
            request.setAttribute("eperson", eperson);

            // And the token
            request.setAttribute("token", token);
            
            if(eperson != null){
                
                return "pages/register/new-password";
                
            }
            else{
                
                return "pages/register/invalid-token";
                
            }//end else
            }//end else token
        
        
    }
    
    
    protected String submit(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException{
        
        String returnPath = "pages/home";      
        // First get the step
        int step = UIUtil.getIntParameter(request, "step");
        
        System.out.println(step);
           
        switch (step)
        {
            case ENTER_EMAIL_PAGE:       
            returnPath = processEnterEmail(context, model, request, response);
            break;
                
            case PERSONAL_INFO_PAGE:
            returnPath = processPersonalInfo(context, model, request, response);
            System.out.println("This is returnPath " + returnPath);

            break;

            case NEW_PASSWORD_PAGE:
            returnPath = processNewPassword(context, model, request, response);

            break;    
                       
            default:
            log.warn(LogManager.getHeader(context, "integrity_error", UIUtil
                    .getRequestLogInfo(request)));
            JSPManager.showIntegrityError(request, response);
        }//end switch
        
        //view resolver cannot deal with a String object
        return returnPath;
       // return "pages/home";
    }//end submit        
    
    private String processEnterEmail(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        
        Object checkRegister = request.getSession().getAttribute("register"); 
        if(checkRegister==null){
            registering = false; 
        }//end if
        else{
            registering = true;
            //System.out.println("registering is true");
            request.getSession().setAttribute("request", null);
        }
        
        
        String email = request.getParameter("email");
        
        //System.out.println(email);
        
        if (email == null || email.length() > 64)
        {
        	// Malformed request or entered value is too long.
        	email = "";
        }
        else
        {
        	email = email.toLowerCase().trim();
        }
        
        String netid = request.getParameter("netid");
        String password = request.getParameter("password");
        EPerson eperson = EPerson.findByEmail(context, email);
        EPerson eperson2 = null;
        if (netid!=null)
        {
            eperson2 = EPerson.findByNetid(context, netid.toLowerCase());
        }

        try
        {
            if (registering)
            {
                // If an already-active user is trying to register, inform them so
                if ((eperson != null && eperson.canLogIn()) || (eperson2 != null && eperson2.canLogIn()))
                {
                    log.info(LogManager.getHeader(context,
                            "already_registered", "email=" + email));
         
                    return "pages/register/already-registered";
                   
                }
                else
                {
                    // Find out from site authenticator whether this email can
                    // self-register
                    boolean canRegister =
                        AuthenticationManager.canSelfRegister(context, request, email);

                    if (canRegister)
                    {
                        //System.out.println("we can register");
                        //-- registering by email
                        if ((!ldap_enabled)||(netid==null)||(netid.trim().equals("")))
                        {
                            
                            //System.out.println("we are new user");
                            // OK to register.  Send token.
                            log.info(LogManager.getHeader(context,
                                "sendtoken_register", "email=" + email));

                            try
                            {
                                //System.out.println("sending registration...");
                                AccountManager.sendRegistrationInfo(context, email);
                                //System.out.println("registration info out !");
                            }
                            catch (javax.mail.SendFailedException e)
                            {
                            	if (e.getNextException() instanceof SMTPAddressFailedException)
                            	{
                                    // If we reach here, the email is email is invalid for the SMTP server (i.e. fbotelho).
                                    log.info(LogManager.getHeader(context,
                                        "invalid_email",
                                        "email=" + email));
                                    //request.setAttribute("retry", Boolean.TRUE);
                                    model.addAttribute("retry", Boolean.TRUE);
                                    //JSPManager.showJSP(request, response, "/register/new-user.jsp");
                                    return "pages/register/new-user";
                            	}
                            	else
                            	{
                            	     
                                     throw e;
                            	}
                            }
                            catch(Exception ex){
                                
                                ex.printStackTrace();
                            }
                            
                            context.complete();
                            return "pages/register/registration-sent";
                            /*JSPManager.showJSP(request, response,
                                "/register/registration-sent.jsp");*/

                            // Context needs completing to write registration data
                            
                        }
                        //-- registering by netid
                        else 
                        {
                            //--------- START LDAP AUTH SECTION -------------
                            if (password!=null && !password.equals("")) 
                            {
                                String ldap_provider_url = ConfigurationManager.getProperty("ldap.provider_url");
                                String ldap_id_field = ConfigurationManager.getProperty("ldap.id_field");
                                String ldap_search_context = ConfigurationManager.getProperty("ldap.search_context");
                           
                                // Set up environment for creating initial context
                                Hashtable env = new Hashtable(11);
                                env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                                env.put(javax.naming.Context.PROVIDER_URL, ldap_provider_url);
                        
                                // Authenticate 
                                env.put(javax.naming.Context.SECURITY_AUTHENTICATION, "simple");
                                env.put(javax.naming.Context.SECURITY_PRINCIPAL, ldap_id_field+"="+netid+","+ldap_search_context);
                                env.put(javax.naming.Context.SECURITY_CREDENTIALS, password);
                        
                                try {
                                   // Create initial context
                                   DirContext ctx = new InitialDirContext(env);
             
                                   // Close the context when we're done
                                   ctx.close();
                                } 
                                catch (NamingException e) 
                                {
                                    // If we reach here, supplied email/password was duff.
                                    log.info(LogManager.getHeader(context,
                                        "failed_login",
                                        "netid=" + netid + e));
                                    //JSPManager.showJSP(request, response, "/login/ldap-incorrect.jsp");
                                    return "pages/login/ldap-incorrect";
                                }
                            }
                            //--------- END LDAP AUTH SECTION -------------
                            // Forward to "personal info page"
                            //JSPManager.showJSP(request, response, "/register/registration-form.jsp");
                            return "pages/register/registration-form"; 
                        }
                    }
                    else
                    {
                        /*JSPManager.showJSP(request, response,
                            "/register/cannot-register.jsp");*/
                        
                        return "pages/register/cannot-register";
                    }
                }
            }
            else
            {
                if (eperson == null)
                {
                    // Invalid email address
                    log.info(LogManager.getHeader(context, "unknown_email",
                            "email=" + email));

                    //request.setAttribute("retry", Boolean.TRUE);
                    model.addAttribute("retry", Boolean.TRUE); 

                    /*JSPManager.showJSP(request, response,
                            "/register/forgot-password.jsp");*/
                    
                    return "pages/register/forgot-password"; 
                }
                else if (!eperson.canLogIn())
                {
                    // Can't give new password to inactive user
                    log.info(LogManager.getHeader(context,
                            "unregistered_forgot_password", "email=" + email));

                    /*JSPManager.showJSP(request, response,
                            "/register/inactive-account.jsp");*/
                    
                    return "pages/register/inactive-account"; 
                }
                else if (eperson.getRequireCertificate() && !registering)
                {
                    // User that requires certificate can't get password
                    log.info(LogManager.getHeader(context,
                            "certificate_user_forgot_password", "email="
                                    + email));

                    /*JSPManager.showJSP(request, response,
                            "/error/require-certificate.jsp");*/
                    
                    return "pages/error/require-certificate"; 
                    
                    
                }
                else
                {
                    // OK to send forgot pw token.
                    log.info(LogManager.getHeader(context,
                            "sendtoken_forgotpw", "email=" + email));

                    AccountManager.sendForgotPasswordInfo(context, email);
                    /*JSPManager.showJSP(request, response,
                            "/register/password-token-sent.jsp");*/

                    // Context needs completing to write registration data
                    context.complete();
                    return "pages/register/password-token-sent"; 
                }
            }
        }
        catch (AddressException ae)
        {
            // Malformed e-mail address
            log.info(LogManager.getHeader(context, "bad_email", "email="
                    + email));

            //request.setAttribute("retry", Boolean.TRUE);
            model.addAttribute("retry", Boolean.TRUE); 

            if (registering)
            {
                if (ldap_enabled)
                {
                    //JSPManager.showJSP(request, response, "/register/new-ldap-user.jsp");
                    return "pages/register/new-ldap-user"; 
                }
                else
                {
                    //JSPManager.showJSP(request, response, "/register/new-user.jsp");
                    return "pages/register/new-user";
                }
            }
            else
            {
                //JSPManager.showJSP(request, response, "/register/forgot-password.jsp");
                return "pages/register/forgot-password"; 
            }
        }
        catch (MessagingException me)
        {
            // Some other mailing error
            log.info(LogManager.getHeader(context, "error_emailing", "email=" + email), me);

            JSPManager.showInternalError(request, response);
        }
        
        return "";
    }//end process email
    
    private String processPersonalInfo(Context context, ModelMap model,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException,
            AuthorizeException
    {
        // Get the token
        String token = request.getParameter("token");

        // Get the email address
        String email = AccountManager.getEmail(context, token);
        String netid = request.getParameter("netid");
        if ((netid!=null)&&(email==null))
        {
            email = request.getParameter("email");
        }
        
        // If the token isn't valid, show an error
        if (email == null && netid==null)
        {
            log.info(LogManager.getHeader(context, "invalid_token", "token="
                + token));

            // Invalid token
            /*JSPManager
                .showJSP(request, response, "/register/invalid-token.jsp");*/

            return "pages/register/invalid-token";
        }

        // If the token is valid, we create an eperson record if need be
        EPerson eperson = null;
        if (email!=null)
        {
            eperson = EPerson.findByEmail(context, email);
        }
        EPerson eperson2 = null;
        if (netid!=null)
        {
            eperson2 = EPerson.findByNetid(context, netid.toLowerCase());
        }
        if (eperson2 !=null)
        {
            eperson = eperson2;
        }
        
        if (eperson == null)
        {
            // Need to create new eperson
            // FIXME: TEMPORARILY need to turn off authentication, as usually
            // only site admins can create e-people
            context.setIgnoreAuthorization(true);
            eperson = EPerson.create(context);
            eperson.setEmail(email);
            if (netid!=null)
            {
                eperson.setNetid(netid.toLowerCase());
            }
            eperson.update();
            context.setIgnoreAuthorization(false);
        }

        // Now set the current user of the context
        // to the user associated with the token, so they can update their
        // info
        context.setCurrentUser(eperson);

        // Set the user profile info
        boolean infoOK = EditProfileController.updateUserProfile(eperson, request);

        eperson.setCanLogIn(true);
        eperson.setSelfRegistered(true);

        // Give site auth a chance to set/override appropriate fields
        AuthenticationManager.initEPerson(context, request, eperson);

        // If the user set a password, make sure it's OK
        boolean passwordOK = true;
        if (!eperson.getRequireCertificate() && netid==null &&
            AuthenticationManager.allowSetPassword(context, request,
                eperson.getEmail()))
        {
            passwordOK = EditProfileController.confirmAndSetPassword(eperson,
                    request);
        }

        if (infoOK && passwordOK)
        {
            // All registered OK.
            log.info(LogManager.getHeader(context, "usedtoken_register",
                    "email=" + eperson.getEmail()));

            // delete the token
            if (token!=null)
            {
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
            
            
        }
        else
        {
            request.setAttribute("token", token);
            request.setAttribute("eperson", eperson);
            request.setAttribute("netid", netid);
            request.setAttribute("missingfields", Boolean.valueOf(!infoOK));
            request.setAttribute("passwordproblem", Boolean.valueOf(!passwordOK));

            // Indicate if user can set password
            boolean setPassword = AuthenticationManager.allowSetPassword(
                    context, request, email);
            // request.setAttribute("set.password", Boolean.valueOf(setPassword));
            model.addAttribute("setpassword", Boolean.valueOf(setPassword)); 

            /*JSPManager.showJSP(request, response,
                    "/register/registration-form.jsp");*/

            // Changes to/creation of e-person in DB cancelled
            context.abort();
            return "pages/register/registration-form";
        }
    }
    
    private String processNewPassword(Context context, ModelMap model,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException,
            AuthorizeException
    {
        // Get the token
        String token = request.getParameter("token");

        // Get the eperson associated with the password change
        EPerson eperson = AccountManager.getEPerson(context, token);

        // If the token isn't valid, show an error
        if (eperson == null)
        {
            log.info(LogManager.getHeader(context, "invalid_token", "token="
                    + token));

            // Invalid token
            /*JSPManager
                    .showJSP(request, response, "/register/invalid-token.jsp");*/

            return "pages/register/invalid-token";
        }

        // If the token is valid, we set the current user of the context
        // to the user associated with the token, so they can update their
        // info
        context.setCurrentUser(eperson);

        // Confirm and set the password
        boolean passwordOK = EditProfileController.confirmAndSetPassword(eperson,
                request);

        if (passwordOK)
        {
            log.info(LogManager.getHeader(context, "usedtoken_forgotpw",
                    "email=" + eperson.getEmail()));

            eperson.update();
            AccountManager.deleteToken(context, token);

            /*JSPManager.showJSP(request, response,
                    "/register/password-changed.jsp");*/
            context.complete();
            return "pages/register/password-changed";
        }
        else
        {
            /*request.setAttribute("password.problem", Boolean.TRUE);
            request.setAttribute("token", token);
            request.setAttribute("eperson", eperson);*/
            
            model.addAttribute("passwordproblem", Boolean.TRUE);
            model.addAttribute("token", token);
            model.addAttribute("eperson", eperson);

            // JSPManager.showJSP(request, response, "/register/new-password.jsp");
            return "pages/register/new-password";
        }//end else
        
    }//end processNewPassword
    
    @RequestMapping
    private String processRequest(ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        
        // set all incoming encoding to UTF-8
        request.setCharacterEncoding("UTF-8");
        
        String newPassword = request.getParameter("newpassword");  

        // Get the URL from the request immediately, since forwarding
        // loses that information
        UIUtil.storeOriginalURL(request);

        try
        {
            // Obtain a context - either create one, or get the one created by
            // an authentication filter
            context = UIUtil.obtainContext(request);

            // Are we resuming a previous request that was interrupted for
            // authentication?
            request = Authenticate.getRealRequest(request);

            if (log.isDebugEnabled())
            {
                log.debug(LogManager.getHeader(context, "http_request", UIUtil
                        .getRequestLogInfo(request)));
            }

            // Invoke the servlet code
            if (request.getMethod().equals("POST"))
            {
                System.out.println("The request method is post");
                return submit(context, model, request, response);
            }
            else if (newPassword!=null){
                System.out.println("we are in new password");
                return forgot(context, model, request, response);
            }                            
            else
            {
                return register(context, model, request, response);
            }
        }
        catch (SQLException se)
        {
            // For database errors, we log the exception and show a suitably
            // apologetic error
            log.warn(LogManager.getHeader(context, "database_error", se
                    .toString()), se);

            // Also email an alert
            UIUtil.sendAlert(request, se);

            JSPManager.showInternalError(request, response);
        }
        catch (AuthorizeException ae)
        {
            /*
             * If no user is logged in, we will start authentication, since if
             * they authenticate, they might be allowed to do what they tried to
             * do. If someone IS logged in, and we got this exception, we know
             * they tried to do something they aren't allowed to, so we display
             * an error in that case.
             */
            if (context.getCurrentUser() != null ||
                Authenticate.startAuthentication(context, request, response))
            {
                // FIXME: Log the right info?
                // Log the error
                log.info(LogManager.getHeader(context, "authorize_error", ae
                        .toString()));

                JSPManager.showAuthorizeError(request, response, ae);
            }
        }
        finally
        {
            // Abort the context if it's still valid
            if ((context != null) && context.isValid())
            {
                context.abort();
            }
        }
        
        return "";
    }
    
  
    
    
   
    
        

        
    }//end registerController
    
   
    
    
    
    

