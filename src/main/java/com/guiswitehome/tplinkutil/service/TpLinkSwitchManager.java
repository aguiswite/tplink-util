package com.guiswitehome.tplinkutil.service;

import com.guiswitehome.tplinkutil.constant.snmp.TpLinkSnmpObjectCategory;
import com.guiswitehome.tplinkutil.constant.snmp.TplinkSnmpObject;
import com.guiswitehome.tplinkutil.data.PoePortDataHandler;
import com.guiswitehome.tplinkutil.data.snmp.SnmpPduGroupedGetRequest;
import com.guiswitehome.tplinkutil.data.snmp.SnmpPduGroupedSetRequest;
import com.guiswitehome.tplinkutil.data.snmp.SnmpPduResponse;
import com.guiswitehome.tplinkutil.helper.SnmpPduRequestHelper;
import com.guiswitehome.tplinkutil.model.PoePortConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.smi.OID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.guiswitehome.tplinkutil.constant.snmp.TplinkSnmpObject.*;

@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TpLinkSwitchManager {

    private SnmpConnectionManager snmpConnectionManager;
    private final PoePortDataHandler poePortDataHandler;

    public PoePortConfig togglePoeState(Integer portNumber, Integer powerState) {
        log.info("Toggling POE Port Number{} with power state {}", portNumber, powerState);
        PoePortConfig portConfig = null;
        try {
            snmpConnectionManager.withConnection((SnmpSessionManager sessionManager) -> {
                SnmpPduGroupedSetRequest request = SnmpPduRequestHelper.createGroupedSetRequest(PoePortStatus,
                        IntStream.rangeClosed(portNumber, portNumber).boxed().toList(), powerState);
                sessionManager.set(List.of(request));
            });
            portConfig = getPoeConfiguration(portNumber);
        } catch (Exception e) {
            log.error("SNMP ERROR", e);
        }
        return portConfig;
    }

    public List<PoePortConfig> togglePoeState(final Integer startPortNumber, final Integer endPortNumber, final Integer powerState) {
        log.info("Toggling POE Range {}-{} with power state {}", startPortNumber, endPortNumber, powerState);
        List<PoePortConfig> portConfigs = new ArrayList<>();
        try {
            snmpConnectionManager.withConnection((SnmpSessionManager sessionManager) -> {
                SnmpPduGroupedSetRequest request = SnmpPduRequestHelper.createGroupedSetRequest(PoePortStatus,
                        IntStream.rangeClosed(startPortNumber, endPortNumber).boxed().toList(), powerState);
                sessionManager.set(List.of(request));
            });
            portConfigs.addAll(getPoeConfiguration(startPortNumber, endPortNumber));
        } catch (Exception e) {
            log.error("SNMP ERROR", e);
        }
        return portConfigs;
    }

    public PoePortConfig getPoeConfiguration(final Integer portNumber) {
        log.info("Getting POE Port Configuration for port {}", portNumber);
        return getPoeConfiguration(portNumber, portNumber).get(0);
    }

    public List<PoePortConfig> getPoeConfiguration(final Integer startPortNumber, final Integer endPortNumber) {
        log.info("Getting POE Port Configuration for ports {} to {}", startPortNumber, endPortNumber);
        List<PoePortConfig> portConfigs = new ArrayList<>();
        try {
            snmpConnectionManager.withConnection((SnmpSessionManager sessionManager) -> {
                List<SnmpPduGroupedGetRequest> requests = TplinkSnmpObject.getByCategory(TpLinkSnmpObjectCategory.Poe).stream()
                        .map(o -> SnmpPduRequestHelper.createGroupedGetRequest(o,
                                IntStream.rangeClosed(startPortNumber, endPortNumber).boxed().toList()))
                        .toList();
                Map<OID, List<SnmpPduResponse>> response = sessionManager.get(requests);
                portConfigs.addAll(poePortDataHandler.parsePoeConfigResponse(response));
            });
        } catch (Exception e) {
            log.error("SNMP ERROR", e);
        }
        return portConfigs;
    }
}
