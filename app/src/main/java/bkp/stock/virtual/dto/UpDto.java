package bkp.stock.virtual.dto;

public class UpDto {
    private String up_id;
    private String title;
    private String desc;
    private String by;
    private String datetime;
    private String class_name;
    private String type;

    public UpDto() {
    }

    public UpDto(String up_id, String title, String desc, String by, String datetime, String class_name, String type) {
        this.up_id = up_id;
        this.title = title;
        this.desc = desc;
        this.by = by;
        this.datetime = datetime;
        this.class_name = class_name;
        this.type = type;
    }

    public String getUp_id() {
        return up_id;
    }

    public void setUp_id(String up_id) {
        this.up_id = up_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
