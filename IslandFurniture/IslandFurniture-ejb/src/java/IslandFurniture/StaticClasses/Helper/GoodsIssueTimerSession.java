/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import java.util.Collection;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

/**
 *
 * @author KamilulAshraf
 */
@Stateless
@LocalBean
public class GoodsIssueTimerSession implements GoodsIssueTimerSessionLocal {

    @Resource
    private SessionContext ctx;

    @Override
    public void createTimers() {
        TimerService timerService = ctx.getTimerService();
        Timer timer10000ms = timerService.createTimer(10000, 10000, new String("GoodsIssueTimerSession"));
    }

    @Override
    public void cancelTimers() {
        TimerService timerService = ctx.getTimerService();
        Collection timers = timerService.getTimers();
        for (Object obj : timers) {
            Timer timer = (Timer) obj;
            if (timer.getInfo().toString().equals("EJBTIMER")) {
                timer.cancel();
            }
        }
    }

    @Timeout
    @Override
    public void handleTimeout(Timer timer) {
        if (timer.getInfo().toString().equals("EJBTIMER")) {//Do something}}}
        }
    }
}
