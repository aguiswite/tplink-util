package com.guiswitehome.tplinkutil.data.snmp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SnmpPduSetRequest {

    @NonNull
    private OID baseOid;
    private Integer oidSuffix;
    @NonNull
    private Variable value;
}
