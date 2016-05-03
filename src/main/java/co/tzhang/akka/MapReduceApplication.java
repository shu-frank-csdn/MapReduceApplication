package co.tzhang.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import co.tzhang.akka.actors.MasterActor;
import co.tzhang.akka.message.Result;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;



public class MapReduceApplication {
    public static void main(String[] args) throws Exception {
        Timeout timeout = new Timeout(FiniteDuration.create(5, TimeUnit.SECONDS));
        ActorSystem system = ActorSystem.create("MapReduceWordCount");
        ActorRef master = system.actorOf(Props.create(MasterActor.class), "master");
        System.out.println(master.path());
        master.tell("The quick brown fox tried to jump over the lazy dog and fell on the dog", master);
        master.tell("Dog is man's best friend", master);
        master.tell("Dog and Fox belong to the same family", master);

        Thread.sleep(1000);
        //Future是一种ask的方式，等待接收消息。超时时间这里设置的是5s
        Future<Object> future = Patterns.ask(master, new Result(), timeout);
        String result = (String) Await.result(future, timeout.duration());
        //String result = master.tell(new Result(),master);//这是发送之后不等待的方式
        System.out.println(result);
        system.shutdown();
    }
}
