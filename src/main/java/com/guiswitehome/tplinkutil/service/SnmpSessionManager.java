package com.guiswitehome.tplinkutil.service;

import com.guiswitehome.tplinkutil.data.snmp.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class SnmpSessionManager {

    @NonNull
    private Snmp snmp;
    @NonNull UserTarget target;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<OID, List<SnmpPduResponse>> set(final List<SnmpPduGroupedSetRequest> requests) throws IOException {
        final PDU pdu = new ScopedPDU();
        pdu.setType(PDU.SET);

        final Map<OID, SnmpPduSetRequest> pduRequestMap = new HashMap<>();
        final Map<OID, List<SnmpPduResponse>> groupedResponses = new HashMap<>();
        for (SnmpPduGroupedSetRequest request : requests) {
            if (!CollectionUtils.isEmpty(request.getOidSuffixes())) {
                for (Integer suffix : request.getOidSuffixes()) {
                    OID formattedOid = new OID(request.getBaseOid()).append(suffix);
                    pduRequestMap.put(formattedOid, new SnmpPduSetRequest(request.getBaseOid(), suffix, request.getValue()));
                    pdu.add(new VariableBinding(formattedOid, request.getValue()));
                }
            } else {
                final OID oid = new OID(request.getBaseOid());
                pduRequestMap.put(oid, new SnmpPduSetRequest(request.getBaseOid(), request.getValue()));
                pdu.add(new VariableBinding(oid, request.getValue()));
            }
        }

        log.info("Setting the following SNMP objects: \n {}",
                pdu.getAll().stream()
                        .map(VariableBinding::toString)
                        .collect(Collectors.joining("\n")));

        final ResponseEvent event = snmp.set(pdu, target);
        if(event != null) {
            for (VariableBinding variableBinding : event.getResponse().getAll()) {
                if (pduRequestMap.containsKey(variableBinding.getOid())) {
                    SnmpPduSetRequest request = pduRequestMap.get(variableBinding.getOid());
                    SnmpPduResponse response = new SnmpPduResponse(request.getBaseOid(),
                            request.getOidSuffix(), variableBinding.getVariable());
                    if (groupedResponses.containsKey(request.getBaseOid())) {
                        groupedResponses.get(request.getBaseOid()).add(response);
                    } else {
                        groupedResponses.put(request.getBaseOid(), new ArrayList<>(List.of(response)));
                    }
                }
            }
            return groupedResponses;
        }

        throw new RuntimeException("SET timed out");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<OID, List<SnmpPduResponse>> get(final List<SnmpPduGroupedGetRequest> requests) throws IOException {
        final PDU pdu = new ScopedPDU();
        pdu.setType(PDU.GET);

        final Map<OID, SnmpPduGetRequest> pduRequestMap = new HashMap<>();
        final Map<OID, List<SnmpPduResponse>> groupedResponses = new HashMap<>();
        for (SnmpPduGroupedGetRequest request : requests) {
            if (!CollectionUtils.isEmpty(request.getOidSuffixes())) {
                for (Integer suffix : request.getOidSuffixes()) {
                    OID formattedOid = new OID(request.getBaseOid()).append(suffix);
                    pduRequestMap.put(formattedOid, new SnmpPduGetRequest(request.getBaseOid(), suffix));
                    pdu.add(new VariableBinding(formattedOid));
                }
            } else {
                final OID oid = new OID(request.getBaseOid());
                pduRequestMap.put(oid, new SnmpPduGetRequest(request.getBaseOid()));
                pdu.add(new VariableBinding(oid));
            }
        }

        log.info("Getting the following SNMP objects: \n {}",
                pdu.getAll().stream()
                        .map(VariableBinding::getOid)
                        .map(OID::toString)
                        .collect(Collectors.joining("\n")));

        final ResponseEvent event = snmp.send(pdu, target);
        if(event != null) {

            for (VariableBinding variableBinding : event.getResponse().getAll()) {
                if (pduRequestMap.containsKey(variableBinding.getOid())) {
                    SnmpPduGetRequest request = pduRequestMap.get(variableBinding.getOid());

                    SnmpPduResponse response = new SnmpPduResponse(request.getBaseOid(),
                            request.getOidSuffix(), variableBinding.getVariable());

                    if (groupedResponses.containsKey(request.getBaseOid())) {
                        groupedResponses.get(request.getBaseOid()).add(response);
                    } else {
                        groupedResponses.put(request.getBaseOid(), new ArrayList<>(List.of(response)));
                    }
                }
            }

            return groupedResponses;
        }

        throw new RuntimeException("GET timed out");
    }

}
