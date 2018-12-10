package com.shared.interfaces;

import java.io.IOException;

public interface ReceiverMSG {
    void receiveMSG() throws IOException;
    void setActionType(RequestType requestEnum);
    RequestType getActionType();
}
