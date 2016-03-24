package com.emb.tictactoe.util;

import java.io.Closeable;
import java.util.Map;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.security.Principal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class JsfUtil {

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(JsfUtil.class.getName());
    public static final String WIN = "WIN";
    public static final String DRAW = "DRAW";
    public static final String PLAYING = "PLAYING...";
    public static final String START = "Please select first Player";

    public static void log(String message) {
        LOG.log(Level.INFO, "{0}", message);
    }

    private static boolean ipIsValid(String ip) {
        return null != ip && !"".equals(ip.trim()) && !"unknown".equalsIgnoreCase(ip);
    }

    public static String getIpAddr() {
        HttpServletRequest request = request();
        try {
            String ip = request.getHeader("X-Real-IP");
            if (ipIsValid(ip)) {
                return ip;
            }

            ip = request.getHeader("X-FORWARDED-FOR");
            if (ipIsValid(ip)) {
                int index = ip.indexOf(',');
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            }

            ip = request.getHeader("X-Forwarded-For");
            if (ipIsValid(ip)) {
                int index = ip.indexOf(',');
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            }

            ip = request.getHeader("x-forwarded-for");
            if (ipIsValid(ip)) {
                int index = ip.indexOf(',');
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            }
        } catch (Exception e) {
        }
        return request.getRemoteAddr();
    }

    public static String getResource(String key) {
        try {
            return ResourceBundle.getBundle("/Bundle").getString(key);
        } catch (Exception ex) {
            log(new StringBuilder("@ getResource: Did not find ")
                    .append(key)
                    .toString()
            );
            return "";
        }
    }

    public static String getResource(String key, Object... params) {
        try {
            return MessageFormat.format(ResourceBundle.getBundle("/Bundle").getString(key), params);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    public static boolean isValidationFailed() {
        return facesContext().isValidationFailed();
    }

    public static void getMessages(FacesMessage.Severity severity) {
        String messages = initMessage();
        String message = "";
        int i = 0;
        for (Iterator<FacesMessage> it = facesContext().getMessages(null); it.hasNext();) {
            i++;
            message = (it.next()).getDetail();
            if (isSystemMessage(message)) {
                message = "Operation not allowed. Record has Links";
            }
            messages += i + ". " + (message + "<br/><br/>");
            it.remove();
        }

        addMessage(message, severity);
    }

    private static boolean isSystemMessage(String message) {
        return message != null && !message.equals(" ") && message.equalsIgnoreCase("Transaction marked for rollback");
    }

    private static String initMessage() {
        String messages;
        if (FacesContext.getCurrentInstance().getMessageList().size() > 1) {
            messages = "<br/>Message(s): <br/>";
        } else {
            messages = "<br/>";
        }
        return messages;
    }

    public static void addMessage(String msg, FacesMessage.Severity severity) {
        FacesMessage facesMsg = new FacesMessage(severity, msg, msg);
        facesContext().addMessage(null, facesMsg);
        preserveMessage();
    }

    public static void addWarningMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        facesContext().addMessage(null, facesMsg);
        preserveMessage();
    }

    public static void preserveMessage() {
        facesContext().getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        facesContext().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        facesContext().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return facesContext().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName,
            Converter converter, UIComponent component) {
        String theId = getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static Map getRequestParameterMap() {
        return facesContext().getExternalContext().getRequestParameterMap();
    }

    public static Object getEntity(String var, Class entityClass) {
        return facesContext().getApplication().evaluateExpressionGet(facesContext(), var, entityClass);
    }

    public static boolean Null(Object param) {
        return param == null;
    }

    public static boolean notNull(Object param) {
        return param != null;
    }

    public static boolean notNull(Object param1, Object param2) {
        return notNull(param1) && notNull(param2);
    }

    public static boolean notNull(Object param1, Object param2, Object param3) {
        return notNull(param1, param2) && notNull(param3);
    }

    public static boolean notNull(Object param1, Object param2, Object param3, Object param4) {
        return notNull(param1, param2) && notNull(param3, param4);
    }

    public static boolean notEmpty(List param) {
        return notNull(param) && !param.isEmpty();
    }

    public static boolean notEmpty(String param) {
        return notNull(param) && !param.isEmpty();
    }

    public static boolean Empty(List param) {
        return notNull(param) && param.isEmpty();
    }

    public static boolean Empty(String param) {
        return notNull(param) && param.isEmpty();
    }

    public static void navigate(String url) {
        try {
            facesContext().getApplication().getNavigationHandler()
                    .handleNavigation(FacesContext.getCurrentInstance(), null, url);
        } catch (Exception e) {
            log(new StringBuilder("Exception @ navigate: ")
                    .append(e)
                    .toString()
            );
        }
    }

    public static void redirect(String url) {
        try {
            facesContext().getExternalContext().redirect(url);
        } catch (IOException e) {
            addErrorMessage(new StringBuilder(url)
                    .append(" not valid")
                    .toString());
        }
    }

    public static String assignView() {
        String group;
        if (request().isUserInRole("SYSADMIN")) {
            group = "SYSADMIN";
        } else if (request().isUserInRole("ADMIN")) {
            group = "ADMIN";
        } else if (request().isUserInRole("SELLER")) {
            group = "SELLER";
        } else {
            group = "BUYER";
        }
        return group;
    }

    public static void routeTo(String message, String successPage,
            String errorPage, FacesMessage.Severity severity) {
        try {
            String role = assignView();
            if (notEmpty(message)) {
                addErrorMessage(message);
                getMessages(severity);
            }
            if (role != null) {
                navigate("/" + role.toLowerCase() + ResourceBundle.getBundle("/Bundle").getString(successPage));
            } else {
                navigate(ResourceBundle.getBundle("/Bundle").getString(errorPage));
            }
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isLoggedIn() {
        return notNull(facesContext().getExternalContext().getUserPrincipal());
    }

    public static boolean loggedIn() {
        return notNull(facesContext().getExternalContext().getUserPrincipal());
    }

    /* // used when using JAAC
     public static String getUsername() {
     try {
     Principal principal = facesContext().getExternalContext().getUserPrincipal();
     if (notNull(principal)) {
     return principal.getName();
     }
     } catch (Exception e) {
     // LOG.info("Exception @ JsfUtil getUsername()", e);
     }
     return null;
     }
     */
    public static boolean userIsAuthenticated() {
        return userPrincipal() != null;
    }

    // used when storing use in session
    public static String getUsername() {
        String username = (String) request().getSession().getAttribute("username");
        if (username == null) {
            return "Betrand"; // "System" // For testing with no active security context
        }
        return username;
    }

    public static void store(String name, String object) {
        HttpSession session = request().getSession();
        session.setAttribute(name, object);
    }

    public static void remove(String name) {
        HttpSession session = request().getSession();
        session.removeAttribute(name);
    }

    public static Principal userPrincipal() {
        return request().getUserPrincipal();
    }

    public static HttpServletRequest request() {
        return (HttpServletRequest) externalContext().getRequest();
    }

    public static HttpServletResponse response() {
        return (HttpServletResponse) externalContext().getResponse();
    }

    public static ServletContext servletContext() {
        return (ServletContext) externalContext().getContext();
    }

    public static ExternalContext externalContext() {
        return facesContext().getExternalContext();
    }

    public static FacesContext facesContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
     *
     * @return only the contextPath e.g. /emb
     */
    public static String path() {
        return ((HttpServletRequest) request()).getContextPath();
    }

    /**
     *
     * @return the full path such as /emb/sysadmin/welcome.jsf
     */
    public static String uri() {
        return ((HttpServletRequest) request()).getRequestURI();
    }

    public static Object getBean(String bean) {
        return facesContext().
                getApplication().
                getELResolver().
                getValue(facesContext().
                        getELContext(), null, bean);
    }

    public static void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                log(e + "");
            }
        }
    }

    public static String getLogo() {
        try {
            return getResource("TempFiles") + "fileshare/logo.png";
        } catch (Exception e) {
            log("Could not find Logo");
        }
        return "";
    }

    public static String extract(String toExtract, String string) {
        int index = string.indexOf(toExtract);
        String extracted = string.substring(index);
        return extracted;
    }

    public static double roundOff(double x, int digits) {
        String val = String.valueOf(x);
        int index = val.indexOf(".");
        val = val.substring(index);
        if (val.length() > 4) {
            return new BigDecimal(x).setScale(digits, RoundingMode.UP).doubleValue();
        }
        return new BigDecimal(x).setScale(digits, RoundingMode.FLOOR).doubleValue();
        // return x;
    }

    public static Date getDateMinusYearsFromToday(int yearsAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, yearsAgo);
        return c.getTime();
    }

    public static boolean validateInlineDate(Date start_, Date end_) {
        if (start_ == null) {
            return false;
        }

        Date date = new Date();
        StringBuilder sb = new StringBuilder();

        date = getDownAdjustedDate(date);
        if (!date.equals(start_) && date.after(start_)) {
            JsfUtil.addWarningMessage(sb
                    .append("Invalid Date ")
                    .append(start_)
                    .append(" You cannot assign an Event on a Date in the Past")
                    .toString()
            );
            return true;
        }

        if (end_ != null && end_.before(start_)) {
            JsfUtil.addWarningMessage("Finish Time cannot be before Start Time");
            return true;
        }
        return false;
    }

    public static boolean validateDate(Date start_, Date end_) {
        if (start_ == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        date = getDownAdjustedDate(date);
        if (!date.equals(start_) && date.after(start_)) {
            JsfUtil.addWarningMessage(sb
                    .append("Invalid Date ")
                    .append(formatDate(date))
                    .append(". ")
                    .append(" You cannot assign an Event on a Date in the Past")
                    .toString()
            );
            return true;
        }

        if (end_ != null && end_.before(start_)) {
            JsfUtil.addWarningMessage(sb
                    .append("Invalid Date range ")
                    .append(formatDate(end_))
                    .append(" is before ")
                    .append(formatDate(start_)).toString()
            );
            return true;
        }
        return false;
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    public static Date getDownAdjustedDate(Date date) {
        return new Date(date.getTime() - 60 * 60 * 24 * 1000);
    }

    public static Date getUpAdjustedDate(Date date) {
        return new Date(date.getTime() + 60 * 60 * 24 * 1000);
    }

    public static boolean startAndEndInclusiveOverlap(Date start1, Date end1, Date from, Date to) {
        return start1.getTime() <= to.getTime() && from.getTime() >= end1.getTime();
    }

    public static boolean startInclusiveOverlap(Date start1, Date end1, Date from, Date to) {
        return start1.getTime() <= to.getTime() && from.getTime() > end1.getTime();
    }

    public static boolean startAndEndExclusiveOverlap(Date start1, Date end1, Date from, Date to) {
        return start1.getTime() < to.getTime() && from.getTime() > end1.getTime();
    }

    public static boolean endInclusiveOverlap(Date start1, Date end1, Date from, Date to) {
        return start1.getTime() < to.getTime() && from.getTime() >= end1.getTime();
    }

    public static String getFileExtension(String file, String preferred) {
        if (file == null) {
            return preferred;
        }

        String fileExtension = file.substring(file.lastIndexOf('.') + 1);
        fileExtension = fileExtension.toLowerCase();
        switch (fileExtension) {
            case "pdf":
                return "application/pdf";
            case "png":
                return "application/png";
            case "jpg":
                return "application/jpg";
            case "jpeg":
                return "application/jpeg";
            case "ps":
                return "application/ps";
            case "txt":
                return "application/text-plain";
            default:
                return "application/pdf";
        }
    }

    public static String userCookie() {
        return "KEEP_ALIVE_ADMIN";
    }

    public static String passerByCookie() {
        return "PASSER_BY_ADMIN";
    }

    public static Cookie findCookie(String userCookie) {
        Cookie[] cookies = JsfUtil.request().getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(userCookie)) {
                    //LOG.info("Found cookie ");
                    return cookie;
                }
            }
        }
        // LOG.info("Found No cookie");
        return null;
    }

    public static Cookie findAnotherCookie(String userCookie, String oldCookie) {
        Cookie[] cookies = JsfUtil.request().getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(userCookie) && !cookie.getValue().equals(oldCookie)) {
                    //LOG.info("Found cookie");
                    return cookie;
                }
            }
        }
        // LOG.info("Found No cookie");
        return null;
    }

    public static String getAnotherCookie(String oldCookie, String cookieName) {
        Cookie sessionCookie;
        if (cookieName == null) {
            sessionCookie = findCookie(userCookie());
            if (JsfUtil.notNull(sessionCookie) && !sessionCookie.getValue().equals(oldCookie)) {
                return sessionCookie.getValue();
            } else {
                sessionCookie = findCookie(passerByCookie());
                if (JsfUtil.notNull(sessionCookie) && !sessionCookie.getValue().equals(oldCookie)) {
                    return sessionCookie.getValue();
                }
                return null;
            }
        } else if (cookieName.equals(userCookie())) {
            sessionCookie = findCookie(userCookie());
            if (JsfUtil.notNull(sessionCookie) && !sessionCookie.getValue().equals(oldCookie)) {
                return sessionCookie.getValue();
            } else {
                sessionCookie = findCookie(passerByCookie());
                if (JsfUtil.notNull(sessionCookie) && !sessionCookie.getValue().equals(oldCookie)) {
                    return sessionCookie.getValue();
                }
                return null;
            }
        }
        return findCookie(passerByCookie()).getValue();
    }

}
