package application.controller;

import application.controller.enums.AccountUpdateResult;
import application.controller.services.MainConnection;
import application.controller.services.UpdateDetailsService;
import application.controller.tools.Security;
import application.model.Account;
import application.model.AccountUpdate;
import application.view.ViewFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ProfileWindowController extends BaseController implements Initializable {

  private Account account;
  private AccountUpdate accountUpdate;
  private UpdateDetailsService updateDetailsService;

  @FXML
  private AnchorPane anchorPane;

  @FXML
  private Label usernameErrorLabel;

  @FXML
  private Label emailErrorLabel;

  @FXML
  private Label passwordErrorLabel;

  @FXML
  private Label tutorStatusErrorLabel;

  @FXML
  private Label usernameLabel;

  @FXML
  private Label emailAddressLabel;

  @FXML
  private Label tutorStatusLabel;

  @FXML
  private TextField newUsernameField;

  @FXML
  private TextField newEmailField;

  @FXML
  private TextField confirmNewEmailField;

  @FXML
  private PasswordField currentPasswordForUsernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private PasswordField passwordConfirmField;

  @FXML
  private PasswordField currentPasswordForPasswordField;

  @FXML
  private PasswordField currentPasswordForEmailField;

  @FXML
  private PasswordField currentPasswordForTutorStatusField;

  @FXML
  private Button updatePasswordButton;

  @FXML
  private Button updateUsernameButton;

  @FXML
  private Button updateEmailButton;

  @FXML
  private Button updateTutorStatusButton;

  @FXML
  private CheckBox isTutorCheckBox;


  /**
   * CONSTRUCTOR DESCRIPTION.
   *  @param viewFactory
   * @param fxmlName
   * @param mainConnection
   * @param account
   */
  public ProfileWindowController(ViewFactory viewFactory, String fxmlName,
      MainConnection mainConnection, Account account) {
    super(viewFactory, fxmlName, mainConnection);
    this.account = account;
    updateDetailsService = new UpdateDetailsService(null, mainConnection);
  }

  /**
   * CONSTRUCTOR DESCRIPTION.
   *  @param viewFactory
   * @param fxmlName
   * @param mainConnection
   * @param account
   */
  public ProfileWindowController(ViewFactory viewFactory, String fxmlName,
      MainConnection mainConnection, Account account, Button updatePasswordButton,
      Button updateUsernameButton, Button updateEmailButton, Button updateTutorStatusButton,
      Label usernameErrorLabel, Label emailErrorLabel, Label passwordErrorLabel,
      Label tutorStatusErrorLabel, Label usernameLabel, Label emailAddressLabel,
      Label tutorStatusLabel, TextField newUsernameField, TextField newEmailField,
      TextField confirmNewEmailField, PasswordField currentPasswordForUsernameField,
      PasswordField passwordField, PasswordField passwordConfirmField,
      PasswordField currentPasswordForPasswordField, PasswordField currentPasswordForEmailField,
      PasswordField currentPasswordForTutorStatusField,CheckBox isTutorCheckBox) {
    super(viewFactory, fxmlName, mainConnection);
    this.account = account;
    updateDetailsService = new UpdateDetailsService(null, mainConnection);

    this.updatePasswordButton = updatePasswordButton;
    this.updateUsernameButton = updateUsernameButton;
    this.updateEmailButton = updateEmailButton;
    this.updateTutorStatusButton = updateTutorStatusButton;
    this.usernameErrorLabel = usernameErrorLabel;
    this.emailErrorLabel = emailErrorLabel;
    this.passwordErrorLabel = passwordErrorLabel;
    this.tutorStatusErrorLabel = tutorStatusErrorLabel;
    this.usernameLabel = usernameLabel;
    this.emailAddressLabel = emailAddressLabel;
    this.tutorStatusLabel = tutorStatusLabel;
    this.newUsernameField = newUsernameField;
    this.newEmailField = newEmailField;
    this.confirmNewEmailField = confirmNewEmailField;
    this.currentPasswordForUsernameField = currentPasswordForUsernameField;
    this.passwordField = passwordField;
    this.passwordConfirmField = passwordConfirmField;
    this.currentPasswordForPasswordField = currentPasswordForPasswordField;
    this.currentPasswordForEmailField = currentPasswordForEmailField;
    this.currentPasswordForTutorStatusField = currentPasswordForTutorStatusField;
    this.isTutorCheckBox = isTutorCheckBox;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    updateAccountViews();
  }

  private void updateAccountViews() {
    if (account != null) {
      usernameLabel.setText(account.getUsername());
      emailAddressLabel.setText(account.getEmailAddress());

      if (account.getTutorStatus() == 0) {
        tutorStatusLabel.setText("Student Account");
      } else {
        tutorStatusLabel.setText("Tutor Account");
      }
    }
  }

  @FXML
  void updateEmailAction() {
    if (Security.emailIsValid(newEmailField.getText(), confirmNewEmailField.getText(), emailErrorLabel)) {
      if (isPasswordFilled(currentPasswordForEmailField, emailErrorLabel)) {
        account.setHashedpw(Security.hashPassword(currentPasswordForEmailField.getText()));
        accountUpdate = new AccountUpdate(account, "null",
            newEmailField.getText(), "null", -1);
        updateDetails(emailErrorLabel, "Email");
      }
    }
  }

  @FXML
  void updatePasswordAction() {
    if (Security.passwordIsValid(passwordField.getText(), passwordConfirmField.getText(), passwordErrorLabel)) {
      if (isPasswordFilled(currentPasswordForPasswordField, passwordErrorLabel)) {
        account.setHashedpw(Security.hashPassword(currentPasswordForPasswordField.getText()));
        accountUpdate = new AccountUpdate(account, "null",
            "null", Security.hashPassword(passwordField.getText()), -1);
        updateDetails(passwordErrorLabel, "Password");
      }
    }
  }

  @FXML
  void updateTutorStatusAction() {
    if (isPasswordFilled(currentPasswordForTutorStatusField, tutorStatusErrorLabel)) {
      account.setHashedpw(Security.hashPassword(currentPasswordForTutorStatusField.getText()));
      accountUpdate = new AccountUpdate(account, "null",
          "null", "null",  isTutorCheckBox.isSelected() ? 1 : 0);
      updateDetails(tutorStatusErrorLabel, "TutorStatus");
    }
  }

  @FXML
  void updateUsernameAction() {
    if (Security.usernameIsValid(newUsernameField.getText(), usernameErrorLabel)) {
      if (isPasswordFilled(currentPasswordForUsernameField, usernameErrorLabel)) {
        account.setHashedpw(Security.hashPassword(currentPasswordForUsernameField.getText()));
        accountUpdate = new AccountUpdate(account, newUsernameField.getText(),
            "null", "null", -1);
        updateDetails(usernameErrorLabel, "Username");
      }
    }
  }

  private boolean isPasswordFilled(PasswordField passwordField, Label errorLabel) {
    if (passwordField.getText().isEmpty()) {
      errorLabel.setText("Please Enter Password");
      return false;
    }
    return true;
  }

  private void updateDetails(Label errorLabel, String field) {
    updateDetailsService.setAccountUpdate(accountUpdate);

    if (!updateDetailsService.isRunning()) {
      updateDetailsService.reset();
      updateDetailsService.start();
    } else {
      System.out.println("Error as UpdateDetailsService is still running.");
    }

    updateDetailsService.setOnSucceeded(event -> {
      AccountUpdateResult result = updateDetailsService.getValue();

      switch (result) {
        case SUCCESS:
          System.out.println("Updated!");
          errorLabel.setText("Success");
          switch (field) {
            case "Email":
              account.setEmailAddress(newEmailField.getText());
              break;
            case "Password":
              account.setHashedpw(Security.hashPassword(passwordField.getText()));
              break;
            case "Username":
              account.setUsername(newUsernameField.getText());
              break;
            case "TutorStatus":
              account.setTutorStatus(isTutorCheckBox.isSelected() ? 1 : 0);
              break;
            default:
          }
          updateAccountViews();
          break;
        case FAILED_BY_CREDENTIALS:
          errorLabel.setText("Incorrect Password");
          break;
        case FAILED_BY_EMAIL_TAKEN:
          errorLabel.setText("Email Already Registered");
          break;
        case FAILED_BY_USERNAME_TAKEN:
          errorLabel.setText("Username Already Taken");
          break;
        case FAILED_BY_UNEXPECTED_ERROR:
          errorLabel.setText("Unexpected Error");
          break;
        case FAILED_BY_NETWORK:
          errorLabel.setText("Network Error");
          break;
        default:
      }
    });
  }
}