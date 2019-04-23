# Tanknite

[![Build Status](https://travis-ci.com/Chasbob/Tanknite.svg?branch=master)](https://travis-ci.com/Chasbob/Tanknite)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/Chasbob/Tanknite.svg)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/Chasbob/Tanknite.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Chasbob/Tanknite/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/Chasbob/Tanknite.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Chasbob/Tanknite/alerts/)

## Overview

Tanknite is a game I made with 5 other people as the end product of my second year team project outlined [here](https://chasbob.github.io/Tanknite/Presentation).

## Game

Tanknite is very loosely based on, Tank Trouble but diverged during development.
The game follows a battle royal style with an initial free for all game mode implemented. Single and multiplayer modes are available allowing you to battle it out with both AI and other players.\
A more immersive experience has been developed through offerings of:

* Persistent users with statistic tracking
* Player hosted multiplayer
* LAN server discovery for multiplayer
* Localised in-game audio
* Competitive AI
* Both 2D and isometric rendering (experimental cross play between the two)

## How to play

### From Release

1. download your preferred archive format and extract
2. launch the game through the system appropriate executable in the bin directory

### From source

1. clone/download the repository
2. ensure ```gradlew``` is executable
    * e.g ```chmod +x ./gradlew```
3. run
    * build release:
        * ```./gradlew build```
        * navigate to ```client/build/distributions``` and follow steps for 'from release'
    * ```play``` task:
        * ```./gradlew play```