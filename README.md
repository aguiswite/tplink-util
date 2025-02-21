# TP-LINK POE Switch RESTful API

----

## What is it?
A java-based standalone SpringBoot app that exposes a RESTful API for interacting with a TP-Link POE switch. 
Behind the scenes, communication between the app and the switch is done via SNMP.

For example, I use the RESTful API to turn on/off specific ports on my POE switch from a script running on a NUT 
(Network UPS Tools) instance. This allows me to incrementally turn off POE WiFi access points and IP cameras to conserve 
battery power during a power outage and turn them back on once main power is restored.

## Compiling, Building, and Running

### Standalone
The project can be built using JDK 17+. which produces an executable JAR artifact. This can be started as a standalone 
app by running an embedded Tomcat server within the executable jar. Use the following command to start the app: 
`java -jar /tplink-util-0.0.1.jar`. By default, the API is exposed on port 8750.

### Docker/Container
Use the included Dockerfile to build a container that will automatically run the app upon startup. Unless specified 
differently in the docker run command, the API is exposed on port 8750.

## Endpoints
| URL/Path                                                                     | Method | Description                                                                                                               |
|------------------------------------------------------------------------------|-------------|---------------------------------------------------------------------------------------------------------------------------|
| <_hostname_>:<_port_>/api/v1/poe/{portNumber}/{powerState}                   | PUT         | Toggles a single port state by specifying the port number and state value (0=off, 1=on)                                   |
| <_hostname_>:<_port_>/api/v1/poe/range/{startPortNumber}/{endPortNumber}/{powerState} | PUT         | Toggles a port state by range. Provide the start and end port numbers in the range along with a state value (0=off, 1=on) |
| <_hostname_>:<_port_>/api/v1/poe/{portNumber}                                         | GET         | Retrieve port information for a single port number                                                                        |
| <_hostname_>:<_port_>/api/v1/poe/range/{startPortNumber}/{endPortNumber}              | GET         | Retrieves port information for a range of port numbers                                                                    |

### Example
#### http://tplink-util-server:8750/api/v1/poe/1
```javascript
{"port":1,"status":"Enabled","priority":"High","powerStatus":"On","powerLimit":300,"current":0.165,"voltage":52.4,"wattage":8.6}
```

#### http://tplink-util-server:8750/api/v1/poe/range/1/8
```javascript
[{"port":1,"status":"Enabled","priority":"High","powerStatus":"On","powerLimit":300,"current":0.134,"voltage":52.2,"wattage":7.0},{"port":2,"status":"Enabled","priority":"High","powerStatus":"On","powerLimit":300,"current":0.092,"voltage":52.1,"wattage":4.8},{"port":3,"status":"Enabled","priority":"Low","powerStatus":"On","powerLimit":300,"current":0.059,"voltage":52.2,"wattage":3.1},{"port":4,"status":"Enabled","priority":"Low","powerStatus":"On","powerLimit":300,"current":0.058,"voltage":52.3,"wattage":3.0},{"port":5,"status":"Enabled","priority":"Low","powerStatus":"On","powerLimit":300,"current":0.062,"voltage":52.0,"wattage":3.2},{"port":6,"status":"Enabled","priority":"Low","powerStatus":"On","powerLimit":300,"current":0.065,"voltage":52.1,"wattage":3.3},{"port":7,"status":"Enabled","priority":"Low","powerStatus":"On","powerLimit":300,"current":0.062,"voltage":52.2,"wattage":3.2},{"port":8,"status":"Enabled","priority":"Low","powerStatus":"Off","powerLimit":300,"current":0.0,"voltage":0.0,"wattage":0.0}]
```

## Configuration
Connection info is required to communicate with the TP-Link switch. These options can be configured as environment 
variables or by adding them to the application.properties file. When running as container, environment variable 
configuration is advantageous.

| Config Name  | Config Value                                                      |
|--------------|-------------------------------------------------------------------|
| SNMP_AUTH_PASSWORD | String value of SNMP password                                     |
| SNMP_AUTH_PRIVACY_PASSPHRASE | String value of the SNMP passphrase                               |
| SNMP_AUTH_USER | String value of the SNMP user account                             |
| SNMP_HOST | String value of the TP-Link switch IP address or hostname         |
| SNMP_PORT | String value of the TP-Link switch SNMP port (usually 161 or 162) |
<br/>

----

## Releases

v0.0.1 - Initial release
