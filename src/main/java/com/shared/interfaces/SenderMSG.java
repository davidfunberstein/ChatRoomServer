package com.shared.interfaces;

import java.io.IOException;

public interface SenderMSG {
    String sendMSG(String msg) throws IOException;
    void setActionType(RequestType requestEnum);
    RequestType getActionType();
}
