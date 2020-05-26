package uk.gov.moj.cpp.jobmanager.example.task;

import static uk.gov.moj.cpp.jobmanager.example.MakeCakeWorkflow.CAKE_MADE;
import static uk.gov.moj.cpp.jobmanager.example.MakeCakeWorkflow.nextTask;
import static uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo.executionInfo;
import static uk.gov.moj.cpp.jobstore.api.task.ExecutionStatus.COMPLETED;
import static uk.gov.moj.cpp.jobstore.api.task.ExecutionStatus.INPROGRESS;

import uk.gov.justice.services.common.converter.ObjectToJsonObjectConverter;
import uk.gov.moj.cpp.jobmanager.example.MakeCakeWorkflow;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionStatus;

import java.time.ZonedDateTime;

import javax.inject.Inject;

public class JobUtil {

    @Inject
    ObjectToJsonObjectConverter objectConverter;


    public ExecutionInfo nextJob(final ExecutionInfo prevExecutionInfo) {

        final MakeCakeWorkflow nextStep = nextTask(MakeCakeWorkflow.valueOf(prevExecutionInfo.getNextTask()));

        final ExecutionStatus nextExecutionStatus = MakeCakeWorkflow.valueOf(prevExecutionInfo.getNextTask()) == CAKE_MADE ? COMPLETED : INPROGRESS;

        return executionInfo().from(prevExecutionInfo)
                .withJobData(objectConverter.convert(nextStep.getTaskData()))
                .withNextTask(nextStep.toString())
                .withNextTaskStartTime(ZonedDateTime.now())
                .withExecutionStatus(nextExecutionStatus)

                .build();


    }

    public ExecutionInfo sameJob(final ExecutionInfo prevExecutionInfo, final ExecutionStatus executionStatus, final Object jobData, final ZonedDateTime nextTaskStartTime) {

        return executionInfo()
                .withJobData(objectConverter.convert(jobData))
                .withNextTask(prevExecutionInfo.getNextTask())
                .withNextTaskStartTime(nextTaskStartTime)
                .withExecutionStatus(executionStatus)
                .build();

    }
}
