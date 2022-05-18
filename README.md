# RAF SI - BOLNICA - BACKEND

## How to run using docker compose

- Download Docker
- Go in the directory where the docker-compose.yaml file is located
- From terminal run command `docker-compose up`
- To shut down the services press Ctrl + C or open another tab in terminal and run command `docker-compose down`

### Napomena

Kako bi Docker pokupio izmene načinjene nad projektom, prilikom izmene bilo kog file-a u određenom servisu - neophodno je da njega ponovo build-ujemo kroz komandu `docker-compose build $service` gde je $service onaj servis nad kojim imamo izmene. 

Primer komande: `docker-compose build management-service`. 

Takođe, moguće je odraditi komandu `docker-compose up --build` koja će sve servise ponovo build-ovati iz source-a.
Ukoliko se ne koristi `--build` uz komandu `docker-compose up` on ce povuci image-e sa GHCR koji ima image-e sa main grane.

### Swagger

Moguće je pristupiti Swagger dokumentaciji preko URL-ova:

**User servis:** http://localhost:8081/swagger-ui.html

**Management servis:** http://localhost:8082/swagger-ui.html

**Laboratory servis:** http://localhost:8083/swagger-ui.html
