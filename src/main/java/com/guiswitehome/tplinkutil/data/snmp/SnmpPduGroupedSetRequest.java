package com.guiswitehome.tplinkutil.data.snmp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SnmpPduGroupedSetRequest {

    @NonNull
    private OID baseOid;
    private List<Integer> oidSuffixes;
    @NonNull
    private Variable value;
}
