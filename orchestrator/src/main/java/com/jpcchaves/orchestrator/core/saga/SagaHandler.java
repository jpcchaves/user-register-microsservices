package com.jpcchaves.orchestrator.core.saga;

import static com.jpcchaves.orchestrator.core.enums.ETopics.*;

import com.jpcchaves.orchestrator.core.enums.EEventSource;
import com.jpcchaves.orchestrator.core.enums.ESagaStatus;
import com.jpcchaves.orchestrator.core.enums.ETopics;

public class SagaHandler {

    public static final Object[][] SAGA_HANDLER = {
        // When user registers in auth-service,
        // the source is set to ORCHESTRATOR and the status is set to SUCCESS
        // it will make the next topic be the email-success by the SEC
        {EEventSource.ORCHESTRATOR, ESagaStatus.SUCCESS, ETopics.EMAIL_SUCCESS},

        // in the email-service, the source will be set to EMAIL_SERVICE
        /// and if there is an error, the status will be FAIL
        // if there's no error, the status will be SUCCESS and it will return to
        // registration-completed topic
        {EEventSource.EMAIL_SERVICE, ESagaStatus.FAIL, ETopics.REGISTRATION_COMPLETED},
        {EEventSource.EMAIL_SERVICE, ESagaStatus.SUCCESS, ETopics.REGISTRATION_COMPLETED},
    };

    public static final int EVENT_SOURCE_INDEX = 0;
    public static final int SAGA_STATUS_INDEX = 1;
    public static final int TOPIC_INDEX = 2;

    private SagaHandler() {}
}
