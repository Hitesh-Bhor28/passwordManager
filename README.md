# Password Manager Application

## Overview
A secure password management application built using Java and Swing, allowing users to store and manage passwords with enhanced security measures.

---

## Features
- **User Authentication:** Register and login with secure password hashing using BCrypt.
- **Password Management:** Add, view, update, and delete stored passwords.
- **Password Strength Indicator:** Displays the strength of passwords (Weak, Medium, Strong).
- **Generate Strong Passwords:** Generate and update weak passwords to strong, system-generated ones.
- **Search Functionality:** Search for specific accounts by name.

---

## Security Enhancements
- **Password Hashing:** All passwords are stored as hashed strings in the database using BCrypt.
- **In-Memory Decryption:** Plaintext passwords are decrypted in memory during an active session for functionality.
- **Session Security:** Decrypted passwords are cleared from memory on logout.

---

## Installation
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Open the project in your preferred Java IDE.
3. Set up a local MySQL database and configure the `Database` class with your credentials.
4. Run the application.

---

## Usage
1. **Register:** Create a new account.
2. **Login:** Access the application with your credentials.
3. **Manage Passwords:**
   - Add new passwords with account names.
   - View all stored passwords.
   - Search for specific accounts.
   - Update weak passwords to strong ones.
4. **Logout:** Securely log out and clear session data.

---

## Known Issues
- **Plaintext Password Visibility:** Users can view plaintext passwords in the GUI for convenience.
- **Performance Impact:** Hashing and decryption may slow down with large datasets.

---

## Future Enhancements
- Implement two-factor authentication.
- Add password export/import functionality.
- Improve UI responsiveness for large datasets.

---

## Bugs
### 1. **Plain Passwords Visible in Database**
- Resolved by hashing passwords before storing them.

### 2. **Decrypted Passwords Persist After Logout**
- Fixed by clearing passwords from memory on logout.

### 3. **Incorrect Display of Hashed Passwords**
- Addressed by decrypting passwords in memory during a session for GUI display.

### 4. **Weak Encryption Algorithm Detected**
- Upgraded to BCrypt for robust password hashing.

---

## Contributing
Contributions are welcome. Please create a pull request with a detailed description of your changes.

---

## License
This project is licensed under the [MIT License](LICENSE).
