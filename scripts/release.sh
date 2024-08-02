#!/usr/bin/env bash
#发行新的版本上传到中心仓库可以执行这个脚本
#!/bin/bash
version=$1
if [ -z "$version" ]; then
    version=1.0.1
fi
cd ../
mvn clean deploy -P release,oss -f structure-basic-api/pom.xml -Dmaven.test.skip=true -Drevision=$version

