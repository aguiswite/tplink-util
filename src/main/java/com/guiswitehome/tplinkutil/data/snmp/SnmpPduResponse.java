package com.guiswitehome.tplinkutil.data.snmp;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

@Data
@AllArgsConstructor
public class SnmpPduResponse {

    public OID baseOid;
    public Integer oidSuffix;
    public Variable value;
}
