# Scraper [![Build Status](https://travis-ci.com/doerfli/feedscraper.svg?branch=master)](https://travis-ci.com/doerfli/feedscraper) [![CodeFactor](https://www.codefactor.io/repository/github/doerfli/feedscraper/badge)](https://www.codefactor.io/repository/github/doerfli/feedscraper)

## Containers

- scraper: downloads feeds and store in db
- viewer: shows feeds and items

## Start 

```
docker-compose docker-compose up -d db
```

### Stop, Delete and Restart 

```
docker-compose down && rm -r pgdata/ && docker-compose up -d db
```
