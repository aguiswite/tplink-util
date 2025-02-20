package com.guiswitehome.tplinkutil.service.builder.command;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandBuilder {

    protected CommandBuilder() {}

    private final List<String> queuedCommands = new ArrayList<>();

    public PrivilegedExecSupport enable() {
        queuedCommands.add(SshCommands.ENABLE);
        return new PrivilegedExecSupport();
    }

    public List<String> build() {
        return queuedCommands;
    }

    public static CommandBuilder command() {
        return new CommandBuilder();
    }

    public interface ChainedCommandBuilder {
        List<String> build();
    }

    public abstract class BaseCommandBuilder implements ChainedCommandBuilder {
        public List<String> build() {
            return CommandBuilder.this.build();
        }
    }

    public class PrivilegedExecSupport extends BaseCommandBuilder {

        private PrivilegedExecSupport() {}

        public GlobalConfigurationSupport config() {
            queuedCommands.add(SshCommands.CONFIG);
            return new GlobalConfigurationSupport();
        }
    }

    public class GlobalConfigurationSupport extends BaseCommandBuilder {

        private GlobalConfigurationSupport() {}

        public InterfaceConfigurationSupport networkInterface(Integer portNumber) {
            queuedCommands.add(String.format(SshCommands.INTERFACE, portNumber));
            return new InterfaceConfigurationSupport(this);
        }

        public GlobalConfigurationSupport showPoeInfo() {
            queuedCommands.add(SshCommands.SHOW_POE_INFORMATION);
            return this;
        }

        public GlobalConfigurationSupport showPoeConfig() {
            queuedCommands.add(SshCommands.SHOW_POE_CONFIGURATION);
            return this;
        }
    }

    public class InterfaceConfigurationSupport extends BaseCommandBuilder {

        private InterfaceConfigurationSupport(GlobalConfigurationSupport globalConfigurationSupport) {
            this.globalConfigurationSupport = globalConfigurationSupport;
        }

        private final GlobalConfigurationSupport globalConfigurationSupport;

        public InterfaceConfigurationSupport poePortState(Integer powerState) {
            String state;
            if (NumberUtils.compare(powerState, 1) == 0) {
                state = "enable";
            } else if (NumberUtils.compare(powerState, 0) == 0) {
                state = "disable";
            } else {
                throw new RuntimeException("Invalid POE port state value");
            }
            queuedCommands.add(String.format(SshCommands.POE_PORT_STATE, state));
            return this;
        }

        public GlobalConfigurationSupport end() {
            queuedCommands.add(SshCommands.END);
            return globalConfigurationSupport;
        }
    }
}
