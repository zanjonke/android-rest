package ep.rest;

import java.io.Serializable;
import java.util.Locale;

public class Stranka implements Serializable {

    public String idstranka;
    public String ime;
    public String priimek;
    public String email;
    public String telefon;
    public String naslov;
    public String aktiviran;
    public int tip;

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH,
                "%s: %s, %s, %s, %s, %d ",
                idstranka, ime, priimek, email, aktiviran, tip);
    }
}
