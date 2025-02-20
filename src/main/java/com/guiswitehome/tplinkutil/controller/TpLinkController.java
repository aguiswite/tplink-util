package com.guiswitehome.tplinkutil.controller;

import com.guiswitehome.tplinkutil.model.PoePortConfig;
import com.guiswitehome.tplinkutil.service.TpLinkSwitchManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TpLinkController {

    private final TpLinkSwitchManager switchManager;

    @RequestMapping(value = "/poe/{portNumber}/{powerState}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<PoePortConfig> togglePortPower(@PathVariable(value = "portNumber") Integer portNumber,
                                                  @PathVariable(value = "powerState") Integer powerState) {
        PoePortConfig result = switchManager.togglePoeState(portNumber, powerState);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/poe/range/{startPortNumber}/{endPortNumber}/{powerState}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<List<PoePortConfig>> togglePortPower(@PathVariable(value = "startPortNumber") Integer startPortNumber,
                                                  @PathVariable(value = "endPortNumber") Integer endPortNumber,
                                                  @PathVariable(value = "powerState") Integer powerState) {
        List<PoePortConfig> result = switchManager.togglePoeState(startPortNumber, endPortNumber, powerState);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/poe/{portNumber}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<PoePortConfig> getPoeConfiguration(@PathVariable(value = "portNumber") Integer portNumber) {
        PoePortConfig result = switchManager.getPoeConfiguration(portNumber);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/poe/range/{startPortNumber}/{endPortNumber}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<PoePortConfig>> getPoeConfiguration(@PathVariable(value = "startPortNumber") Integer startPortNumber,
                                                               @PathVariable(value = "endPortNumber") Integer endPortNumber) {
        List<PoePortConfig> result = switchManager.getPoeConfiguration(startPortNumber, endPortNumber);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
