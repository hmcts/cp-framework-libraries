package uk.gov.moj.cpp.task.execution;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import uk.gov.moj.cpp.jobstore.api.ExecutionService;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.service.JobService;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;

@ApplicationScoped
public class DefaultExecutionService implements ExecutionService {

    @Inject
    JobService jobService;

    @Inject
    TaskRegistry taskRegistry;

    @Override
    public void executeWith(final ExecutionInfo executionInfo) {
        final Integer retryAttemptsRemaining = taskRegistry.findRetryAttemptsRemainingFor(executionInfo.getNextTask());
        jobService.insertJob(new Job(randomUUID(), executionInfo.getJobData(),
                executionInfo.getNextTask(), executionInfo.getNextTaskStartTime(), empty(), empty(), retryAttemptsRemaining));
    }
}
