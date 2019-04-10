package ru.stairenx.nvsutimetable.database;

public class UserTable {

    Long _id;
    String faculty;
    String group;
    String subgroup;


    public UserTable(String faculty, String group) {
        this.faculty = faculty;
        this.group = group;
    }

    public UserTable(String faculty, String group, String subgroup) {
        this.faculty = faculty;
        this.group = group;
        this.subgroup = subgroup;
    }

    @Override
    public String toString() {
        return "UserTable{" +
                "_id=" + _id +
                ", faculty='" + faculty + '\'' +
                ", group='" + group + '\'' +
                ", subgroup='" + subgroup + '\'' +
                '}';
    }
}
