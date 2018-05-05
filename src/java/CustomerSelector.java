import java.io.Serializable;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author team1
 */
@Named(value = "selector")
@ManagedBean
@SessionScoped
public class CustomerSelector implements Serializable {

    private String[] choices = {"Check Reservation", "Make Reservation", "Cancel Reservation"};
    private String choice;

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String transition() {
        switch (choice) {
            case "Check Reservation":
                return "getReservation";
            case "Make Reservation":
                return "createReservation";
            case "Cancel Reservation":
                return "deleteReservation";
            default:
                return null;
        }
    }

}
