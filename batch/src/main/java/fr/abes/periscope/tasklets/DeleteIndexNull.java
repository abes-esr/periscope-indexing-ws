package fr.abes.periscope.tasklets;

import fr.abes.periscope.repository.baseXml.PeriscopeIndexRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class DeleteIndexNull implements Tasklet {
    private final PeriscopeIndexRepository dao;
    public DeleteIndexNull(PeriscopeIndexRepository dao) {
        this.dao = dao;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        dao.deleteByDateIndexIsNull();
        return RepeatStatus.FINISHED;
    }
}
