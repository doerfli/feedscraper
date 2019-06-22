# Feedscraper 
[![Build Status](https://travis-ci.com/doerfli/feedscraper.svg?branch=master)](https://travis-ci.com/doerfli/feedscraper) 
[![CodeFactor](https://www.codefactor.io/repository/github/doerfli/feedscraper/badge)](https://www.codefactor.io/repository/github/doerfli/feedscraper) 
[![codecov](https://codecov.io/gh/doerfli/feedscraper/branch/master/graph/badge.svg)](https://codecov.io/gh/doerfli/feedscraper)

## Scraper

Component to scrape feeds

## Viewer

Webapplication to display feeds

## Containers

- scraper: downloads feeds and store in db
- viewer: shows feeds and items

### DB

Start DB container

```
docker-compose docker-compose up -d db
```

Reset DB container

```
docker-compose down && rm -r pgdata/ && docker-compose up -d db
```
