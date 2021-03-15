package fr.abes.periscope.partitioner;

import fr.abes.periscope.service.NoticesBibioService;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;


public class RangePartitioner implements Partitioner {

    private NoticesBibioService service;

    public RangePartitioner(NoticesBibioService service) {
        this.service = service;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Integer min = service.getMinId();
        Integer max = service.getMaxId();

        int targetSize = (max - min) / gridSize + 1;

        Map<String, ExecutionContext> result = new HashMap<>();

        int number = 0;
        int start = min;
        int end = start + targetSize - 1;

        while (start <= max)
        {
            ExecutionContext value = new ExecutionContext();
            value.putString("name", "Thread" + number);

            if(end >= max) {
                end = max;
            }

            value.putInt("minValue", start);
            value.putInt("maxValue", end);
            result.put("partition" + number, value);
            start += targetSize;
            end += targetSize;

            number++;
        }
        return result;
    }
}
