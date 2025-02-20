package com.guiswitehome.tplinkutil.model.snmp;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

@Data
@AllArgsConstructor
public class SnmpResponse {

    private OID oid;
    private VariableBinding variableBinding;
}
