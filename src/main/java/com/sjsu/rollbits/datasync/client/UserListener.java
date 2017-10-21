package com.sjsu.rollbits.datasync.client;

import routing.Pipe;

public class UserListener  implements CommListener{
    @Override
    public String getListenerID() {
        return null;
    }

    @Override
    public void onMessage(Pipe.Route msg) {

    }
}
