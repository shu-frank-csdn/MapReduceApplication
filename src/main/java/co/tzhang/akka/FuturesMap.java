package co.tzhang.akka;

import akka.actor.ActorSystem;
import akka.dispatch.Mapper;
import akka.dispatch.OnSuccess;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import java.util.concurrent.Callable;

import static akka.dispatch.Futures.future;
/**
 * Created by haofan on 5/14/2016.
 * 该例子是函数式Future的例子中map的使用方法
 */
public class FuturesMap {
    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("FuturesMap");
        final ExecutionContext ec = system.dispatcher();
        Future<String> f1 = future(new Callable<String>() {
            public String call() {
                return "Hello" + "World";
            }
        }, ec);
        Future<Integer> f2 = f1.map(new Mapper<String,Integer>(){
            public Integer apply(String s){
                return s.length();
            }
        },ec);
        f2.onSuccess(new PrintResult<Integer>(),system.dispatcher());
    }
    public final static class PrintResult<T> extends OnSuccess<T> {
        @Override public final void onSuccess(T t) {
            System.out.println(t);
        }
    }
}
