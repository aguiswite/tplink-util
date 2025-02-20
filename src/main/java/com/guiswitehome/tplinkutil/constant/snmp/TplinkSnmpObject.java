package com.guiswitehome.tplinkutil.constant.snmp;

import lombok.Getter;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.guiswitehome.tplinkutil.constant.snmp.TpLinkSnmpObjectCategory.Poe;

@Getter
public enum TplinkSnmpObject {
    PoePortStatus(new OID("1.3.6.1.4.1.11863.6.56.1.1.2.1.1.2"), Integer32.class, "POE Status", Poe),
    PoePortWattage(new OID("1.3.6.1.4.1.11863.6.56.1.1.2.1.1.7"), Integer32.class, "POE Watts", Poe),
    PoePortCurrent(new OID("1.3.6.1.4.1.11863.6.56.1.1.2.1.1.8"), Integer32.class, "POE Current", Poe),
    PoePortVoltage(new OID("1.3.6.1.4.1.11863.6.56.1.1.2.1.1.9"), Integer32.class, "POE Voltage", Poe),
    PoePriority(new OID("1.3.6.1.4.1.11863.6.56.1.1.2.1.1.3"), Integer32.class, "POE Priority", Poe),
    PoePowerLimit(new OID("1.3.6.1.4.1.11863.6.56.1.1.2.1.1.4"), Integer32.class, "POE Power Limit", Poe),
    PoePowerStatus(new OID("1.3.6.1.4.1.11863.6.56.1.1.2.1.1.11"), Integer32.class, "POE Power Status", Poe);

    @SuppressWarnings("rawtypes")
    TplinkSnmpObject(OID baseOid, Class varClass, String objectName, TpLinkSnmpObjectCategory category) {
        this.baseOid = baseOid;
        this.varClass = varClass;
        this.objectName = objectName;
        this.category = category;
    }

    private final OID baseOid;
    final Class varClass;
    final String objectName;

    final TpLinkSnmpObjectCategory category;

    public static TplinkSnmpObject getByOid(OID oid) {
        return Arrays.stream(TplinkSnmpObject.values())
                .filter(o -> o.getBaseOid().equals(oid))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("TpLinkSnmpObject does not exist for OID: " + oid));
    }

    public static List<TplinkSnmpObject> getByCategory(TpLinkSnmpObjectCategory category) {
        return Arrays.stream(TplinkSnmpObject.values())
                .filter(s -> Poe.equals(s.category))
                .collect(Collectors.toList());
    }
}
