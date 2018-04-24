import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import java.util.Date;
import java.util.TimeZone;
import javax.el.ELContext;
import javax.faces.bean.ManagedProperty;

@Named(value = "customer")
@SessionScoped
@ManagedBean
public class Customer implements Serializable {

    @ManagedProperty(value = "#{login}")
    private Login login;

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    private DBConnect dbConnect = new DBConnect();
    //private Integer CID;
    private String fname;
    private String lname;
    private String email;
    private String address;
    private String ccn;
    private Date exp_date;
    private Integer crccode; 
    private Date created_date;

    /*public Integer getCID() throws SQLException {
        if (CID == null) {
            Connection con = dbConnect.getConnection();

            if (con == null) {
                throw new SQLException("Can't get database connection");
            }

            PreparedStatement ps = con.prepareStatement(
                            "select max(customer_id)+1 from customer");
            ResultSet result = ps.executeQuery();
            if (!result.next()) {
                return null;
            }
            CID = result.getInt(1);
            result.close();
            con.close();
        }
        return CID;
    }

    public void setCustomerID(Integer customerID) {
        this.CID = customerID;
    }*/

    public String getFName() {
        // ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        //Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
    
        //  return login.getLogin();
        if (fname == null) {
            try(Connection con = dbConnect.getConnection()) {
                PreparedStatement ps = con.prepareStatement("select fname from customer");
                ResultSet result = ps.executeQuery();
                if (!result.next()) {
                    return null;
                }
                fname = result.getString("fname"); 
                result.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("Can't get database connection"); 
            }
        }
        return fname;
    }

    public void setFName(String fname) {
        this.fname = fname;
    }
    
    public String getLName() {
        if (lname == null) {
            try(Connection con = dbConnect.getConnection()) {
                PreparedStatement ps = con.prepareStatement("select lname from customer");
                ResultSet result = ps.executeQuery();
                if (!result.next()) {
                    return null;
                }
                lname = result.getString("lname"); 
                result.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("Can't get database connection"); 
            }
        }
        return lname;
    }
    
    public void setLName(String lname) {
        this.lname = lname;
    }
    
    public String getEmail() {
        if (email == null) {
            try(Connection con = dbConnect.getConnection()) {
                PreparedStatement ps = con.prepareStatement("select email from customer");
                ResultSet result = ps.executeQuery();
                if (!result.next()) {
                    return null;
                }
                email = result.getString("email"); 
                result.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("Can't get database connection"); 
            }
        }
        return email; 
    }
    
    public void setEmail(String email) {
        this.email = email; 
    }

    public String getAddress() {
        if (address == null) {
            try(Connection con = dbConnect.getConnection()) {
                PreparedStatement ps = con.prepareStatement("select address from customer");
                ResultSet result = ps.executeQuery();
                if (!result.next()) {
                    return null;
                }
                address = result.getString("address"); 
                result.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("Can't get database connection"); 
            }
        }
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCCN() {
        if (ccn == null) {
            try(Connection con = dbConnect.getConnection()) {
                PreparedStatement ps = con.prepareStatement("select ccn from customer");
                ResultSet result = ps.executeQuery();
                if (!result.next()) {
                    return null;
                }
                ccn = result.getString("ccn"); 
                result.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("Can't get database connection"); 
            }
        }
        return ccn;
    }
    
    public void setCCN(String ccn) {
        this.ccn = ccn; 
    }
    
    public Date getExpDate() {
        return exp_date; 
    }
    
    public void setExpDate(Date exp_date) {
        this.exp_date = exp_date; 
    }
    
    public Integer getCRCCode() {
        return crccode; 
    }
    
    public void setCRCCode(Integer code) {
        this.crccode = code; 
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        this.created_date = created_date;
    }

    public String createCustomer() throws SQLException, ParseException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);

        Statement statement = con.createStatement();

        PreparedStatement preparedStatement = con.prepareStatement("Insert into Customer values(?,?,?,?)");
        preparedStatement.setInt(1, CID);
        preparedStatement.setString(2, fname);
        preparedStatement.setString(3, address);
        preparedStatement.setDate(4, new java.sql.Date(created_date.getTime()));
        preparedStatement.executeUpdate();
        statement.close();
        con.commit();
        con.close();
        //Util.invalidateUserSession();
        return "main";
    }

    public String deleteCustomer() throws SQLException, ParseException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);

        Statement statement = con.createStatement();
        statement.executeUpdate("Delete from Customer where customer_id = " + CID);
        statement.close();
        con.commit();
        con.close();
        Util.invalidateUserSession();
        return "main";
    }

    public String showCustomer() {
        return "showCustomer";
    }

    public Customer getCustomer() throws SQLException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps
                = con.prepareStatement(
                        "select * from customer where customer_id = " + CID);

        //get customer data from database
        ResultSet result = ps.executeQuery();

        result.next();

        fname = result.getString("name");
        address = result.getString("address");
        created_date = result.getDate("created_date");
        return this;
    }

    public List<Customer> getCustomerList() throws SQLException {

        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps = con.prepareStatement(
            "select login, fname, lname, email, address, ccn, expdate, crccode from customer order by lname");

        //get customer data from database
        ResultSet result = ps.executeQuery();

        List<Customer> list = new ArrayList<Customer>();

        while (result.next()) {
            
            Customer cust = new Customer();

            cust.setCustomerID(result.getInt("customer_id"));
            cust.setFName(result.getString("name"));
            cust.setAddress(result.getString("address"));
            cust.setCreated_date(result.getDate("created_date"));

            //store all data into a List
            list.add(cust);
        }
        result.close();
        con.close();
        return list;
    }

    public void customerIDExists(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException, SQLException {

        if (!existsCustomerId((Integer) value)) {
            FacesMessage errorMessage = new FacesMessage("ID does not exist");
            throw new ValidatorException(errorMessage);
        }
    }

    public void validateCustomerID(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException, SQLException {
        int id = (Integer) value;
        if (id < 0) {
            FacesMessage errorMessage = new FacesMessage("ID must be positive");
            throw new ValidatorException(errorMessage);
        }
        if (existsCustomerId((Integer) value)) {
            FacesMessage errorMessage = new FacesMessage("ID already exists");
            throw new ValidatorException(errorMessage);
        }
    }

    private boolean existsCustomerId(int id) throws SQLException {
        Connection con = dbConnect.getConnection();
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps = con.prepareStatement("select * from customer where customer_id = " + id);

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
}
