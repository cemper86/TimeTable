package ru.stairenx.nvsutimetable.item;

public class TimeTableItem {

    String group;
    String pair;
    String discipline;
    String vid;
    String aud;
    String bilding;
    String korp;
    String teacher;
    String dol;
    String potok;
    String subgroup;
    String datezen;

    public TimeTableItem(String group, String pair, String discipline, String vid, String aud, String bilding, String korp, String teacher, String dol, String potok, String subgroup, String datezen) {
        this.group = group;
        this.pair = pair;
        this.discipline = discipline;
        this.vid = vid;
        this.aud = aud;
        this.bilding = bilding;
        this.korp = korp;
        this.teacher = teacher;
        this.dol = dol;
        this.potok = potok;
        this.subgroup = subgroup;
        this.datezen = datezen;
    }

    public TimeTableItem(String group, String pair, String discipline, String vid, String aud, String teacher, String subgroup) {
        this.group = group;
        this.pair = pair;
        this.discipline = discipline;
        this.vid = vid;
        this.aud = aud;
        this.teacher = teacher;
        this.subgroup = subgroup;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getBilding() {
        return bilding;
    }

    public void setBilding(String bilding) {
        this.bilding = bilding;
    }

    public String getKorp() {
        return korp;
    }

    public void setKorp(String korp) {
        this.korp = korp;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDol() {
        return dol;
    }

    public void setDol(String dol) {
        this.dol = dol;
    }

    public String getPotok() {
        return potok;
    }

    public void setPotok(String potok) {
        this.potok = potok;
    }

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public String getDatezen() {
        return datezen;
    }

    public void setDatezen(String datezen) {
        this.datezen = datezen;
    }
}
