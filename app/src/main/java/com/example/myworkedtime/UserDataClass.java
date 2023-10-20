package com.example.myworkedtime;

public class UserDataClass {
    private boolean isUserStudent;
    private Double rate = 1.0;
    private String dateOfCreateBackup;
    private String hashedPassword = null;

    //public boolean isPasswordCorrect(String Password){//тут пароль буде хешуватись і порівнюватись із записаним захешованим паролем}
    //public void setNewPassword(String newPassword){//тут короче буде новий пароль хешуватись 256 стандартом і записуватись в змінну}

    //getters
    public boolean isUserStudent() {
        return isUserStudent;
    }
    public Double getRate() {
        return rate;
    }
    public String getDateOfCreateBackup() {
        return dateOfCreateBackup;
    }
    //setters
    public void setRate(Double rate) {
        this.rate = rate;
    }
    public void setDateOfCreateBackup(String dateOfCreateBackup) {
        this.dateOfCreateBackup = dateOfCreateBackup;
    }
}
