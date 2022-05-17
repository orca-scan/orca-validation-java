package com.validation;

import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Application {

    @RequestMapping(
        value = "/", 
        method = RequestMethod.POST)
    ResponseEntity<String> index(@RequestBody Map<String, Object> data)  throws Exception {

        // dubug purpose: show in console raw data received
        System.out.println(data);

        // NOTE:
        // orca system fields start with ___
        // you can access the value of each field using the field name (data.get("Name"), data.get("Barcode"), data.get("Location")
        String name = data.get("Name").toString();

        // validation example
        if (name.length() > 20){
            // return json error message
            JSONObject json = new JSONObject();
            json.put("title", "Invalid Name");
            json.put("message", "Name cannot contain more than 20 characters");

            //return json.toJSONString();
            return new ResponseEntity<>(json.toJSONString(), HttpStatus.OK);
        }

        // return HTTP Status 204 (No Content)
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}