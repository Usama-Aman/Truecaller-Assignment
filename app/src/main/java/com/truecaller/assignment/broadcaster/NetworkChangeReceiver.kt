package com.truecaller.assignment.broadcaster

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.truecaller.assignment.utilities.Utility

class NetworkChangeReceiver : BroadcastReceiver {
    private var statusChangeListener: ConnectionStatusChangeCallBack? = null

    constructor()
    constructor(listener: ConnectionStatusChangeCallBack) {
        this.statusChangeListener = listener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val status = Utility.getConnectivityStatus(context!!)
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent?.action) {
            if (!status) {
                statusChangeListener?.connectionLost()
            } else {
                statusChangeListener?.connectionFound()
            }
        }
    }

    interface ConnectionStatusChangeCallBack {
        fun connectionLost()
        fun connectionFound()
    }

}