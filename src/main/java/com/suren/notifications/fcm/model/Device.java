package com.suren.notifications.fcm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonId;

/**
 * @author Surendra Kumar S
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Device {

    @BsonId
    private String userId;
    private String token;


}
