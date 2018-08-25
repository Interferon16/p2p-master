package com.simple_p2p.p2p_engine.Message;

public enum MessageType {
    TEXT,
    DATA,
    BOOTSTRAP,
    HANDSHAKE,
    PING,
    FIND,
    LIST_OF_FILES,
    GET_FILE_LIST,
    GET_FILE_PART,
    ANSWER
}
