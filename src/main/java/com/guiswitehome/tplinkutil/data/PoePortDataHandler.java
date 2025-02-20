package com.guiswitehome.tplinkutil.data;

import com.guiswitehome.tplinkutil.constant.snmp.TplinkSnmpObject;
import com.guiswitehome.tplinkutil.data.snmp.SnmpPduResponse;
import com.guiswitehome.tplinkutil.model.PoePortConfig;
import com.guiswitehome.tplinkutil.model.PoePowerStatus;
import com.guiswitehome.tplinkutil.model.PoePriority;
import com.guiswitehome.tplinkutil.model.PoeStatus;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.smi.OID;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PoePortDataHandler {

    public List<PoePortConfig> parsePoeConfigResponse(Map<OID, List<SnmpPduResponse>> response) {
        Map<Integer,Map<TplinkSnmpObject, String>> traversedMap = new HashMap<>();
        response.forEach((key, value) -> value.forEach(v -> {
            if (!traversedMap.containsKey(v.getOidSuffix())) {
                traversedMap.put(v.getOidSuffix(), new HashMap<>());
            }
            traversedMap.get(v.getOidSuffix())
                    .put(TplinkSnmpObject.getByOid(v.getBaseOid()), v.getValue().toString());
        }));
        SortedSet<Integer> keys = new TreeSet<>(traversedMap.keySet());
        return keys.stream().map(k -> {
            Map<TplinkSnmpObject, String> valueMap = traversedMap.get(k);
            PoePortConfig portConfig = new PoePortConfig();
            portConfig.setPortNumber(k);
            portConfig.setStatus(PoeStatus.getPoeStatusByValue(valueMap.get(TplinkSnmpObject.PoePortStatus)));
            portConfig.setCurrent(Double.parseDouble(valueMap.get(TplinkSnmpObject.PoePortCurrent))/1000);
            portConfig.setWattage(Double.parseDouble(valueMap.get(TplinkSnmpObject.PoePortWattage))/10);
            portConfig.setVoltage(Double.parseDouble(valueMap.get(TplinkSnmpObject.PoePortVoltage))/10);
            portConfig.setPriority(PoePriority.getByValue(valueMap.get(TplinkSnmpObject.PoePriority)));
            portConfig.setPowerLimit(Integer.parseInt(valueMap.get(TplinkSnmpObject.PoePowerLimit)));
            portConfig.setPowerStatus(PoePowerStatus.getByValue(valueMap.get(TplinkSnmpObject.PoePowerStatus)));
            return portConfig;
        }).collect(Collectors.toList());
    }
}
