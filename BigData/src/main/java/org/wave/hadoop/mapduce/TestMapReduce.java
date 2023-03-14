package org.wave.hadoop.mapduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

public class TestMapReduce {
    public static void main(String[] args) {
        Configuration conf = new Configuration();
        conf.set("mapreduce.job.jvm.numtasks","4");
        /* 创建任务 */
        Job job = null;
        try {
            job = Job.getInstance(conf, "word count");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(job == null);
        /* Job -> n个task -> container -> taskset */
        job.setJarByClass(TestMapReduce.class);
        /* mapper操作 */
        job.setMapperClass(TestMapper.class);
        /* combiner操作，合并一个结点中的数据,这里没有单独写combiner的逻辑 */
        job.setCombinerClass(IntSumReducer.class);
        /* reduce操作，合并不同结点中的数据 */
        job.setReducerClass(IntSumReducer.class);
        /* 设置输入、输出目录，输出目录不能存在 */
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        /* 设置输入、输出目录，输出目录不能存在 */
        /* 设置输入输出的目录,本地运行设置在本地就好 */
        Path inputpath = new Path("D:\\lht\\aa.txt.txt");
        Path outpath = new Path("D:\\lht\\output");
        /* 设置需要计算的文件 */
        try {
            FileInputFormat.addInputPath(job, inputpath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("cala over!");
        /* 删除输出目录,这里是我自己写的一个工具类 */
        //MpUtil.delOutPut(conf, outpath);
        /* 设置输出目录 */
        FileOutputFormat.setOutputPath(job, outpath);

        try {
            job.waitForCompletion(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        /* 0表示正常退出，1表示错误退出 */
    }
}

class TestMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    static {
        System.out.println("this is map");
    }
    private final static IntWritable one = new IntWritable(1);
    private final Text word = new Text();

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        //System.out.println("key:" + key + ",value:" + value);
        /* 根据空格进行切分，将得到的每一个单词写出去，用单词作为key，value赋值为1表示出现了一次 */
        StringTokenizer itr = new StringTokenizer(value.toString());
        while (itr.hasMoreTokens()) {
            word.set(itr.nextToken());
            context.write(word, one);
        }
    }
}

class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    static {
        System.out.println("this is reduce");
    }
    private final IntWritable result = new IntWritable();
    /**
     * 做规约时返回的格式为 <word,{1,1,1}>
     *
     * @param key     单词
     * @param values  返回的结果，为列表结构，存放每一个结点计算的结果
     * @param context 上下文环境
     */
    public void reduce(Text key, Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {
        //System.out.println("reduce任务:  它的键 :" + key + ", 它的值:" + values.toString());
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        result.set(sum);
        context.write(key, result);
    }
}