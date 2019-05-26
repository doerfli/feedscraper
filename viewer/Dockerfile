#= Build ============================================================
FROM openjdk:12.0.1-jdk-oraclelinux7 as build
WORKDIR /workspace

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY gradle.properties .
COPY settings.gradle.kts .

COPY src src

RUN ./gradlew -Dorg.gradle.daemon=false assemble
RUN mkdir -p build/libs/dependency && (cd build/libs/dependency; jar -xf ../*.jar)

#= Run ==============================================================
FROM openjdk:12.0.1-jdk-oraclelinux7

VOLUME /tmp
VOLUME /log

COPY --from=build /workspace/build/libs/dependency/BOOT-INF/lib /app/lib
COPY --from=build /workspace/build/libs/dependency/META-INF /app/META-INF
COPY --from=build /workspace/build/libs/dependency/BOOT-INF/classes /app

ENTRYPOINT java -cp app:app/lib/* li.doerf.feeder.viewer.ViewerApplicationKt
