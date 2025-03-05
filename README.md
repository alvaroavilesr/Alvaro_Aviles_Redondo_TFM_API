## [Máster en Ingeniería Web por la Universidad Politécnica de Madrid (miw-upm)](http://miw.etsisi.upm.es)
## Trabajo de fin de master 📖
> Este proyecto es uno de los dos proyectos que se han desarrollado para el trabajo de fin de master, el cual consiste en la migración
> de una API rest con arquitectura 3 capas a una arquitectura hexagonal, así como la aplicación del framework de Angular.

### Estado del código ⚙️
[![DevOps](https://github.com/alvaroavilesr/Alvaro_Aviles_Redondo_TFM_API/actions/workflows/build.yml/badge.svg)](https://github.com/alvaroavilesr/Alvaro_Aviles_Redondo_TFM_API/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=alvaroavilesr_Alvaro_Aviles_Redondo_TFM_API&metric=alert_status)](https://sonarcloud.io/dashboard?id=alvaroavilesr_Alvaro_Aviles_Redondo_TFM_API)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=alvaroavilesr_Alvaro_Aviles_Redondo_TFM_API&metric=bugs)](https://sonarcloud.io/summary/new_code?id=alvaroavilesr_Alvaro_Aviles_Redondo_TFM_API)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=alvaroavilesr_Alvaro_Aviles_Redondo_TFM_API&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=alvaroavilesr_Alvaro_Aviles_Redondo_TFM_API)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=alvaroavilesr_Alvaro_Aviles_Redondo_TFM_API&metric=coverage)](https://sonarcloud.io/summary/new_code?id=alvaroavilesr_Alvaro_Aviles_Redondo_TFM_API)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=alvaroavilesr_Alvaro_Aviles_Redondo_TFM_API&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=alvaroavilesr_Alvaro_Aviles_Redondo_TFM_API)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=alvaroavilesr_Alvaro_Aviles_Redondo_TFM_API&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=alvaroavilesr_Alvaro_Aviles_Redondo_TFM_API)

## Arranque de la aplicación 🚀

Sigue estos pasos para arrancar la aplicación en local:
1. Asegurate de tener instalado:
    - Docker
    - Docker-compose
    - Git
2. Clona este repositorio en tu local.
3. Ejecuta el comando "docker-compose build --no-cache" y después ejecuta "docker-compose up -d".
4. El contenedor de la api va a fallar hasta que, a través del panel PhpMyAdmin (http://localhost:8081/), una nueva base de datos app_db sea creada manualmente.
5. Cuando todos los contenedores estén levantados, ejecuta el DUMP inicial de BBDD a través del panel de PhpMyAdmin (http://localhost:8081/).

## Acceso a la aplicación 🌐

Una vez realizados los pasos de arranque de ambos repositorios, la aplicación será accesible. Habrá varios puntos de acceso, pero solo el front-end web será accesible para el usuario final:

- Front-end web: http://localhost:4200/

- SwaggerUI: http://localhost:8082/swagger-ui/index.html

- API Healthcheck: http://localhost:8082/actuator/health

- Panel de control PhpMyAdmin: http://localhost:8081/


## Credenciales de acceso 🔑

### PhPMyAdmin 

- User: root
- Pass: root

### Aplicación Web 

Como se ha realizado un volcado inicial de base de datos, las contraseñas estaban encriptadas. Aqui está la lista de usuarios - contraseñas:

- User1 - user@pass
- User2 - user@pass
- Vendor1 - vendor@pass
- Vendor2 - vendor@pass
- Admin1 - admin@pass
- Admin2 - admin@pass
