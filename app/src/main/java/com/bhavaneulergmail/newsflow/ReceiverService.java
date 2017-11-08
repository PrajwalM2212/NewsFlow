package com.bhavaneulergmail.newsflow;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by prajwalm on 05/11/17.
 */

public class ReceiverService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ReceiverFactory(this.getApplicationContext(), intent);
    }
}
