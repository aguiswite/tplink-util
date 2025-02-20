package com.guiswitehome.tplinkutil.data.snmp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.snmp4j.smi.OID;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SnmpPduGroupedGetRequest {

    @NonNull
    private OID baseOid;
    private List<Integer> oidSuffixes;
}
