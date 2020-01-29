package com.hyper.ubertransport.registration.usable;

import com.hyper.ubertransport.utils.UsernameFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AddressInfo implements Serializable {

    private String street;
    private String flat;
    private String city;


    public Map<String, String> getAdressMap() {

        Map<String, String> adress = new HashMap<>();
        adress.put(UsernameFirestore.street.name(), street);

        adress.put(UsernameFirestore.flat.name(), flat);

        adress.put(UsernameFirestore.city.name(), city);

        return adress;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
