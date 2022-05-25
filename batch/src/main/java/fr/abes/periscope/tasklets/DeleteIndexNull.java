package fr.abes.periscope.tasklets;

import fr.abes.periscope.repository.baseXml.PeriscopeIndexRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Step permettant la suppression des notices non indexées car ne correspondant pas aux critères d'indexation
 * (notice autres que ressources continues)
 */
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
