package bkp.stock.virtual.dto;

public class AssignDto {
    private String ass_id;
    private String title;
    private String desc;
    private String ass_subject;
    private String class_name;
    private String last_date;
    private String datetime;
    private String created_by;
    private String created_by_type;

    public AssignDto() {
    }

    public AssignDto(String ass_id, String title, String desc, String ass_subject, String class_name, String last_date, String datetime, String created_by, String created_by_type) {
        this.ass_id = ass_id;
        this.title = title;
        this.desc = desc;
        this.ass_subject = ass_subject;
        this.class_name = class_name;
        this.last_date = last_date;
        this.datetime = datetime;
        this.created_by = created_by;
        this.created_by_type = created_by_type;
    }

    public String getAss_id() {
        return ass_id;
    }

    public void setAss_id(String ass_id) {
        this.ass_id = ass_id;
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

    public String getAss_subject() {
        return ass_subject;
    }

    public void setAss_subject(String ass_subject) {
        this.ass_subject = ass_subject;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getLast_date() {
        return last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_by_type() {
        return created_by_type;
    }

    public void setCreated_by_type(String created_by_type) {
        this.created_by_type = created_by_type;
    }
}
