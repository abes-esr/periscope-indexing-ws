package fr.abes.periscope.processor;

import fr.abes.periscope.dto.PeriscopeIndexDto;
import fr.abes.periscope.entity.xml.NoticesBibio;
import fr.abes.periscope.service.NoticesBibioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class PeriscopeIndexReader implements ItemReader<NoticesBibio>, StepExecutionListener {
    private List<PeriscopeIndexDto> periscopeIndexDtoList;
    private AtomicInteger i = new AtomicInteger();
    @Autowired
    private NoticesBibioService service;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();
        this.periscopeIndexDtoList = (List<PeriscopeIndexDto>) executionContext.get("listIndex");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public NoticesBibio read() {
        PeriscopeIndexDto idx;
        if (i.intValue() < this.periscopeIndexDtoList.size()) {
            idx = this.periscopeIndexDtoList.get(i.getAndIncrement());
            log.debug("Reader PPN : " + idx.getPpn());
            return service.findByPpn(idx.getPpn());
        }
        return null;
    }
}
