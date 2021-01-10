
# EindProject Advanced Programming Topics
## 1. Algemene opgave

Voor het Eindproject Advanced Programming Topics werk je in groep een microservices opdracht uit en dit met de nodige testen en documentatie.

Het thema mag je zelf kiezen binnen de groep. Indien je geen thema kan bedenken kan je er een vragen aan de docent.

De volledige evaluatie gebeurt op GitHub. Dit wil zeggen dat iets dat niet op GitHub staat, ook niet geëvalueerd zal worden. Er wordt gekeken naar commits en er zal een Buddycheck peerevaluation plaatsvinden. %-punten die je krijgt per volledige sectie liggen vast. %-punten die vervolgens afgetrokken worden voor niet-complete delen van de sectie liggen niet vast.


## 2. Algemene Eisen &amp; Documentatie (+65%)

| | |
| --- | --- |
| ☒ | Minimum aantal &#39;Back-end&#39; microservices: 4 voor team met 4 personen / 3 voor teams met minder dan 4 personen|
| ☒ | 1 Edge microservice |
| ☒ | Dockerfile voor elke microservice |
| ☒ | Korte beschrijving van het gekozen thema + Diagram van de volledige microservices architectuur (met links naar &#39;back-end&#39; repo&#39;s) op GitHub README van de Edge microservice |
| ☒ | Aantoonbare werking totale architectuur door Postman requests op de Edge microservice (buiten containers) |
| ☒ | Volledige implementatie SwaggerUI voor de Edge microservice en screenshot(s) van de output op GitHub README

### REST API
| | |
| --- | --- |
| ☒ | Minstens 4 GET endpoints op de Edge microservice, nooit zoekende op DB id |
| ☒ | POST, PUT en DELETE endpoints op de Edge microservice |
| ☒ | Minstens 2 GET endpoints per &#39;Back-end&#39; microservice, nooit zoekende op DB id |
| ☒ | POST, PUT en DELETE endpoints voor minstens 1 &#39;Back-end&#39; microservice |
| ☒ | Gebruik van PostgreSQL en MongoDB |
| ☒ | Efficiënt gebruik @PathVariable vs. @RequestParam |

### TESTING

| | |
| --- | --- |
| ☒ | Unit tests voor alle microservice controllers |
| ☒ | Integration tests alle microservice controllers |
| ☒ | 100% method test code coverage voor controllers, repositories en constructors van model classes |

### CI/CD

| | |
| --- | --- |
| ☒  | Elke GitHub repo heeft een CI/CD pipeline die tests runt, de .jar upload als artifact en een Docker container naar Docker Hub pushed |

## 3. Bijkomende componenten

| | | 
| --- | --- |
| ☒  | Deployment op K8s (+15%) ||
| ☒  | └→ Gebruik van K8s secrets voor environment variables bij deployment (+5%) |
| ☐ | └→ Keycloak integratie (+15%) |
| ☐ | └→ Queue met bv. ActiveMQ (+10%) |
| ☐ | └→ Prometheus auto-scaling via een extra endpoint (+15%) |
| ☒ | Basic front-end dat communiceert met de edge-service (+15%) |
| ☐ | └→ Selenium testing van het front-end (+10%) |
