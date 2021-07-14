package id.indrasudirman.setoranmurojaahapp.model;

public class Murojaah {

    private int murojaahId;
    private String typeMurojaah;
    private String dateMasehi;
    private String dateHijri;
    private String surat;
    private String ayat;

    //Default constructor
    public Murojaah() {
    }

    public int getMurojaahId() {
        return murojaahId;
    }

    public void setMurojaahId(int murojaahId) {
        this.murojaahId = murojaahId;
    }

    public String getTypeMurojaah() {
        return typeMurojaah;
    }

    public void setTypeMurojaah(String typeMurojaah) {
        this.typeMurojaah = typeMurojaah;
    }

    public String getDateMasehi() {
        return dateMasehi;
    }

    public void setDateMasehi(String dateMasehi) {
        this.dateMasehi = dateMasehi;
    }

    public String getDateHijri() {
        return dateHijri;
    }

    public void setDateHijri(String dateHijri) {
        this.dateHijri = dateHijri;
    }

    public String getSurat() {
        return surat;
    }

    public void setSurat(String surat) {
        this.surat = surat;
    }

    public String getAyat() {
        return ayat;
    }

    public void setAyat(String ayat) {
        this.ayat = ayat;
    }
}
