import java.io.Serializable;
import java.sql.*;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 *
 * @author team1
 */
@Named(value = "login")
@SessionScoped
@ManagedBean
public class Login extends DBConnect implements Serializable {

    private String login;
    private String password;
    public String succeed; 
    private UIInput loginUI;
    private DBConnect dbc = new DBConnect();

    public UIInput getLoginUI() {
        return loginUI;
    }

    public void setLoginUI(UIInput loginUI) {
        this.loginUI = loginUI;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void emplValidate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException { 
        Connection con = dbc.getConnection(); 
        
        login = loginUI.getLocalValue().toString();
        password = value.toString();
        
        String selectStmt = "select " + login + "," + password + "from employees";
        con.setAutoCommit(false); 
        try (Statement stmt = con.createStatement()) {
            stmt.execute(selectStmt); 
            ResultSet rs = stmt.executeQuery(selectStmt); 
            int rowNum = rs.getRow();

            if (rowNum < 1) {
                succeed = "fail"; 
                FacesMessage errorMessage = new FacesMessage("Wrong login/password");
                throw new ValidatorException(errorMessage);
            }
            succeed = "success"; 
            con.commit(); 
        } catch (SQLException e) {
            con.rollback();
        }
    }

    public String go() {
        if (succeed.equals("success")) {
            return "success";
        }
        Util.invalidateUserSession();
        return "fail"; 
    }

}
