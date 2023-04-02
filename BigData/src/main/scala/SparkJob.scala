import org.apache.spark.{SparkConf, SparkContext, SparkEnv}

object SparkJob {
  def main(args: Array[String]): Unit = {

    val context = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("test"))
    val context1 = SparkContext.getOrCreate()
    val RDD = context.makeRDD(Seq(1->2, 2 -> 3, 1 ->2))
    val value = RDD.groupByKey()
    println(value.count())


//    val add = (x :  Int) => x + 1
//    val buffer = SparkEnv.get.closureSerializer.newInstance().serialize(add)
//    val buffer = SparkEnv.get.
//    buffer.
    //val value = RDD.groupByKey()
    //value.count()
  }
}
