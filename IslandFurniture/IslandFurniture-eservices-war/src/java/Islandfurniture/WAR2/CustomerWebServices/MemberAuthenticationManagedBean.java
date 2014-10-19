package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.Entities.Customer;
import java.io.IOException;
import java.io.Serializable;
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

    @EJB
    private ManageMemberAuthenticationBeanLocal mmab;

    public void login() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        emailAddress = request.getParameter("loginForm:emailAddress");
        password = request.getParameter("loginForm:password");

        coDir = ec.getRequestParameterMap().get("coCode");
        if (coDir == null || coDir.isEmpty()) {
            coDir = "";
        } else {
            coDir = "/" + coDir;
        }

        Customer customer = mmab.authenticate(emailAddress, password);
        if (customer != null) {
            HttpSession session = Util.getSession();
            session.setAttribute("emailAddress", emailAddress);
            System.out.println(customer.getName() + " logged in.");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Logged in successfully", ""));
            ec.redirect(ec.getRequestContextPath() + coDir + "/home.xhtml");
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email or password", ""));
            ec.redirect(ec.getRequestContextPath() + coDir + "/login.xhtml");
        }
    }

    public void register() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        emailAddress = request.getParameter("registerForm:emailAddress");
        password = request.getParameter("registerForm:password");
        confirmPassword = request.getParameter("registerForm:confirmPassword");

        coDir = ec.getRequestParameterMap().get("coCode");
        if (coDir == null || coDir.isEmpty()) {
            coDir = "";
        } else {
            coDir = "/" + coDir;
        }

        if (!password.equals(confirmPassword)) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match", ""));
            ec.redirect(ec.getRequestContextPath() + coDir + "/login.xhtml");
        }
        name = request.getParameter("registerForm:name");
        address = request.getParameter("registerForm:address");
        phoneNo = request.getParameter("registerForm:phoneNo");
        dateOfBirth = request.getParameter("registerForm:dateOfBirth");
        id = mmab.createCustomerAccount(emailAddress, password, name, phoneNo, address, dateOfBirth);
        ec.redirect(ec.getRequestContextPath() + coDir + "/home.xhtml");
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

}
