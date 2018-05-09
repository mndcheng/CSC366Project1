import java.io.Serializable;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author team1
 */
@Named(value = "reservationSelector")
@ManagedBean
@SessionScoped
public class ReservationSelector implements Serializable {

    private String[] bedView = {"Bed Type", "Room View"};
    private String[] bedChoices = {"Double Queen", "Single King"};
    private String[] viewChoices = {"Ocean", "Pool"};
    private String choice;
    private String userChoice = "";

    public String[] getChoices() {
        if (userChoice.equals("Bed Type"))
            return bedChoices;
        return viewChoices;
    }

    public void setChoices(String[] choices) {
        if (userChoice.equals("Bed Type"))
            this.bedChoices = choices;
        else if (userChoice.equals("Room View"))
            this.viewChoices = choices;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String transition() {
        switch (choice) {
            case "Bed Type": 
                return "chooseBed";
            case "Room View": 
                return "chooseView";
            default:
                return null;
        }
    }

}
