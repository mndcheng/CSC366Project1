import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.*;
import java.util.*;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.el.ELContext;
import javax.faces.bean.ManagedProperty;
import java.time.LocalDate; 

@Named(value = "reservations")
@SessionScoped
@ManagedBean
public class Reservations implements Serializable {
    
    @ManagedProperty(value = "#{login}")
    private Login login;

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
    
    private DBConnect dbConnect = new DBConnect();
    private String checkIn;
    private String checkOut; 
    private String view;
    private String bedType;
    private Integer roomNum; 
    
    /* can we just grab the user based on whoever's logged in? */ 
    public String getLoginUser() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Login login = (Login) elContext.getELResolver().getValue(elContext, null, "login");
    
        return login.getLogin();
    }
    
    public String getCheckIn() {
        if (checkIn == null) {
            try(Connection con = dbConnect.getConnection()) {
                PreparedStatement ps = con.prepareStatement("select checkin from reservations");
                ResultSet result = ps.executeQuery();
                if (!result.next()) {
                    return null;
                }
                checkIn = result.getString("checkin"); 
                result.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("Can't get database connection"); 
            }
        }
        return checkIn;
    }
    
    public void setCheckIn (String checkIn) {
        this.checkIn = checkIn; 
    }
    
    public String getCheckOut() {
        if (checkOut == null) {
            try(Connection con = dbConnect.getConnection()) {
                PreparedStatement ps = con.prepareStatement("select checkout from reservations");
                ResultSet result = ps.executeQuery();
                if (!result.next()) {
                    return null;
                }
                checkOut = result.getString("checkout"); 
                result.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("Can't get database connection"); 
            }
        }
        return checkOut;
    }
    
    public void setCheckOut (String checkOut) {
        this.checkOut = checkOut; 
    }
    
    public String getView() {
        if (view == null) {
            try(Connection con = dbConnect.getConnection()) {
                PreparedStatement ps = con.prepareStatement("select roomnum from reservations");
                ResultSet result = ps.executeQuery();
                if (!result.next()) {
                    return null;
                }
                int roomNum = result.getInt("roomnum"); 
                int viewNum = roomNum % 100;
                if (viewNum >= 1 || viewNum <= 6) {
                    view = "ocean";
                } else {
                    view = "pool";
                }
                result.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("Can't get database connection"); 
            }
        }
        return view;
    }
    
    public void setView(String view) {
        this.view = view; 
    }
    
    public String getBedType() {
        if (bedType == null) {
            try(Connection con = dbConnect.getConnection()) {
                PreparedStatement ps = con.prepareStatement("select roomnum from reservations");
                ResultSet result = ps.executeQuery();
                if (!result.next()) {
                    return null;
                }
                int roomNum = result.getInt("roomnum"); 
                if (roomNum%2 == 0) {
                    bedType = "double queen";
                } else {
                    bedType = "single king";
                }
                result.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("Can't get database connection"); 
            }
        }
        return bedType;
    }
    
    public void setBedType(String bedType) {
        this.bedType = bedType; 
    }
    
    public Integer getRoomNum() {
        if (roomNum == null) {
            try(Connection con = dbConnect.getConnection()) {
                PreparedStatement ps = con.prepareStatement("select roomnum from reservations");
                ResultSet result = ps.executeQuery();
                if (!result.next()) {
                    return null;
                }
                roomNum = result.getInt("roomnum"); 
                result.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("Can't get database connection"); 
            }
        }
        return roomNum;
    }
    
    public void setRoomNum (Integer roomNum) {
        this.roomNum = roomNum; 
    }
    
    /* connect this to DB as well? */ 
    /*public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        this.created_date = created_date;
    }*/

    public String createReservation() throws SQLException, ParseException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);

        Statement statement = con.createStatement();
        
        String insert = "insert into reservations (custlogin, checkin, checkout, roomnum) values (?,?,?,?)";
        
        PreparedStatement preparedStatement = con.prepareStatement(insert);
        preparedStatement.setString(1, getLoginUser()); 
        preparedStatement.setDate(2, stringToDate(checkIn));
        preparedStatement.setDate(3, stringToDate(checkOut));
        preparedStatement.setInt(4, roomNum);
        //preparedStatement.setDate(8, new java.sql.Date(created_date.getTime()));
        preparedStatement.executeUpdate();
        statement.close();
        con.commit();
        con.close();
        //Util.invalidateUserSession();
        return "main";
    }
    
    private java.sql.Date stringToDate(String dateString) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            java.util.Date date = format.parse(dateString);
            return new java.sql.Date(date.getTime());
        } catch(ParseException e) {
            return null; 
        }
    }
    
    public String deleteReservation() throws SQLException, ParseException {
        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);

        Statement statement = con.createStatement();
        statement.executeUpdate("Delete from reservations where login = " + getLoginUser());
        statement.close();
        con.commit();
        con.close();
        Util.invalidateUserSession();
        return "main";
    }

    public String showReservation() {
        return "showReservation";
    }

    /*public Customer getCustomer() throws SQLException {
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

        FName = result.getString("name");
        address = result.getString("address");
        created_date = result.getDate("created_date");
        return this;
    }*/

    public List<Customer> getCustomerList() throws SQLException {

        Connection con = dbConnect.getConnection();

        if (con == null) {
            throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps = con.prepareStatement(
            "select login, FName, LName, email, address from customers order by LName");

        //get customer data from database
        ResultSet result = ps.executeQuery();

        List<Customer> list = new ArrayList<Customer>();

        while (result.next()) {
            
            Customer cust = new Customer();

            cust.setUserLogin(result.getString("login"));
            cust.setFName(result.getString("FName"));
            cust.setLName(result.getString("LName"));
            cust.setEmail(result.getString("email"));
            cust.setAddress(result.getString("address"));
            //cust.setCreated_date(result.getDate("created_date"));

            //store all data into a List
            list.add(cust);
        }
        result.close();
        con.close();
        return list;
    }

//    public void customerLoginExists(FacesContext context, UIComponent componentToValidate, Object value)
//            throws ValidatorException, SQLException {
//
//        if (!existsCustomerLogin((String) value)) {
//            FacesMessage errorMessage = new FacesMessage("User does not exist");
//            throw new ValidatorException(errorMessage);
//        }
//    }

    public void validateCustomerLogin(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException, SQLException {
        String loginUser = (String) value;
        if (existsCustomerLogin(loginUser)) {
            FacesMessage errorMessage = new FacesMessage("User already exists");
            throw new ValidatorException(errorMessage);
        }
    }

    private boolean existsCustomerLogin(String userLogin) throws SQLException {
        Connection con = dbConnect.getConnection();
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        
        PreparedStatement ps = con.prepareStatement("select * from customers where login = ?");
        ps.setString(1, userLogin); 

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
