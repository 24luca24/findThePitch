package com.fl.findthepitch.model;

import com.fl.findthepitch.model.fieldTypeInformation.AreaType;
import com.fl.findthepitch.model.fieldTypeInformation.PitchType;
import com.fl.findthepitch.model.fieldTypeInformation.Price;
import com.fl.findthepitch.model.fieldTypeInformation.SurfaceType;

import java.io.Serializable;
import java.time.LocalTime;

public class PitchData implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String address;
    private String city;
    private AreaType areaType;
    private Price price;
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
    private PitchType pitchType;
    private SurfaceType surfaceType;

    private PitchData(Builder builder) {
        this.name = builder.name;
        this.address = builder.address;
        this.city = builder.city;
        this.areaType = builder.areaType;
        this.price = builder.price;
        this.canShower = builder.canShower;
        this.hasParking = builder.hasParking;
        this.hasLighting = builder.hasLighting;
        this.openingTime = builder.openingTime;
        this.lunchBrakeStart = builder.lunchBrakeStart;
        this.lunchBrakeEnd = builder.lunchBrakeEnd;
        this.closingTime = builder.closingTime;
        this.phoneNumber = builder.phoneNumber;
        this.website = builder.website;
        this.email = builder.email;
        this.description = builder.description;
        this.image = builder.image;
        this.pitchType = builder.pitchType;
        this.surfaceType = builder.surfaceType;
    }

    public static class Builder {
        private String name;
        private String address;
        private String city;
        private AreaType areaType;
        private Price price;
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
        private PitchType pitchType;
        private SurfaceType surfaceType;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder areaType(AreaType area) {
            this.areaType = area;
            return this;
        }
        public Builder price(Price isFree) {
            this.price = isFree;
            return this;
        }

        public Builder canShower(boolean canShower) {
            this.canShower = canShower;
            return this;
        }

        public Builder hasParking(boolean hasParking) {
            this.hasParking = hasParking;
            return this;
        }

        public Builder hasLighting(boolean hasLighting) {
            this.hasLighting = hasLighting;
            return this;
        }

        public Builder openingTime(LocalTime openingTime) {
            this.openingTime = openingTime;
            return this;
        }

        public Builder lunchBrakeStart(LocalTime lunchBrakeStart) {
            this.lunchBrakeStart = lunchBrakeStart;
            return this;
        }

        public Builder lunchBrakeEnd(LocalTime lunchBrakeEnd) {
            this.lunchBrakeEnd = lunchBrakeEnd;
            return this;
        }

        public Builder closingTime(LocalTime closingTime) {
            this.closingTime = closingTime;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder website(String website) {
            this.website = website;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public Builder pitchType(PitchType pitchType) {
            this.pitchType = pitchType;
            return this;
        }

        public Builder surfaceType(SurfaceType surfaceType) {
            this.surfaceType = surfaceType;
            return this;
        }

        public PitchData build() {
            return new PitchData(this);
        }
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public AreaType areaType() {
        return areaType;
    }

    public Price getPrice() {
        return price;
    }

    public boolean isCanShower() {
        return canShower;
    }

    public boolean isHasParking() {
        return hasParking;
    }

    public boolean isHasLighting() {
        return hasLighting;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getLunchBrakeStart() {
        return lunchBrakeStart;
    }

    public LocalTime getLunchBrakeEnd() {
        return lunchBrakeEnd;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public PitchType getPitchType() {
        return pitchType;
    }

    public SurfaceType getSurfaceType() {
        return surfaceType;
    }
}