package com.jpcchaves.orchestrator.core.saga;

import com.jpcchaves.orchestrator.core.enums.EEventSource;
import com.jpcchaves.orchestrator.core.enums.ESagaStatus;
import com.jpcchaves.orchestrator.core.enums.ETopics;

public class SagaHandler {

    public static final Object[][] SAGA_HANDLER = {

        // start saga
        {EEventSource.ORCHESTRATOR, ESagaStatus.SUCCESS, ETopics.EMAIL_SUCCESS},

        // case start saga success, finishes saga with success
        {EEventSource.EMAIL_SERVICE, ESagaStatus.SUCCESS, ETopics.REGISTRATION_COMPLETED},

        // case start saga fail
        {EEventSource.EMAIL_SERVICE, ESagaStatus.ROLLBACK_PENDING, ETopics.EMAIL_FAIL},

        // after saga fail and the rollback was executed
        // finishes the saga with fail
        {EEventSource.EMAIL_SERVICE, ESagaStatus.FAIL, ETopics.REGISTRATION_COMPLETED}
    };

    public static final int EVENT_SOURCE_INDEX = 0;
    public static final int SAGA_STATUS_INDEX = 1;
    public static final int TOPIC_INDEX = 2;

    private SagaHandler() {}
}
