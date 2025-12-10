package persistencia.jpa.jobs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import persistencia.jpa.jobs.job.JobAgendado;

@Repository
public interface JobsRepository extends JpaRepository<JobAgendado, String> {
}
