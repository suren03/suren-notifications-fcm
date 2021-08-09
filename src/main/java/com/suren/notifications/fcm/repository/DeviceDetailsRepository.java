package com.suren.notifications.fcm.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.suren.notifications.fcm.model.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Surendra Kumar S
 */

@Slf4j
@Repository
public class DeviceDetailsRepository {

    @Autowired
    private MongoDatabase mongoDB;

    private MongoCollection<Device> deviceCollection;


    @PostConstruct
    void init() {
        this.deviceCollection = mongoDB.getCollection("suren-device-details", Device.class);
    }

    public List<Device> getAllDevices() {
        return deviceCollection
                .find()
                .projection(Projections.include("token"))
                .into(new ArrayList<>());

    }


}
