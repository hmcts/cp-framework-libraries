package uk.gov.moj.cpp.task.execution;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.LOW;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.MEDIUM;

import uk.gov.moj.cpp.jobstore.persistence.JobStoreConfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultJobStoreSchedulerPrioritySelectorTest {

    @Mock
    private RandomPercentageProvider randomPercentageProvider;

    @Mock
    private JobStoreConfiguration jobStoreConfiguration;

    @InjectMocks
    private DefaultJobStoreSchedulerPrioritySelector defaultJobStoreSchedulerPrioritySelector;

    @Test
    public void shouldSelectHighPriorityIfRandomPercentageAboveOrEqualToHighPriorityThreshold() throws Exception {

        when(jobStoreConfiguration.getJobPriorityPercentageHigh()).thenReturn(70);
        when(jobStoreConfiguration.getJobPriorityPercentageLow()).thenReturn(10);
        when(randomPercentageProvider.getRandomPercentage()).thenReturn(29, 30, 31);

        assertThat(defaultJobStoreSchedulerPrioritySelector.selectNextPriority(), is(MEDIUM));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectNextPriority(), is(HIGH));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectNextPriority(), is(HIGH));
    }

    @Test
    public void shouldSelectLowPriorityIfRandomPercentageBelowHighPriorityThreshold() throws Exception {

        when(jobStoreConfiguration.getJobPriorityPercentageHigh()).thenReturn(70);
        when(jobStoreConfiguration.getJobPriorityPercentageLow()).thenReturn(10);
        when(randomPercentageProvider.getRandomPercentage()).thenReturn(9, 10, 11);

        assertThat(defaultJobStoreSchedulerPrioritySelector.selectNextPriority(), is(LOW));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectNextPriority(), is(MEDIUM));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectNextPriority(), is(MEDIUM));
    }

    @Test
    public void shouldReturnMediumPriorityIfRandomPercentageBetweenTheHighAndLowThresholds() throws Exception {
        when(jobStoreConfiguration.getJobPriorityPercentageHigh()).thenReturn(70);
        when(jobStoreConfiguration.getJobPriorityPercentageLow()).thenReturn(10);
        when(randomPercentageProvider.getRandomPercentage()).thenReturn(9, 10, 11, 29, 30, 31);

        assertThat(defaultJobStoreSchedulerPrioritySelector.selectNextPriority(), is(LOW));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectNextPriority(), is(MEDIUM));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectNextPriority(), is(MEDIUM));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectNextPriority(), is(MEDIUM));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectNextPriority(), is(HIGH));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectNextPriority(), is(HIGH));

    }
}