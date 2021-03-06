# orca-validation-java

Example of [how to validate barcode scans in real-time](https://orcascan.com/guides/how-to-validate-barcode-scans-in-real-time-56928ff9) in using [Java](https://www.java.com/) and [Spring boot](https://spring.io/projects/spring-boot) framework.

## Install

First ensure you have [Java](https://www.java.com/) and [Maven](https://maven.apache.org/install.html) installed.

Then execute the following:

```bash
# download this example code
git clone https://github.com/orca-scan/orca-validation-java.git

# go into the new directory
cd orca-validation-java

# install dependecies
mvn package
```

## Run

```bash
# start the project
mvn spring-boot:run
```

Your server will now be running on port 8080.

You can emulate an Orca Scan Validation input using [cURL](https://dev.to/ibmdeveloper/what-is-curl-and-why-is-it-all-over-api-docs-9mh) by running the following:

```bash
curl --location --request POST 'http://127.0.0.1:8080/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "___orca_sheet_name": "Vehicle Checks",
    "___orca_user_email": "hidden@requires.https",
    "Barcode": "orca-scan-test",
    "Date": "2022-04-19T16:45:02.851Z",
    "Name": "Orca Scan Validation"
}'
```

### Important things to note

1. Only Orca Scan system fields start with `___`
2. Properties in the JSON payload are an exact match to the  field names in your sheet _(case and space)_

## How this example works

This [example](src/main//java/com/Application.java) uses the [srping boot](https://spring.io/projects/spring-boot) framework:

### Validation example

```java
@RequestMapping(
    value = "/", 
    method = RequestMethod.POST)
ResponseEntity<String> index(@RequestBody Map<String, Object> data)  throws Exception {

    // debug purpose: show in console raw data received
    System.out.println(data);

    // NOTE:
    // orca system fields start with ___
    // you can access the value of each field using the field name (data.get("Name"), data.get("Barcode"), data.get("Location")
    String name = data.get("Name").toString();

    // validation example
    if (name.length() > 20){
        // return error message
        JSONObject json = new JSONObject();
        json.put("title", "Invalid Name");
        json.put("message", "Name cannot contain more than 20 characters");

        return new ResponseEntity<>(json.toJSONString(), HttpStatus.OK);
    }

    // return HTTP Status 200 with no body
    return new ResponseEntity<>("", HttpStatus.OK);
}
```

## Test server against Orca Cloud

To expose the server securely from localhost and test it easily agaisnt the real Orca Cloud environment you can use [Secure Tunnels](https://ngrok.com/docs/secure-tunnels#what-are-ngrok-secure-tunnels). Take a look at [Ngrok](https://ngrok.com/) or [Cloudflare](https://www.cloudflare.com/).

```bash
ngrok http 8080
```

## Troubleshooting

If you run into any issues not listed here, please [open a ticket](https://github.com/orca-scan/orca-validation-java/issues).

## Examples in other langauges
* [orca-validation-dotnet](https://github.com/orca-scan/orca-validation-dotnet)
* [orca-validation-python](https://github.com/orca-scan/orca-validation-python)
* [orca-validation-go](https://github.com/orca-scan/orca-validation-go)
* [orca-validation-java](https://github.com/orca-scan/orca-validation-java)
* [orca-validation-php](https://github.com/orca-scan/orca-validation-php)
* [orca-validation-node](https://github.com/orca-scan/orca-validation-node)

## History

For change-log, check [releases](https://github.com/orca-scan/orca-validation-java/releases).

## License

&copy; Orca Scan, the [Barcode Scanner app for iOS and Android](https://orcascan.com).