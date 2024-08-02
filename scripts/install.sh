#!/usr/bin/env bash
#在本地仓库安装.RELEASE
version=$1
if [ -z "$version" ]; then
    version=1.0.1
fi
cd ../
mvn clean install -f structure-basic-dependencies/pom.xml -Dmaven.test.skip=true -Drevision=$version
