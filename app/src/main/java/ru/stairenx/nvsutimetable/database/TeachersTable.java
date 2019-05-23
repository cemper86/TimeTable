package ru.stairenx.nvsutimetable.database;

public class TeachersTable {

    Long _id;
    String name;
    String staffer;


    public TeachersTable(String name, String staffer) {
        this.name = name;
        this.staffer = staffer;
    }

    public TeachersTable() {
        this.name = "";
        this.staffer = "";
    }

    public String getNameTeacher(){
        return name;
    }

    @Override
    public String toString() {
        return "UserTable{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", staffer='" + staffer + '\'' +
                '}';
    }
}
