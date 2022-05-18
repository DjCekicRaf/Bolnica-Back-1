package raf.si.bolnica.management.requests;


import lombok.Setter;

@Setter
public class IstorijaBolestiRequestDTO {

    private String dijagnoza;

    public String getDijagnoza() {
        return dijagnoza;
    }
}
