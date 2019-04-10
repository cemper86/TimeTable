package ru.stairenx.nvsutimetable.database;

public class TimetableTable {

    public Long _id;
    public String group;
    public String pair;
    public String discipline;
    public String vid;
    public String aud;
    public String bilding;
    public String korp;
    public String teacher;
    public String dol;
    public String potok;
    public String subgroup;
    public String datezen;

    public TimetableTable(String group, String pair, String discipline, String vid, String aud, String bilding, String korp, String teacher, String dol, String potok, String subgroup, String datezen) {
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

    @Override
    public String toString() {
        return "TimetableTable{" +
                "_id=" + _id +
                ", group='" + group + '\'' +
                ", pair='" + pair + '\'' +
                ", discipline='" + discipline + '\'' +
                ", vid='" + vid + '\'' +
                ", aud='" + aud + '\'' +
                ", bilding='" + bilding + '\'' +
                ", korp='" + korp + '\'' +
                ", teacher='" + teacher + '\'' +
                ", dol='" + dol + '\'' +
                ", potok='" + potok + '\'' +
                ", subgroup='" + subgroup + '\'' +
                ", datezen='" + datezen + '\'' +
                '}';
    }
}
