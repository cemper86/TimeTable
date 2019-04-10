package ru.stairenx.nvsutimetable.item;

public class UserItem {

    String faculty;
    String group;
    String subroup;

    public UserItem(String faculty, String group, String subroup) {
        this.faculty = faculty;
        this.group = group;
        this.subroup = subroup;
    }

    public UserItem(String faculty, String group) {
        this.faculty = faculty;
        this.group = group;
        this.subroup = "subroup";
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSubroup() {
        return subroup;
    }

    public void setSubroup(String subroup) {
        this.subroup = subroup;
    }
}
