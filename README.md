# spark_docker


Spark:
------

* Spark Standalone Mode. We advise you to use a Spark version 1.5.2 which get be obtained [here](https://spark.apache.org/downloads.html).

* Spark Standalone Mode. Instructions in how to [install Spark Standalone](http://spark.apache.org/docs/latest/spark-standalone.html).

* To create a simple Spark's app you should read [quick start](http://spark.apache.org/docs/latest/quick-start.html)


SBT:
----

* To create Spark's apps you need to have SBT. The instructions to install it on Ubuntu are the following:
```
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
sudo apt-get update
sudo apt-get install sbt
```
