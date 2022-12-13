package com.example.demo.Transportation.Domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusTerminal {
    private String terminalId;
    private String terminalNm;

    public BusTerminal(String terminalId, String terminalNm) {
        this.terminalId = terminalId;
        this.terminalNm = terminalNm;
    }
}
