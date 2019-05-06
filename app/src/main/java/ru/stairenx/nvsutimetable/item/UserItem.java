package ru.stairenx.nvsutimetable.item;

public class UserItem {

    String faculty;
    String myGroup;
    String mySubGroup;

    public UserItem(String faculty, String group, String subroup) {
        this.faculty = faculty;
        this.myGroup = group;
        this.mySubGroup = subroup;
    }

    public UserItem(String faculty, String group) {
        this.faculty = faculty;
        this.myGroup = group;
        this.mySubGroup = "subroup";
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getGroup() {
        return myGroup;
    }

    public void setGroup(String group) {
        this.myGroup = group;
    }

    public String getSubroup() {
        return mySubGroup;
    }

    public void setSubroup(String subroup) {
        this.mySubGroup = subroup;
    }
}
