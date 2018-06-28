package uk.gov.moj.cpp.jobstore.persistence;

import javax.enterprise.inject.Alternative;

@Alternative
public class AnsiJobSqlProvider implements JobSqlProvider {

    private static final String INSERT_JOB_SQL = "INSERT INTO job(job_id,worker_id,worker_lock_time,next_task,next_task_start_time,job_data) values (?,?,?,?,?,?)";
    private static final String UPDATE_JOB_DATA_SQL = "UPDATE job SET job_data = ? WHERE job_id = ?";

    @Override
    public String getInsertSql() {
        return INSERT_JOB_SQL;
    }

    @Override
    public String getUpdateJobDataSql() {
        return UPDATE_JOB_DATA_SQL;
    }
}
