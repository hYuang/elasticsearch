/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.core.ml.action;

import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.test.AbstractStreamableXContentTestCase;
import org.elasticsearch.xpack.core.ml.action.ForecastJobAction.Request;

import static org.hamcrest.Matchers.equalTo;

public class ForecastJobActionRequestTests extends AbstractStreamableXContentTestCase<Request> {

    @Override
    protected Request doParseInstance(XContentParser parser) {
        return Request.parseRequest(null, parser);
    }

    @Override
    protected boolean supportsUnknownFields() {
        return false;
    }

    @Override
    protected Request createTestInstance() {
        Request request = new Request(randomAlphaOfLengthBetween(1, 20));
        if (randomBoolean()) {
            request.setDuration(TimeValue.timeValueSeconds(randomIntBetween(1, 1_000_000)).getStringRep());
        }
        if (randomBoolean()) {
            request.setExpiresIn(TimeValue.timeValueSeconds(randomIntBetween(0, 1_000_000)).getStringRep());
        }
        return request;
    }

    @Override
    protected Request createBlankInstance() {
        return new Request();
    }

    public void testSetDuration_GivenZero() {
        IllegalArgumentException e = expectThrows(IllegalArgumentException.class, () -> new Request().setDuration("0"));
        assertThat(e.getMessage(), equalTo("[duration] must be positive: [0ms]"));
    }

    public void testSetDuration_GivenNegative() {
        IllegalArgumentException e = expectThrows(IllegalArgumentException.class, () -> new Request().setDuration("-1s"));
        assertThat(e.getMessage(), equalTo("[duration] must be positive: [-1]"));
    }

    public void testSetExpiresIn_GivenZero() {
        Request request = new Request();
        request.setExpiresIn("0");
        assertThat(request.getExpiresIn(), equalTo(TimeValue.ZERO));
    }

    public void testSetExpiresIn_GivenNegative() {
        IllegalArgumentException e = expectThrows(IllegalArgumentException.class, () -> new Request().setExpiresIn("-1s"));
        assertThat(e.getMessage(), equalTo("[expires_in] must be non-negative: [-1]"));
    }
}
