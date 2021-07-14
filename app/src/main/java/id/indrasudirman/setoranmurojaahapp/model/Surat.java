package id.indrasudirman.setoranmurojaahapp.model;

public class Surat {

    private long suratKe;
    private String namaSurat;
    private String jumlahAyat;

    public Surat(long suratKe, String namaSurat, String jumlahAyat) {
        this.suratKe = suratKe;
        this.namaSurat = namaSurat;
        this.jumlahAyat = jumlahAyat;
    }

    public long getSuratKe() {
        return suratKe;
    }

    public void setSuratKe(long suratKe) {
        this.suratKe = suratKe;
    }

    public String getNamaSurat() {
        return namaSurat;
    }

    public void setNamaSurat(String namaSurat) {
        this.namaSurat = namaSurat;
    }

    public String getJumlahAyat() {
        return jumlahAyat;
    }

    public void setJumlahAyat(String jumlahAyat) {
        this.jumlahAyat = jumlahAyat;
    }
}
