package ru.stairenx.nvsutimetable.item;

public class PairItem {

    private String GRUP;
    private String PAIR;
    private String DISCIPLINE;
    private String VID;
    private String AUD;
    private String BUILDING;
    private String KORP;
    private String TEACHER;
    private String DOL;
    private String POTOK;
    private String SUBGRUP;
    private String DATEZAN;

    private String TIME;

    public PairItem(String GRUP, String PAIR, String DISCIPLINE, String VID, String AUD, String TEACHER) {
        this.GRUP = GRUP;
        this.PAIR = PAIR;
        this.DISCIPLINE = DISCIPLINE;
        this.VID = VID;
        this.AUD = AUD;
        this.TEACHER = TEACHER;
    }

    public PairItem(String GRUP, String PAIR, String TIME, String DISCIPLINE, String VID, String AUD,String POTOK, String TEACHER) {
        this.GRUP = GRUP;
        this.PAIR = PAIR;
        this.TIME = TIME;
        this.DISCIPLINE = DISCIPLINE;
        this.VID = VID;
        this.AUD = AUD;
        this.POTOK = POTOK;
        this.TEACHER = TEACHER;
    }

    public PairItem(String GRUP, String PAIR, String DISCIPLINE, String VID, String AUD, String BUILDING, String KORP, String TEACHER, String DOL, String POTOK, String SUBGRUP, String DATEZAN) {
        this.GRUP = GRUP;
        this.PAIR = PAIR;
        this.DISCIPLINE = DISCIPLINE;
        this.VID = VID;
        this.AUD = AUD;
        this.BUILDING = BUILDING;
        this.KORP = KORP;
        this.TEACHER = TEACHER;
        this.DOL = DOL;
        this.POTOK = POTOK;
        this.SUBGRUP = SUBGRUP;
        this.DATEZAN = DATEZAN;
    }

    public String getGRUP() {
        return GRUP;
    }

    public void setGRUP(String GRUP) {
        this.GRUP = GRUP;
    }

    public String getPAIR() {
        return PAIR;
    }

    public void setPAIR(String PAIR) {
        this.PAIR = PAIR;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getDISCIPLINE() {
        return DISCIPLINE;
    }

    public void setDISCIPLINE(String DISCIPLINE) {
        this.DISCIPLINE = DISCIPLINE;
    }

    public String getVID() {
        return VID;
    }

    public void setVID(String VID) {
        this.VID = VID;
    }

    public String getAUD() {
        return AUD;
    }

    public void setAUD(String AUD) {
        this.AUD = AUD;
    }

    public String getBUILDING() {
        return BUILDING;
    }

    public void setBUILDING(String BUILDING) {
        this.BUILDING = BUILDING;
    }

    public String getKORP() {
        return KORP;
    }

    public void setKORP(String KORP) {
        this.KORP = KORP;
    }

    public String getTEACHER() {
        return TEACHER;
    }

    public void setTEACHER(String TEACHER) {
        this.TEACHER = TEACHER;
    }

    public String getDOL() {
        return DOL;
    }

    public void setDOL(String DOL) {
        this.DOL = DOL;
    }

    public String getPOTOK() {
        return POTOK;
    }

    public void setPOTOK(String POTOK) {
        this.POTOK = POTOK;
    }

    public String getSUBGRUP() {
        return SUBGRUP;
    }

    public void setSUBGRUP(String SUBGRUP) {
        this.SUBGRUP = SUBGRUP;
    }

    public String getDATEZAN() {
        return DATEZAN;
    }

    public void setDATEZAN(String DATEZAN) {
        this.DATEZAN = DATEZAN;
    }
}
