package com.azamovme.callmanageservice.connection

import android.telecom.Connection

class VoIPConnection : Connection() {
  init {
    setConnectionProperties(PROPERTY_SELF_MANAGED)
    setAudioModeIsVoip(true)
  }
}