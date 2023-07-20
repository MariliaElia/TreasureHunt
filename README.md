# A city-wide treasure hunt mobile application

This mobile application was developed as part of my undergraduate research project at The University of Sheffield. 

The project aimed to develop a city-wide Treasure Hunt mobile application. Treasure Hunt is a game that by its nature requires the players to move around and perform 
some kind of physical activity, while simultaneously remains entertaining and amusing. Using the fundamental ways of creating a treasure hunt the users will easily be able to create their own around a city.

### Application Flow

An activity is related with the user interface and is something the users interact with. The 
application consists of four main activities 
- My Treasure Hunts
- Organize a Treasure Hunt
- Find Treasure Hunts
- My Account

The figure below displays the interconnections between the activities in the application
![image](https://github.com/MariliaElia/TreasureHunt/assets/24305018/c79e6b7c-d442-4070-8da5-8d036e9b70ed)


### Database Design 
The data of the application are saved in a local database using Room. Room is an abstraction layer over SQLite, and it allows easy database access while reclaiming the full power of SQLite. 

![image](https://github.com/MariliaElia/TreasureHunt/assets/24305018/4d64cd25-16a7-491b-8692-9d7e0b0e576b)

#### User
This table holds all the information received when the user registers. Username and email are unique values in the table.
#### Treasure Hunt
A Treasure Hunt can be created by the user and it holds all the necessary information asked when the user creates a treasure hunt. A treasure hunt can be identified in the table by its id. It has the userID as a foreign key to be able to associate it with the user who created it.
#### Clue
A Clue is created by the user when creating a treasure hunt and it holds the longitude and latitude, a puzzled instruction and a description of that specific clue. It has treasureHuntID as foreign key to be able to associate the clue with the treasure hunt it was created for. 
#### TH Player
This table saves information about a player and the treasure hunt he is currently playing. The main fields it holds are noOfSuccessfulClues, totalPoints gained for this treasure hunt and status which can be pending, completed or favorited. The treasureHuntID and 
userID are necessary in order to know who is playing and which treasure hunt is played.
#### Player Clue
This table is used to save the status of each clue displayed to the player. It holds the treasureHuntID, clueID, userID and status, either pending, skipped or completed

### Location Permissions
The game progression relies on the user’s location, so an important step is to request location permissions.The ACCESS_FINE_LOCATION permission 
available in Android is the location permission specified for the application. It allows an application to access precise location, in contrast with 
ACCESS_COARSE_LOCATION which allows approximate location.

To be able to provide this service to the user the application had to request periodic updates from the fused location provider, 
which returned the best available location, according to the currently available location providers such as Wi-Fi and GPS.

In order to request for location updates, the application must make a location request. The Location Request specifies the required level of accuracy, power consumption and the desired update interval for location requests.
The priority of the request was set to PRIORITY_HIGH_ACCURACY. PRIORITY_HIGH_ACCURACY requests the most accurate location. The interval for active location updates is set to 5000 milliseconds and the fastest rate at which the application can hold location updates is 3000 milliseconds. The minimum displacement between location updates is set to 10 meters.
