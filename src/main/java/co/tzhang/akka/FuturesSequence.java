package co.tzhang.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.pattern.Patterns;
import akka.util.Timeout;
import co.tzhang.akka.actors.MasterActor;
import scala.concurrent.duration.FiniteDuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by haofan on 5/15/2016.
 */
public class FuturesSequence {
    private final Map<String, Future<Object>> taskList = new HashMap<>();
    public void addStartTask(ActorSystem system) {
        //final ExecutionContextExecutorService ec = ExecutionContexts.fromExecutorService(Executors.newSingleThreadExecutor());
        Timeout timeout = new Timeout(FiniteDuration.create(2, TimeUnit.SECONDS));
        ActorRef master = system.actorOf(Props.create(MasterActor.class), "master");
        Future<Object> futureRes = Patterns.ask(master,"The quick brown fox tried to jump over the lazy dog and fell on the dog",timeout);
        taskList.put("The quick brown fox tried to jump over the lazy dog and fell on the dog", futureRes);
        Future<Object> futureRes = Patterns.ask(master,"quick brown fox tried to jump over the lazy dog and fell on the dog",timeout);
        taskList.put("quick brown fox tried to jump over the lazy dog and fell on the dog", futureRes);
        Future<Object> futureRes = Patterns.ask(master,"The brown fox tried to jump over the lazy dog and fell on the dog",timeout);
        taskList.put("The brown fox tried to jump over the lazy dog and fell on the dog", futureRes);

    }
    public void completeTasks(ActorSystem system){
        if(!taskList.isEmpty()){
            final Future<Iterable<Object>> futureResponse = Futures.sequence(taskList.values(),system.dispatcher());
        }

    }
    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("FuturesSequence");
        FuturesSequence futuresSequence = new FuturesSequence();
        futuresSequence.addStartTask(system);
        futuresSequence.completeTasks(system);
    }
}
