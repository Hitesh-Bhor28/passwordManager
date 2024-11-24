import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

/*
<applet code="PasswordManagerApplet" width=500 height=300>
</applet>
*/

public class PasswordManagerApplet extends Applet implements ActionListener {
    private TextField accountField, passwordField, retrieveField, displayField;
    private Button saveButton, retrieveButton;
    private HashMap<String, String> passwordStore;

    @Override
    public void init() {
        setLayout(new GridLayout(6, 2, 10, 10));

        // Initialize components
        passwordStore = new HashMap<>();

        add(new Label("Account Name:"));
        accountField = new TextField();
        add(accountField);

        add(new Label("Password:"));
        passwordField = new TextField();
        passwordField.setEchoChar('*');
        add(passwordField);

        saveButton = new Button("Save Password");
        saveButton.addActionListener(this);
        add(saveButton);

        add(new Label("Retrieve Account:"));
        retrieveField = new TextField();
        add(retrieveField);

        retrieveButton = new Button("Retrieve Password");
        retrieveButton.addActionListener(this);
        add(retrieveButton);

        add(new Label("Password Retrieved:"));
        displayField = new TextField();
        displayField.setEditable(false);
        add(displayField);

        add(new Label("")); // Empty cell for better layout
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String account = accountField.getText().trim();
            String password = passwordField.getText().trim();

            if (!account.isEmpty() && !password.isEmpty()) {
                // Save password in the HashMap
                passwordStore.put(account, password);
                accountField.setText("");
                passwordField.setText("");
                showStatus("Password saved successfully for " + account);
            } else {
                showStatus("Account or Password cannot be empty!");
            }
        } else if (e.getSource() == retrieveButton) {
            String account = retrieveField.getText().trim();

            if (!account.isEmpty()) {
                // Retrieve password
                String password = passwordStore.get(account);
                if (password != null) {
                    displayField.setText(password);
                    showStatus("Password retrieved for " + account);
                } else {
                    displayField.setText("No password found!");
                    showStatus("No password found for " + account);
                }
            } else {
                showStatus("Account name cannot be empty!");
            }
        }
    }
}
