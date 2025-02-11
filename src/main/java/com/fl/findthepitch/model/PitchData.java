package com.fl.findthepitch.model;

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
    private PitchType pitchType;
    private SurfaceType surfaceType;

    private PitchData(Builder builder) {
        this.name = builder.name;
        this.address = builder.address;
        this.city = builder.city;
        this.isIndoor = builder.isIndoor;
        this.isFree = builder.isFree;
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

        public Builder isIndoor(boolean isIndoor) {
            this.isIndoor = isIndoor;
            return this;
        }

        public Builder isFree(Price isFree) {
            this.isFree = isFree;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
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
}