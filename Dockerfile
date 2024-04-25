from drsaaron/blazarjavabase:1.38

ENV ENVIRONMENT=prod

ADD ./pom.xml ./pom.xml
ADD ./mvnw ./mvnw
ADD ./.mvn ./.mvn
ADD ./src ./src

ADD ./runBlazarService.sh ./runBlazarService.sh

RUN mvnw clean install

ENV SERVER_PORT=8081
HEALTHCHECK CMD curl --silent --fail http://localhost:$SERVER_PORT/monitoring/health || exit 1

CMD ./runBlazarService.sh
