package com.guiswitehome.tplinkutil.helper;

import com.guiswitehome.tplinkutil.constant.snmp.TplinkSnmpObject;
import com.guiswitehome.tplinkutil.data.snmp.SnmpPduGroupedGetRequest;
import com.guiswitehome.tplinkutil.data.snmp.SnmpPduGroupedSetRequest;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;

import java.lang.reflect.*;
import java.util.List;

@Slf4j
public class SnmpPduRequestHelper {

    public static SnmpPduGroupedGetRequest createGroupedGetRequest(TplinkSnmpObject snmpObject) {
        return createGroupedGetRequest(snmpObject, null);
    }

    public static SnmpPduGroupedGetRequest createGroupedGetRequest(TplinkSnmpObject snmpObject, List<Integer> oidSuffixes) {
        return new SnmpPduGroupedGetRequest(snmpObject.getBaseOid(), oidSuffixes);
    }

    public static SnmpPduGroupedSetRequest createGroupedSetRequest(TplinkSnmpObject snmpObject, List<Integer> oidSuffixes, String value ) {
        try {
            Variable var = getVar(snmpObject, value);
            return new SnmpPduGroupedSetRequest(snmpObject.getBaseOid(), oidSuffixes, var);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            log.error("An error occurred while creatingGroupedSetRequest", ex);
            return null;
        }
    }

    public static SnmpPduGroupedSetRequest createGroupedSetRequest(TplinkSnmpObject snmpObject, List<Integer> oidSuffixes, Integer value ) {
        try {
            Variable var = getVar(snmpObject, value);
            return new SnmpPduGroupedSetRequest(snmpObject.getBaseOid(), oidSuffixes, var);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            log.error("An error occurred while creatingGroupedSetRequest", ex);
            throw new RuntimeException("An error occurred while creatingGroupedSetRequest ", ex);
        }
    }

    public static Variable getVar(TplinkSnmpObject snmpObject, Integer value) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (snmpObject.getVarClass().isAssignableFrom(Integer32.class)) {
            return new Integer32(value);
        } else if (snmpObject.getVarClass().isAssignableFrom(UnsignedInteger32.class)) {
            return new UnsignedInteger32(value);
        } else if (snmpObject.getVarClass().isAssignableFrom(OctetString.class)) {
            return new OctetString(String.valueOf(value));
        } else {
            throw new RuntimeException("Cannot create variable from Integer " + value);
        }
    }

    public static Variable getVar(TplinkSnmpObject snmpObject, String value) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (snmpObject.getVarClass().isAssignableFrom(Integer32.class)) {
            Integer32 integer32 = new Integer32();
            integer32.setValue(value);
            return integer32;
        } else if (snmpObject.getVarClass().isAssignableFrom(UnsignedInteger32.class)) {
            UnsignedInteger32 unsignedInteger32 = new UnsignedInteger32();
            unsignedInteger32.setValue(value);
            return unsignedInteger32;
        } else if (snmpObject.getVarClass().isAssignableFrom(OctetString.class)) {
            return new OctetString(value);
        } else {
            throw new RuntimeException("Cannot create variable from String " + value);
        }
    }

    private static Variable getInstance(TplinkSnmpObject snmpObject) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Variable> varClass = snmpObject.getVarClass();
        Constructor<Variable> varConstruct = varClass.getConstructor();
        Variable var = varConstruct.newInstance();
        return var;
    }
}
