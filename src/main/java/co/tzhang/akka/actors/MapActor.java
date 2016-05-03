package co.tzhang.akka.actors;

import akka.actor.UntypedActor;
import co.tzhang.akka.message.MapData;
import co.tzhang.akka.message.WordCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * MapActor 统计单个句子中的单词出现
 */
public class MapActor extends UntypedActor {

    private static String[] STOP_WORDS = {"a", "am", "an", "and", "are", "as", "at", "be", "do", "go", "if", "in",
            "is", "it", "of", "on", "the", "to"};
    private static List<String> STOP_WORDS_LIST = Arrays.asList(STOP_WORDS);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            //也可以交给其他actor
            getSender().tell(evaluateExpression((String) message), getSelf());
        } else {
            unhandled(message);
        }
    }

    private MapData evaluateExpression(String line) {
        List<WordCount> dataList = new ArrayList<>();
        StringTokenizer parser = new StringTokenizer(line);
        while (parser.hasMoreTokens()) {
            String word = parser.nextToken().toLowerCase();
            if(!STOP_WORDS_LIST.contains(word)) {
                dataList.add(new WordCount(word, 1));
            }
        }
        return new MapData(dataList);
    }
}
