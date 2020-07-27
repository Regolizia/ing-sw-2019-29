# ing-sw-2019-29
This project is part of the course Software Engineering (2018-2019) of the Politecnico di Milano. 

Complete Rules version - fully implemented CLI - partially implemented GUI

## Description of the game
In the future, war has left the world in complete destruction and split the people into factions. The factions have decided to stop the endless war and settle their dispute in the arena. A new virtual bloodsport was created. The Adrenaline tournament. Every faction has a champion, every champion has a chance to fight and the chance to win. Will you take the chance of becoming the next champion of the Adrenaline tournament?

Play a first-person shooter on your gaming table. Grab some ammo, grab a gun, and start shooting. Build up an arsenal for a killer turn. Combat resolution is quick and diceless. And if you get shot, you get faster!

> A Game for 3 to 5 players that can be played in about 30-60 minutes

# GUI implementation
<img src="https://github.com/Regolizia/ing-sw-2019-29/blob/master/IMG_1.png" height="350"></img>

<img src="https://github.com/Regolizia/ing-sw-2019-29/blob/master/IMG_2.jpg" height="350"></img>

## How to play
The rules of the game can be found [here](https://czechgames.com/en/adrenaline/).

### To start the game:
You MUST start the server before the client.

#### MainClass: StartServer
Start the server with an int as parameter.
It sets the seconds before the game starts. For example 40

#### MainClass: ClientCLI
Start the client with the server IP as parameter. For example 127.0.0.1 if local
