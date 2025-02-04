package com.fl.findthepitch.model;

import com.fl.findthepitch.model.fieldTypeInformation.FieldType;
import com.fl.findthepitch.model.fieldTypeInformation.Price;
import com.fl.findthepitch.model.fieldTypeInformation.SurfaceType;
import java.time.LocalTime;

public class PitchData {

    private Integer zipCode;
    private String name;
    private String address;
    private String city;
    private boolean isIndoor;
    private Price isFree;
    private Double price;
    private boolean canShower;
    private boolean hasParking;
    private boolean hasLighting;
    private LocalTime openingTime;
    private LocalTime lunchBrakeStart;
    private LocalTime lunchBrakeEnd;
    private LocalTime closingTime;
    private String phoneNumber;
    private String website;
    private String email;
    private String description;
    private String image;
    private FieldType pitchType;
    private SurfaceType surfaceType;

    public PitchData() {

    }
}
