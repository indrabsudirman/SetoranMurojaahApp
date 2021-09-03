package id.indrasudirman.setoranmurojaahapp.model;

public class TampilMurojaah {

    private String tanggalMasehi;
    private String tanggalHijriah;
    private String bulanHijriah;
    private String tahunHijriah;
    private String tipeMurojaah;
    private String surat;
    private String ayat;

    //Default Constructor
    public TampilMurojaah() {
    }

    public TampilMurojaah(String tanggalMasehi, String tanggalHijriah, String bulanHijriah, String tahunHijriah, String tipeMurojaah, String surat, String ayat) {
        this.tanggalMasehi = tanggalMasehi;
        this.tanggalHijriah = tanggalHijriah;
        this.bulanHijriah = bulanHijriah;
        this.tahunHijriah = tahunHijriah;
        this.tipeMurojaah = tipeMurojaah;
        this.surat = surat;
        this.ayat = ayat;
    }

    public String getTanggalMasehi() {
        return tanggalMasehi;
    }

    public void setTanggalMasehi(String tanggalMasehi) {
        this.tanggalMasehi = tanggalMasehi;
    }

    public String getTanggalHijriah() {
        return tanggalHijriah;
    }

    public void setTanggalHijriah(String tanggalHijriah) {
        this.tanggalHijriah = tanggalHijriah;
    }

    public String getBulanHijriah() {
        return bulanHijriah;
    }

    public void setBulanHijriah(String bulanHijriah) {
        this.bulanHijriah = bulanHijriah;
    }

    public String getTahunHijriah() {
        return tahunHijriah;
    }

    public void setTahunHijriah(String tahunHijriah) {
        this.tahunHijriah = tahunHijriah;
    }

    public String getTipeMurojaah() {
        return tipeMurojaah;
    }

    public void setTipeMurojaah(String tipeMurojaah) {
        this.tipeMurojaah = tipeMurojaah;
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
