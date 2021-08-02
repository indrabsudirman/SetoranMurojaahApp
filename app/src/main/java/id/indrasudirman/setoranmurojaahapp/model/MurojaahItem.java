package id.indrasudirman.setoranmurojaahapp.model;

public class MurojaahItem {

    private int numberMurojaah;
    private String typeMurojaah;
    private String namaSurat;
    private String ayatMurojaah;

    public MurojaahItem(int iNumberMurojaah, String iTypeMurojaah, String iNamaSurat, String iAyatMurojaah) {
        numberMurojaah = iNumberMurojaah;
        typeMurojaah = iTypeMurojaah;
        namaSurat = iNamaSurat;
        ayatMurojaah = iAyatMurojaah;
    }

    public int getNumberMurojaah() {
        return numberMurojaah;
    }

    public void setNumberMurojaah(int numberMurojaah) {
        this.numberMurojaah = numberMurojaah;
    }

    public String getTypeMurojaah() {
        return typeMurojaah;
    }

    public void setTypeMurojaah(String typeMurojaah) {
        this.typeMurojaah = typeMurojaah;
    }

    public String getNamaSurat() {
        return namaSurat;
    }

    public void setNamaSurat(String namaSurat) {
        this.namaSurat = namaSurat;
    }

    public String getAyatMurojaah() {
        return ayatMurojaah;
    }

    public void setAyatMurojaah(String ayatMurojaah) {
        this.ayatMurojaah = ayatMurojaah;
    }
}
