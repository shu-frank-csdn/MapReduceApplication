本程序参考视频教程：http://edu.51cto.com/index.php?do=lesson&id=77672
# MapReduceApplication 程序解释
本程序实现了一个简单的Akka demo程序，各文件解释如下：  
MapReduceApplication：是程序启动入口，创建了ActorSystem，并且向MaterActor发送消息，并最终返回结果。  
MasterActor： 是Actor控制器，实例化了多个Actor  
MapActor：MapActor 统计单个句子中的单词出现，并将结果放入到Map，每个key对应的value是1  
ReduceActor：对传过来的MapData进行统计，统计每个字符串的出现次数（注意这里统计出来的仅仅是一个句子中的单词出现次数）  
AggregateActor：统计所有句子中单词的出现次数  

