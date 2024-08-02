#!/usr/bin/env bash
# 更新快照版本
version=$1
if [ -z "$version" ]; then
    version=1.0.1-SNAPSHOT
fi
cd ../
mvn clean deploy -P release,oss -f structure-basic-api/pom.xml -Dmaven.test.skip=true -Drevision=$version
