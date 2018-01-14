package ep.rest;

import java.io.Serializable;
import java.util.Locale;

public class Artikel implements Serializable {
    public int idartikel, aktiviran;
    public String naziv, opis;
    public double cena;

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH,
                "%s: %s, (%.2f EUR)",
                naziv, opis, cena);
    }
}
