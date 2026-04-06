package com.validation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Server {

    // validate is called every time Orca Scan sends a barcode scan to your server.
    // It reads the scan data, applies your validation logic, and tells Orca Scan
    // whether to save the data, reject it, or change it before saving.
    @PostMapping("/")
    ResponseEntity<Object> validate(
            @RequestBody Map<String, Object> data,
            @RequestHeader(value = "orca-secret", required = false) String secret) {

        // Fields starting with ___ are Orca system fields (e.g. ___orca_sheet_name).
        // All other fields match your sheet column names exactly (case and spaces matter).
        // For example: data.get("Barcode"), data.get("Name"), data.get("___orca_sheet_name")
        String name = data.get("Name") instanceof String ? (String) data.get("Name") : "";

        // ---------------------------------------------------------------
        // OPTION 1: Reject the scan and show an error dialog in the app.
        // Return HTTP 400 with an ___orca_message to block the save and
        // display the message to the user. They must dismiss the dialog
        // before they can try again.
        // ---------------------------------------------------------------
        if (name.length() > 20) {
            Map<String, Object> message = new HashMap<>();
            message.put("display", "dialog");
            message.put("type", "error");
            message.put("title", "Invalid Name");
            message.put("message", "Name cannot be longer than 20 characters");

            Map<String, Object> body = new HashMap<>();
            body.put("___orca_message", message);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        // ---------------------------------------------------------------
        // OPTION 2: Modify the data before it saves.
        // Return HTTP 200 with only the fields you want to change.
        // Orca Scan will update those fields and allow the save.
        // ---------------------------------------------------------------
        // Map<String, Object> updated = new HashMap<>();
        // updated.put("Name", name.trim()); // example: trim whitespace or reformat the value
        // return ResponseEntity.ok(updated);

        // ---------------------------------------------------------------
        // OPTION 3: Show a success notification (green banner in the app).
        // The data still saves - this just gives the user feedback.
        // Return HTTP 200 with an ___orca_message to show the notification.
        // ---------------------------------------------------------------
        // Map<String, Object> notification = new HashMap<>();
        // notification.put("display", "notification");
        // notification.put("type", "success");
        // notification.put("message", "Barcode scanned successfully");
        // Map<String, Object> notificationBody = new HashMap<>();
        // notificationBody.put("___orca_message", notification);
        // return ResponseEntity.ok(notificationBody);

        // ---------------------------------------------------------------
        // SECURITY: Verify the request came from your specific Orca sheet.
        // Set a secret in Orca Scan (Integrations > Events API > Secret)
        // then check it matches here before trusting the data.
        // ---------------------------------------------------------------
        // String orcaSecret = System.getenv("ORCA_SECRET");
        // if (orcaSecret != null && !orcaSecret.equals(secret)) {
        //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        // }

        // All good - return HTTP 204 to allow the data to save with no changes.
        // HTTP 204 means "success, no content" - Orca Scan will save the data as-is.
        return ResponseEntity.noContent().build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
