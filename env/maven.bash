# 先清理项目，再清理本地缓存依赖并重新解析，最后安装，同时强制更新快照依赖
mvn clean dependency:purge-local-repository install -U -s ./env/settings.xml -Dmaven.test.skip=true