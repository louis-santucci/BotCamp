package com.botcamp.common.mail;

import lombok.Getter;

@Getter
public enum GmailAPIAction {
    MESSAGE_LIST(5),
    MESSAGE_GET(5),
    MESSAGE_SEND(100),
    MESSAGE_TRASH(5);


    private int cost;

    GmailAPIAction(int cost) {
        this.cost = cost;
    }


}
