# Scraper

## Containers

### Start 

```
docker-compose docker-compose up -d db
```

### Stop, Delete and Restart 

```
docker-compose down && rm -r pgdata/ && docker-compose up -d db
```
