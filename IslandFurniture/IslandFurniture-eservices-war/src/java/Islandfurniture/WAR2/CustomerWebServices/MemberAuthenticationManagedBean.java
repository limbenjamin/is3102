package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.Customer;
import java.io.IOException;
import java.io.Serializable;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class MemberAuthenticationManagedBean implements Serializable {

    private String emailAddress = null;
    private String password = null;
    private String name = null;
    private String address = null;
    private String phoneNo = null;
    private String dateOfBirth = null;
    private Long id;
    private String confirmPassword = null;
    private String coDir;
    private String coCode;
    private String code;
    private String target;

    @EJB
    private ManageMemberAuthenticationBeanLocal mmab;
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;  

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        coDir = (String) httpReq.getAttribute("coCode");
        code = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");
    }

    public void login() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        emailAddress = request.getParameter("loginForm:emailAddress");
        password = request.getParameter("loginForm:password");

        coDir = ec.getRequestParameterMap().get("coCode");
        target = ec.getRequestParameterMap().get("target");
        if (coDir == null || coDir.isEmpty()) {
            coDir = "";
        } else {
            coDir = "/" + coDir;
        }

        Customer customer = mmab.authenticate(emailAddress, password);
        //System.out.println(customer.getName());
        if (customer != null) {
            HttpSession session = Util.getSession();
            session.setAttribute("emailAddress", emailAddress);
            System.out.println(customer.getName() + " logged in.");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Logged in successfully", ""));

            if (target == null || target.isEmpty()) {
                ec.redirect(ec.getRequestContextPath() + coDir + "/home.xhtml");
            } else {
                ec.redirect(ec.getRequestContextPath() + coDir + target);
            }
        } else {
            System.out.println("Error logging in.");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email or password", ""));
            if (target == null || target.isEmpty()) {
                ec.redirect(ec.getRequestContextPath() + coDir + "/login.xhtml");
            } else {
                ec.redirect(ec.getRequestContextPath() + coDir + "/login.xhtml?target=" + target);
            }
        }
    }

    public void register() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        emailAddress = request.getParameter("registerForm:emailAddress");
        password = request.getParameter("registerForm:password");
        confirmPassword = request.getParameter("registerForm:confirmPassword");

        target = ec.getRequestParameterMap().get("target");
        coCode = ec.getRequestParameterMap().get("coCode");
        coDir = coCode;

        if (coDir == null || coDir.isEmpty()) {
            coDir = "";
        } else {
            coDir = "/" + coDir;
        }

        if (!password.equals(confirmPassword)) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match", ""));
            if (target == null || target.isEmpty()) {
                ec.redirect(ec.getRequestContextPath() + coDir + "/login.xhtml");
            } else {
                ec.redirect(ec.getRequestContextPath() + coDir + "/login.xhtml?target=" + target);
            }
        }
        name = request.getParameter("registerForm:name");
        address = request.getParameter("registerForm:address");
        phoneNo = request.getParameter("registerForm:phoneNo");
        dateOfBirth = request.getParameter("registerForm:dateOfBirth");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Country country = manageLocalizationBean.findCoByCode(coCode).getCountry();
        Date date = null;
        try {
            date = sdf.parse(dateOfBirth);
        } catch (ParseException ex) {
            Logger.getLogger(MemberAuthenticationManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        Format formatter = new SimpleDateFormat("dd-mm-YYYY");
        String s = formatter.format(date);
        id = mmab.createCustomerAccount(emailAddress, password, name, phoneNo, address, s, country);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Account Created", ""));
        if (target == null || target.isEmpty()) {
            ec.redirect(ec.getRequestContextPath() + coDir + "/home.xhtml");
        } else {
            ec.redirect(ec.getRequestContextPath() + coDir + target);
        }
    }

    public void logout() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        /**
         * if (coDir == null || coDir.isEmpty()) { coDir = ""; } else { coDir =
         * "/" + coDir; }*
         */
        HttpSession session = Util.getSession();
        session.setAttribute("", emailAddress);
        session.invalidate();
        ec.redirect(ec.getRequestContextPath() + coDir + "/home.xhtml");
    }

    public void forgotPassword() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        emailAddress = request.getParameter("forgotPasswordForm:emailAddress");
        dateOfBirth = request.getParameter("forgotPasswordForm:dateOfBirth");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        try {
            date = sdf.parse(dateOfBirth);
        } catch (ParseException ex) {
            Logger.getLogger(MemberAuthenticationManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        Format formatter = new SimpleDateFormat("dd-mm-YYYY");
        String s = formatter.format(date);
        coDir = ec.getRequestParameterMap().get("coCode");
        if (coDir == null || coDir.isEmpty()) {
            coDir = "";
        } else {
            coDir = "/" + coDir;
        }
        boolean result = mmab.forgotPassword(emailAddress, s);
        if (result) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "An email has been sent to your account with further instructions", ""));
            ec.redirect(ec.getRequestContextPath() + coDir + "/home.xhtml");
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error, unable to reset password", ""));
            ec.redirect(ec.getRequestContextPath() + coDir + "/home.xhtml");
        }

    }

    public void resetPassword() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        code = request.getParameter("resetPasswordForm:code");
        password = request.getParameter("resetPasswordForm:password");
        confirmPassword = request.getParameter("resetPasswordForm:confirmPassword");
        coDir = ec.getRequestParameterMap().get("coCode");
        if (coDir == null || coDir.isEmpty()) {
            coDir = "";
        } else {
            coDir = "/" + coDir;
        }
        System.err.println(code + "A" + password);
        if (password.equals(confirmPassword)) {

            boolean result = mmab.resetPassword(code, password);
            if (result) {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Reset successful", ""));
                ec.redirect(ec.getRequestContextPath() + coDir + "/home.xhtml");
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error, unable to reset password", ""));
                ec.redirect(ec.getRequestContextPath() + coDir + "/resetpassword.xhtml?code=" + code);
            }
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error, passwords not the same", ""));
            ec.redirect(ec.getRequestContextPath() + coDir + "/resetpassword.xhtml?code=" + code);
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ManageMemberAuthenticationBeanLocal getMmab() {
        return mmab;
    }

    public void setMmab(ManageMemberAuthenticationBeanLocal mmab) {
        this.mmab = mmab;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCoDir() {
        return coDir;
    }

    public void setCoDir(String coDir) {
        this.coDir = coDir;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
