package controller;

import dao.AppointmentDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Contact;
import model.ListManager;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static controller.Home.selectedAppointment;
import static controller.LogIn.currentUser;

public class ModifyAppointment implements Initializable {

    public TextField appointmentIdTF;
    public TextField titleTF;
    public TextField descriptionTF;
    public TextField locationTF;
    public ComboBox contactCB;
    public TextField typeTF;
    public DatePicker startDateDP;
    public ComboBox startHourCB;
    public ComboBox startMinuteCB;
    public DatePicker endDateDP;
    public ComboBox endHourCB;
    public ComboBox endMinuteCB;
    public TextField customerIdTF;
    public TextField userIdTF;

    public String title;
    public String description;
    public String location;
    public String contactName;
    public String type;
    public String startDate;
    public String endDate;
    public String startHour;
    public String endHour;
    public String startMinute;
    public String endMinute;
    public String startTime;
    public String endTime;
    public Timestamp startDateTime;
    public Timestamp endDateTime;
    public String updatedBy;
    public int customerId;
    public int userId;
    public int contactId;
    public int appointmentId;

    ZoneId utcZI = ZoneId.of("UTC");
    ZoneId userZI = ZoneId.systemDefault();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIdTF.setText(String.valueOf(selectedAppointment.getAppointmentId()));
        titleTF.setText(selectedAppointment.getTitle());
        descriptionTF.setText(selectedAppointment.getDescription());
        locationTF.setText(selectedAppointment.getLocation());
        contactCB.setItems(ListManager.allContactNames);
        contactCB.getSelectionModel().select(selectedAppointment.getContactName());
        typeTF.setText(selectedAppointment.getType());
        startDateDP.setValue(LocalDate.parse(selectedAppointment.getStartDate()));
        endDateDP.setValue(LocalDate.parse(selectedAppointment.getEndDate()));

        startHourCB.setItems(ListManager.hours);
        startHourCB.getSelectionModel().select(selectedAppointment.getStartTime().substring(0,2));
        startMinuteCB.setItems(ListManager.minutes);
        startMinuteCB.getSelectionModel().select(selectedAppointment.getStartTime().substring(3,5));
        endHourCB.setItems(ListManager.hours);
        endHourCB.getSelectionModel().select(selectedAppointment.getEndTime().substring(0,2));
        endMinuteCB.setItems(ListManager.minutes);
        endMinuteCB.getSelectionModel().select(selectedAppointment.getEndTime().substring(3,5));

        customerIdTF.setText(String.valueOf(selectedAppointment.getCustomerId()));
        userIdTF.setText(String.valueOf(selectedAppointment.getUserId()));
    }

    public void onSaveReturnBtn(ActionEvent actionEvent) throws Exception {

        appointmentId = Integer.parseInt(appointmentIdTF.getText());
        title = titleTF.getText();
        description = descriptionTF.getText();
        location = locationTF.getText();
        contactName = contactCB.getSelectionModel().getSelectedItem().toString();
        type = typeTF.getText();
        startDate = startDateDP.getValue().toString();
        endDate = endDateDP.getValue().toString();

        startHour = startHourCB.getValue().toString();
        startMinute = startMinuteCB.getValue().toString();
        startTime = " " + startHour + ":" + startMinute + ":00";


        //Converts start time from user's time zone to UTC
        ZonedDateTime userStartZDT = ZonedDateTime.parse(startDate + startTime, formatter.withZone(userZI));
        ZonedDateTime startZDT = ZonedDateTime.ofInstant(userStartZDT.toInstant(), utcZI);
        LocalDateTime startLDT = startZDT.toLocalDateTime();
        startDateTime = Timestamp.valueOf(startLDT);

        endHour = endHourCB.getValue().toString();
        endMinute = endMinuteCB.getValue().toString();
        endTime = " " + endHour + ":" + endMinute + ":00";

        //Converts end time from user's time zone to UTC
        ZonedDateTime userEndZDT = ZonedDateTime.parse(endDate + endTime, formatter.withZone(userZI));
        ZonedDateTime endZDT = ZonedDateTime.ofInstant(userEndZDT.toInstant(), utcZI);
        LocalDateTime endLDT = endZDT.toLocalDateTime();
        endDateTime = Timestamp.valueOf(endLDT);

        updatedBy = currentUser;
        customerId = Integer.parseInt(customerIdTF.getText());
        userId = Integer.parseInt(userIdTF.getText());

        for(Contact contact : ListManager.allContacts) {
            if(contact.getContactName().equals(contactName)){
                contactId = contact.getContactId();
            }
        }

        AppointmentDao.update(title, description, location, type, startDateTime, endDateTime, updatedBy, customerId,
                userId, contactId, appointmentId);

        Parent root = FXMLLoader.load(getClass().getResource("../view/Home.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }

    public void onCancelBtn(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/Home.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }
}
