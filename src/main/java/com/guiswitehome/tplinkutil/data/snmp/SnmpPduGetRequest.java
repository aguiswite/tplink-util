package com.guiswitehome.tplinkutil.data.snmp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.snmp4j.smi.OID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SnmpPduGetRequest {

    @NonNull
    private OID baseOid;
    private Integer oidSuffix;
}
