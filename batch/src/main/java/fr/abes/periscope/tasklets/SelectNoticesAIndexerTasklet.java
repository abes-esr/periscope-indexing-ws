package fr.abes.periscope.tasklets;

import fr.abes.periscope.dto.PeriscopeIndexDto;
import fr.abes.periscope.repository.baseXml.PeriscopeIndexRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.List;


public class SelectNoticesAIndexerTasklet implements Tasklet, StepExecutionListener {
    private List<PeriscopeIndexDto> listIndex;

    private final PeriscopeIndexRepository dao;

    public SelectNoticesAIndexerTasklet(PeriscopeIndexRepository dao) {
        this.dao = dao;
        this.listIndex = new ArrayList<>();
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
            stepExecution.getJobExecution().getExecutionContext().put("listIndex", this.listIndex);
        }
        return stepExecution.getExitStatus();
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        dao.findAllByDateIndexIsNull().forEach(i -> listIndex.add(new PeriscopeIndexDto(i.getPpn(), null)));
        if (listIndex.size() == 0)
            stepContribution.setExitStatus(new ExitStatus("NOTITLE"));
        stepContribution.setExitStatus(new ExitStatus("COMPLETED"));
        return RepeatStatus.FINISHED;
    }
}
