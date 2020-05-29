# Spotlight downloader

[![Build Status](https://travis-ci.org/paulchen/SpotlightDownloader.svg?branch=master)](https://travis-ci.org/paulchen/SpotlightDownloader)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=paulchen_SpotlightDownloader&metric=alert_status)](https://sonarcloud.io/dashboard?id=paulchen_SpotlightDownloader)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

Downloads images used by Microsoft Spotlight. Heavily inspired by https://github.com/lerman01/SpotlightDownloader 

Requires JDK 11 (or above) and Apache Maven. Build an run with

`mvn clean install appassembler:assemble`

`./startup.sh`

During every execution, the program tries to download 100 images and place them into the folder `images`.

As every API call to the Spotlight API yields another image, every execution will download a different set of images.
Images that have already been downloaded will not be downloaded again.

The Spotlight API also gives results in various languages, including ones not using the Latin alphabet like Russian, Chinese, Japanese, or Arabic. 
The application tries to change the names of existing images into languages using the Latin alphabet, preferring English and German.

