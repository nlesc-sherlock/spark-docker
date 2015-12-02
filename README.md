# spark_docker


Spark:
------

* Spark Standalone Mode. We advise you to use a Spark version 1.5.2 which get be obtained [here](https://spark.apache.org/downloads.html).

* Spark Standalone Mode. Instructions in how to [install Spark Standalone](http://spark.apache.org/docs/latest/spark-standalone.html).
```
Small example:
cd spark-1.5.2-bin-hadoop2.6
./sbin/start-master.sh
./sbin/start-slave.sh -c 1 -m 1G -d <out_dir_path>

```

* To create a simple Spark's app you should read [quick start](http://spark.apache.org/docs/latest/quick-start.html)

* Under [caffe](https://github.com/nlesc-sherlock/spark-docker/tree/master/caffe) there is an example of a Spark's app to run the [docker file from deep learning repo](https://github.com/nlesc-sherlock/deeplearning/blob/master/dockerfile).


SBT:
----

* To create Spark's apps you need to have SBT. The instructions to install it on Ubuntu are the following:
```
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
sudo apt-get update
sudo apt-get install sbt
```
