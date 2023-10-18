package bkp.stock.virtual.dto;

public class UserDto {
    private String uid;
    private String name;
    private String type;
    private String email;
    private String pass;
    private String rollno;
    private String classname;
    private String timestamp;

    public UserDto() {
    }

    public UserDto(String uid, String name, String type, String email, String pass, String rollno, String classname, String timestamp) {
        this.uid = uid;
        this.name = name;
        this.type = type;
        this.email = email;
        this.pass = pass;
        this.rollno = rollno;
        this.classname = classname;
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
