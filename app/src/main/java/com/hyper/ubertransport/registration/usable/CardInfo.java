package com.hyper.ubertransport.registration.usable;

import com.hyper.ubertransport.utils.UsernameFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CardInfo implements Serializable {

    private String creditCardNumber;
    private String ccv;

    public Map<String, String> getCardInfoMap() {

        Map<String, String> cardInfo = new HashMap<>();
        cardInfo.put(UsernameFirestore.creditCardNumber.name(), creditCardNumber);
        cardInfo.put(UsernameFirestore.ccv.name(), ccv);

        return cardInfo;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }
}
