package ForLogin;

public class Login {
    private String login;
    private String password;
    public Login(String login, String password) {
        this.password = password;
        this.login = login;
    }

    public Login() {
    }

    public String getPassword() {
        return password;
    }

    public  String getLogin() {
        return login;
    }

    public void setPassword(String about) {
        this.password = about;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
