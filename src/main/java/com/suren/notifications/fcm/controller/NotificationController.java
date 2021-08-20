package com.suren.notifications.fcm.controller;

import com.suren.notifications.fcm.domain.NotificationRequest;
import com.suren.notifications.fcm.firebase.FirebaseService;
import com.suren.notifications.fcm.model.Device;
import com.suren.notifications.fcm.repository.DeviceDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Surendra Kumar S
 */

@Slf4j
@RestController
@RequestMapping(value="/fcm", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {

    @Autowired
    DeviceDetailsRepository deviceDetailsRepository;

    @Autowired
    FirebaseService firebaseService;

    @GetMapping(value = "notifications")
    public ResponseEntity sendMessages() {

        log.info("Reading Notifications from DB" );

        List<Device> devices = deviceDetailsRepository.getAllDevices();

        if(null != devices && !devices.isEmpty()) {
            log.info("Total Number of devices found - {}", devices.size());

            NotificationRequest notificationRequest;
            for(Device device : devices) {
                notificationRequest = new NotificationRequest();
                notificationRequest.setToken("");
                notificationRequest.setTitle("  ");
            }
        }

        log.info("Uploading to DB");

        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "sendNotification")
    public ResponseEntity sendNotification(@RequestBody NotificationRequest request) throws ExecutionException, InterruptedException {

        log.info("Request - {}", request.toString());

        Device device = deviceDetailsRepository.getDeviceData(request);

        if(null != device && !device.getToken().isBlank()) {
            log.info("Device Data  - {}", device.toString());

            request.setToken(device.getToken());
            String response = firebaseService.sendMessageToToken(request);
            return ResponseEntity.ok().body(response);

        } else
            return new ResponseEntity("Device ID and encryption key Not Found", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping(value = "send")
    public ResponseEntity sendMessagesaa() throws ExecutionException, InterruptedException {

        log.info("Reading Notifications from DB" );

        NotificationRequest request = new NotificationRequest();

        request.setTitle("Hi");
        request.setMessage("How are you!! This is Testing !!  And Its Awesome its working !!! !  !!   !    ");
        request.setTopic("");
//        request.setToken("d4CTMzKKQemnaj4-l9PJVd:APA91bFSqgyyj_UL_1hHySVuDbd1oBkVW42Xi-WBr-5_qHwOVehGNdOZZZRvnVc_WD-eMRxU-hMZ9C-b8yginPX0UxaTR5ArItEVl9xakw3-XEX2eJPCze--l5bT-ePYwr7QqOqfcPer?userId=12342362");
//        firebaseService.sendMessageToToken(request);

//        request.setToken("epll29WoRjihQOw2_5gm2Y:APA91bEbdZI7x2PJnwX9QKBajUcbVr82ekTKmECX0loZAMA9UzLei1G31d6-XI3v5R5XHHhsahTqIMl_XqNNHYBZnxTkZ6RKF_2jFQhMiymB0uZjRdfPu3miusVaViGNn-F07sbrfQvI");
        List<Device> devices = deviceDetailsRepository.getAllDevices();

        if(null != devices && !devices.isEmpty()) {
            log.info("Total Number of devices found - {}", devices.size());

            NotificationRequest notificationRequest;
            for(Device device : devices) {
                request.setToken(device.getToken());
                firebaseService.sendMessageToToken(request);
            }
        }



        log.info("Uploading to DB");

        return ResponseEntity.ok().build();
    }

}
