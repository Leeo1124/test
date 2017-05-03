package otter;

import com.alibaba.otter.shared.etl.extend.processor.EventProcessor;
import com.alibaba.otter.shared.etl.model.EventData;
import com.alibaba.otter.shared.etl.model.EventType;

public class DeleteEventProcessor implements EventProcessor {

    @Override
    public boolean process(EventData eventData) {
        if (EventType.DELETE == eventData.getEventType()) {
            return false;
        }
        return true;
    }

}