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
    private UIInput loginUI;

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

    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        DBConnect dbc = new DBConnect();
        Connection con = dbc.getConnection();
        
        login = loginUI.getLocalValue().toString();
        password = value.toString();
        
        String selectStmt = "select * from Employees where Login='" + login + "' and Pwd='" + password + "'";
        try (Statement stmt = con.createStatement()) {
            stmt.execute(selectStmt);
            ResultSet res = stmt.executeQuery(selectStmt);
            
//            res.next();
            if (!res.next()) { //executeQuery doesnt actually returns null, ie can't say res == null
                selectStmt = "select * from Customer where Login='" + login + "' and Pwd='" + password + "'";
                try (Statement s = con.createStatement()) {
                    s.execute(selectStmt);
                    res = stmt.executeQuery(selectStmt);
                    
//                    res.next();
                    if (!res.next()) { //executeQuery doesnt actually returns null, ie can't say res == null
                        FacesMessage errorMessage = new FacesMessage("Incorrect login/password");
                        throw new ValidatorException(errorMessage);
                    }
                    
                    go();
                } catch (SQLException e) {
                    con.rollback();
                }
            }
            
            go();
            
        } catch (SQLException e) {
            con.rollback();
        }
    }

    public String go() {
      //  Util.invalidateUserSession();
        return "success";
    }
    
    private boolean existsLogin(String userLogin) throws SQLException {
        DBConnect dbc = new DBConnect();
        Connection con = dbc.getConnection();
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps = con.prepareStatement("select * from customer where login = " + userLogin);

        ResultSet result = ps.executeQuery();
        if (result.next()) {
            result.close();
            con.close();
            return true;
        }
        result.close();
        con.close();
        return false;
    }
    
    public void validateLogin(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException, SQLException {
        String loginUser = (String) value;
        if (existsLogin((String) loginUser)) {
            FacesMessage errorMessage = new FacesMessage("Login already exists");
            throw new ValidatorException(errorMessage);
        }
    }
}
