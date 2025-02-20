package com.guiswitehome.tplinkutil.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@NoArgsConstructor
public class SnmpConnectionManager {

    @Value("${SNMP_AUTH_USER}")
    private String snmpUser;
    @Value("${SNMP_AUTH_PASSWORD}")
    private String snmpPassword;
    @Value("${SNMP_AUTH_PRIVACY_PASSPHRASE}")
    private String snmpPrivacyPassphrase;
    @Value("${SNMP_HOST}")
    private String snmpHost;
    @Value("${SNMP_PORT}")
    private String snmpPort;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void withConnection(SnmpCallable snmpCallable) {
        log.info("Creating SNMP connection");
        TransportMapping<? extends Address> transport = null;
        Snmp snmp = null;
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);

            OctetString localEngineId = new OctetString(MPv3.createLocalEngineID());
            USM usm = new USM(SecurityProtocols.getInstance(), localEngineId, 0);
            SecurityModels.getInstance().addSecurityModel(usm);

            SecurityProtocols.getInstance().addAuthenticationProtocol(new AuthSHA());
            SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

            OctetString securityName = new OctetString(snmpUser);
            OID authProtocol = AuthSHA.ID;
            OID privProtocol = PrivDES.ID;
            OctetString authPassphrase = new OctetString(snmpPassword);
            OctetString privPassphrase = new OctetString(snmpPrivacyPassphrase);

            snmp.getUSM().addUser(securityName, new UsmUser(securityName, authProtocol, authPassphrase, privProtocol, privPassphrase));

            UserTarget target = new UserTarget();
            target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            target.setSecurityName(securityName);
            target.setAddress(GenericAddress.parse(String.format("udp:%s/%s", snmpHost, snmpPort)));
            target.setVersion(SnmpConstants.version3);
            target.setRetries(2);
            target.setTimeout(60000);

            transport.listen();

            log.info("Established SNMP connection to " + snmpHost);

            snmpCallable.call(new SnmpSessionManager(snmp, target));

        } catch (Exception ex) {
            log.error("Error connecting and executing snmp commands", ex);
        } finally {
            log.info("Closing SNMP connection");
            try {
                if (transport != null && transport.isListening()) {
                    transport.close();
                }

                if (snmp != null) {
                    snmp.close();
                }
            } catch (IOException ioException) {
                log.error("Error closing snmp connection", ioException);
            }
            log.info("SNMP connection closed");
        }

    }
}
