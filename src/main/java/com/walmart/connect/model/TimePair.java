package com.walmart.connect.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor

public class TimePair {


    private  Date key;
    private  Date value;



    public TimePair(Date aKey, Date aValue)
    {
        key   = aKey;
        value = aValue;
    }

    public Date getKey()   { return key; }
    public Date getValue() { return value; }

    @Override
    public String toString() {
        return
                "key=" + key +
                        "\n"+
                ", value=" + value
                ;
    }
}
