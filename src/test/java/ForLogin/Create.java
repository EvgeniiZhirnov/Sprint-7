package ForLogin;

public class Create {
    private String name;
    private String password;

    private String login;

    public Create(String name, String password, String login) {
        this.name = name;
        this.password = password;
        this.login = login;
    }

    public Create() {
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public  String getLogin() {
        return login;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String about) {
        this.password = about;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
