package ru.stairenx.nvsutimetable.database;

public class UserTable {

    Long _id;
    String faculty;
    String myGroup;
    String mySubGroup;


    public UserTable(String faculty, String group) {
        this.faculty = faculty;
        this.myGroup = group;
    }

    public UserTable(String faculty, String group, String subgroup) {
        this.faculty = faculty;
        this.myGroup = group;
        this.mySubGroup = subgroup;
    }

    public UserTable(){

    }

    @Override
    public String toString() {
        return "UserTable{" +
                "_id=" + _id +
                ", faculty='" + faculty + '\'' +
                ", group='" + myGroup + '\'' +
                ", subgroup='" + mySubGroup + '\'' +
                '}';
    }
}
