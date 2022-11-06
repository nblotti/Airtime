package ch.nblotti.airtime;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.OptionalDouble;

import ch.nblotti.airtime.sample.Sample;
import ch.nblotti.airtime.sample.SampleDao;
import ch.nblotti.airtime.session.SessionDao;


public class ApplicationController {


    private final SessionDao sessionDao;

    private final SampleDao sampleDao;



    public ApplicationController(SessionDao sessionDao, SampleDao sampleDao) {
        this.sessionDao = sessionDao;
        this.sampleDao = sampleDao;
    }

    public void start() {
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(MessageEvent event) {
        // Do something
        switch (event.getEventType()) {
            case VIDEO_CHANGE:

                List<Sample> samples = sampleDao.loadAllByIdsSync(event.getuID());

                OptionalDouble sampleAverage = samples.stream()
                        .mapToDouble(a -> a.sampleMeasure)
                        .average();
                sessionDao.updateAverageByIds(event.getuID(), sampleAverage.getAsDouble());
                break;
        }
    }


    public void destroy() {

        EventBus.getDefault().unregister(this);
    }
}