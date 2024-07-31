# 使用 OpenJDK 11 的官方镜像作为基础镜像
FROM openjdk:11

ARG WORKDIR=/opt/run

WORKDIR ${WORKDIR}
COPY ./target/cat-1.0-SNAPSHOT.jar digital-cat.jar

# 暴露容器的8080端口
EXPOSE 8379

ENV LANG=zh_CN.UTF-8
ENV LANGUAGE=zh_CN.UTF-8
ENV LC_ALL=zh_CN.UTF-8
ENV TZ=Asia/Shanghai
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT java -jar digital-cat.jar