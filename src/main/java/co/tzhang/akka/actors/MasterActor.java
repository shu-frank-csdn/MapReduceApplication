package co.tzhang.akka.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;
import co.tzhang.akka.message.MapData;
import co.tzhang.akka.message.ReduceData;
import co.tzhang.akka.message.Result;

/**
 * 是控制器，给mapAcotr发送具体字符串，要求mapAcotr进行单词切分等工作
 * 实例化几个Actor，要把mapAcotr处理内容，发给MapAcotr
 */
public class MasterActor extends UntypedActor {
    //也可以在构造器中传入给其他Actor传入某个Acotr,这样方便在另一个Actor中调用另一个Actor
    //这三个Actor就是子Actor
    ActorRef mapActor = getContext().actorOf(Props.create(MapActor.class)
            .withRouter(new RoundRobinPool(5)), "map");
    ActorRef reduceActor = getContext().actorOf(Props.create(ReduceActor.class)
            .withRouter(new RoundRobinPool(5)), "reduce");
    ActorRef aggregateActor = getContext().actorOf(Props.create(AggregateActor.class),
            "aggregate");
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            System.out.println(mapActor.path());
            mapActor.tell(message, getSelf());
        } else if (message instanceof MapData) {
            reduceActor.tell(message, getSelf());
        } else if (message instanceof ReduceData) {
            aggregateActor.tell(message, getSelf());
        } else if (message instanceof Result) {
            aggregateActor.forward(message, getContext());//消息转发
            //aggregateActor.tell(message, getSelf());
        } else {
            unhandled(message);
        }
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("MasterActor has benn stoped");
    }
}
