package id.indrasudirman.setoranmurojaahapp.model;

public class MurojaahItem {


    private String typeMurojaah;
    private String namaSurat;
    private String ayatMurojaah;

    public MurojaahItem(String iTypeMurojaah, String iNamaSurat, String iAyatMurojaah) {
        typeMurojaah = iTypeMurojaah;
        namaSurat = iNamaSurat;
        ayatMurojaah = iAyatMurojaah;
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
