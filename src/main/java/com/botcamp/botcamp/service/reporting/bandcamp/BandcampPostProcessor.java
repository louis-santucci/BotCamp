package com.botcamp.botcamp.service.reporting.bandcamp;

import com.botcamp.botcamp.service.mailing.Email;
import com.botcamp.botcamp.service.reporting.EmailPostprocessor;

public class BandcampPostProcessor implements EmailPostprocessor<BandcampRecord> {
    @Override
    public BandcampRecord processEmail(Email email) {
        return null;
    }

    @Override
    public Class<BandcampRecord> getClazz() {
        return BandcampRecord.class;
    }
}
