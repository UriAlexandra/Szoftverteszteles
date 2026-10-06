public class User {

    private String userName, password;
    private int id;
    private boolean isLoggedIn;

    private int loginCount;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void login() {
        isLoggedIn = true;
        loginCount++;
    }

    public void logout() {
        isLoggedIn = false;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void updatePwd(String newPassword, boolean isLoggedIn) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

}
