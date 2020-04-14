# Warning

This repository is currently not maintained. Dependencies can be outdated and may contain vulnerabilities. 

# Feedscraper 
[![codebeat badge](https://codebeat.co/badges/3cf4f2b4-7439-4b83-b947-3923f27e7ebc)](https://codebeat.co/projects/github-com-doerfli-feedscraper-master)
[![codecov](https://codecov.io/gh/doerfli/feedscraper/branch/master/graph/badge.svg)](https://codecov.io/gh/doerfli/feedscraper)

## Scraper

Component to scrape feeds

## Viewer

Webapplication to display feeds

## Containers

- db: PostgreSQL database
- mqtt: mosquitto mqtt broker
- scraper: downloads feeds and store in db
- viewer: shows feeds and items

### 

Start containers for development

```
docker-compose docker-compose up -d db mqtt
```

Reset DB container

```
docker-compose down && rm -r pgdata/ && docker-compose up -d db mqtt
```
