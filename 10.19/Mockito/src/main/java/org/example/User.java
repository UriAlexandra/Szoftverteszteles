package org.example;

public class User {
    private int id;
    private String userName;
    private String password;
    private boolean isLoggedIn;

    // A User-nek ismernie kell a repository-t
    private UserDatabase userDatabase;

    // Üres konstruktor a Mockito számára
    public User() {
    }

    public User(int id, String userName, String password, boolean isLoggedIn, UserDatabase userDatabase) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
        this.userDatabase = userDatabase;
    }

    public int getId() { return id; }
    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public boolean isLoggedIn() { return isLoggedIn; }

    // A user osztály updatePwd függvénye hívja a UserDatabase függvényét
    public void updatePwd(String newPwd) {
        userDatabase.setPassword(this.id, newPwd);
    }
}