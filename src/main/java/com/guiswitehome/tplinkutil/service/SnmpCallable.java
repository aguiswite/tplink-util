package com.guiswitehome.tplinkutil.service;

import org.snmp4j.Snmp;

@FunctionalInterface
public interface SnmpCallable {
    void call(final SnmpSessionManager sessionManager) throws Exception;
}
