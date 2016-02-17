Running docker containers inside Spark.

Spark applications:

* caffee/, run the [docker file from deep learning repo](https://github.com/nlesc-sherlock/deeplearning/blob/master/dockerfile) using directory of images on HDFS
* caffeeseq/, run the [docker file from deep learning repo](https://github.com/nlesc-sherlock/deeplearning/blob/master/dockerfile) using sequence file on HDFS as input and orc file as output

See README file of each applications for build and run instructions.

# Prerequisites

## Spark

### Standalone

We advise you to use a Spark version 1.6.0 which get be obtained [here](https://spark.apache.org/downloads.html).

Instructions in how to [install Spark Standalone](http://spark.apache.org/docs/latest/spark-standalone.html).
```
Small example:
cd spark-1.6.0-bin-hadoop2.6
./sbin/start-master.sh
./sbin/start-slave.sh -c 1 -m 1G -d <out_dir_path>

```

### Yarn cluster

Use `--master yarn-cluster` in `spark-submit`.

## SBT

* To create Spark's apps you need to have SBT. The instructions to install it on Ubuntu are the following:
```
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
sudo apt-get update
sudo apt-get install sbt
```
