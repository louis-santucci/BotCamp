package com.botcamp.botcamp.service.reporting.impl;

import com.botcamp.botcamp.service.mailing.Email;
import com.botcamp.botcamp.service.reporting.BandcampRecord;
import com.botcamp.botcamp.service.reporting.EmailPostprocessor;

public class BandcampPostprocessor implements EmailPostprocessor<BandcampRecord> {
    @Override
    public BandcampRecord processEmail(Email email) {
        return null;
    }

    @Override
    public Class<BandcampRecord> getClazz() {
        return BandcampRecord.class;
    }
}
