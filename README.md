# GenesisGuild
Gemini API Developer Competition Application

GenesisGuild is an API for generating multiplayer RPG games in text format, where it is possible to create sessions and interact in a structured way, all done using the Google Gemini API.

## Instructions

### Create a session
To create a session, you need to provide the game genre, a description for the world in which the story will take place, and a description of the mission.

```cmd
POST http://localhost:8080/game/session
```
#### Body Example
```json
{
"game_genre":"fantasy",
"world_description":"Draconia: A realm where dragons are revered as gods and humans live in harmony with them. But an ancient evil stirs, corrupting dragons and plunging the world into chaos. Heroes must unite dragonkind, seeking lost artifacts and facing mythical beasts to restore balance.",
"mission":" infiltrate the shrine, cleanse the corruption by rekindling the Dragonheart, a powerful artifact, and protect a young dragon chosen to become its guardian. Be wary of dragon cultists and their monstrous allies guarding the shrine."
}
```
#### Return Example
```json
{
"session_id": "de24c223-91b3-45bb-8933-6dbcc6394c33",
"game_genre": "fantasy",
"world_description": "Draconia: A realm where dragons are revered as gods and humans live in harmony with them. But an ancient evil stirs, corrupting dragons and plunging the world into chaos. Heroes must unite dragonkind, seeking lost artifacts and facing mythical beasts to restore balance.",
"mission": " infiltrate the shrine, cleanse the corruption by rekindling the Dragonheart, a powerful artifact, and protect a young dragon chosen to become its guardian. Be wary of dragon cultists and their monstrous allies guarding the shrine.",
"start_date": "2024-08-12T19:31:03.0707611",
"players": {},
"contents": {},
"rounds": {}
}
```

-----
### Get session information
Once the session is created, the API provides an ID, which will be necessary for all operations in this session.
Here is an endpoint for querying session data.
```cmd
GET http://localhost:8080/game/session/de24c223-91b3-45bb-8933-6dbcc6394c33
```
#### Return example
```json
{
  "session_id": "de24c223-91b3-45bb-8933-6dbcc6394c33",
  "game_genre": "fantasy",
  "world_description": "Draconia: A realm where dragons are revered as gods and humans live in harmony with them. But an ancient evil stirs, corrupting dragons and plunging the world into chaos. Heroes must unite dragonkind, seeking lost artifacts and facing mythical beasts to restore balance.",
  "mission": " infiltrate the shrine, cleanse the corruption by rekindling the Dragonheart, a powerful artifact, and protect a young dragon chosen to become its guardian. Be wary of dragon cultists and their monstrous allies guarding the shrine.",
  "start_date": "2024-08-12T19:31:03.0707611",
  "players": {},
  "contents": {},
  "rounds": {}
}
```
------

### Add a new player
In this endpoint, we can add players, providing a name and a description of that player.
When creating a new player, the API returns the session information.

```cmd
POST http://localhost:8080/game/session/de24c223-91b3-45bb-8933-6dbcc6394c33/player
```
#### Body example
```json
{
  "name":"Kaelan Drakescale",
  "description":"A young, inexperienced dragon rider with a fiery spirit and a deep connection to their dragon companion"
}
```


------

### Init a session
After all players have been created, it is necessary to start the session, where the instructions will be effectively sent to Gemini to process.

The API returns a description of the round, which contains the relevant information for the current round.

A mission for each player, and 3 action options for each one to follow their adventure.
```cmd
POST http://localhost:8080/game/session/de24c223-91b3-45bb-8933-6dbcc6394c33/init
```

#### Return example
```json
{
  "players": [
    {
      "behavior_options": [
        "Charge into battle, calling upon Torvald to engage the guards head-on.",
        "Attempt to communicate with the guards, offering a distraction or a false promise.",
        "Focus your energy on the barrier, searching for a way to bypass it."
      ],
      "mission": "Use your connection to Torvald to distract the guards while you subtly slip past the gates. You must find a way to disable the magical barrier that surrounds the shrine, likely a glyph or a control panel hidden somewhere.",
      "name": "Kaelan Drakescale"
    },
    {
      "behavior_options": [
        "Charge into the fray, focusing on overwhelming the guards with brute force.",
        "Use your axe to break through the magical barriers surrounding the shrine, creating a path for your allies.",
        "Attempt to parry the guards' magical attacks and hold them back until Kaelan can disable the barrier."
      ],
      "mission": "Engage the guards in a brutal display of strength. Your primary objective is to buy time for Kaelan and Elara to infiltrate the shrine, but be wary of their dark magic. Protect your allies and Aethel.",
      "name": "Torvald Stonefist"
    },
    {
      "behavior_options": [
        "Utilize your archery skills to take down the guards from afar, focusing on precise shots to avoid attracting unwanted attention.",
        "Use your knowledge of arcane magic to create a distraction or a temporary escape route for your companions.",
        "Search for any hidden entrances or weak points in the shrine's walls, perhaps a forgotten passageway or a broken seal."
      ],
      "mission": "Maintain stealth and scout the immediate area for weaknesses in the shrine's defenses. Your knowledge of ancient runes and arcane magic could be key to finding a way inside.",
      "name": "Elara Nightshade"
    }
  ],
  "round_description": "The air hums with an unsettling energy as you approach the grand, obsidian gates of the Dragonheart Shrine. The once pristine marble has been stained black, tendrils of dark magic oozing from the cracks. Shadows writhe around the entrance, and the ground seems to pulse with an unnatural heartbeat. Two hulking figures, cloaked in crimson robes emblazoned with a dragon skull, stand guard, their eyes glowing with malevolent light.  You can hear the young dragon, Aethel, whimpering from within the shrine, its roars muffled by the oppressive magic.",
  "round_number": 0,
  "message": ""
}
```

------

### Play with a character
Now, each player must choose their action for this round, and so on. Once everyone has been chosen, the next round begins

The API return has a new description, containing the effects of the previous actions, new missions for each player, and new action options for each one

```cmd
POST http://localhost:8080/game/session/de24c223-91b3-45bb-8933-6dbcc6394c33/play
```
#### Body example
```json
{
  "name":"Kaelan Drakescale",
  "action":"Focus your energy on the barrier, searching for a way to bypass it."
}
```

------

### Remove a Player
You can remove a player 

```cmd
DELETE http://localhost:8080/game/session/eaf408a5-8f47-4e4a-94ed-5b45a281a4da/player
```
#### Body example
```json
{
  "name":"Kaelan Drakescale",
  "action":"Focus your energy on the barrier, searching for a way to bypass it."
}
```

------

### Delete a Session
You can remove a session

```cmd
DELETE http://localhost:8080/game/session/6071868e-241d-4c82-b5e6-3d0d3f9c7dc5
```

------