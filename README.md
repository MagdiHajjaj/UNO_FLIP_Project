# Group #25 — UNO Flip Project — Milestone #5

**UNO_PROJECT.jar** contains a fully functional playable version of our UNO game for **Milestone #5**.  
Running this file starts the full game with turn handling, scoring, round transitions, and wild card logic.

---

**Feature Selected — Background Images for UNO Flip (Feature 1)** For this milestone, we added custom images to the game interface. The background now switches when a flip card is played, and the Draw Card and Next Player buttons have custom images applied to give the UI a cleaner look. The background changes between the light and dark side depending on the current game mode, making the flip mechanic easier to see during gameplay.

---

## Where the Feature Was Implemented

- A new class BackgroundPanel handles loading and drawing the light and dark background images.

- The panel switches backgrounds using the flip() method when a flip card is played.

- UNOView creates and manages the BackgroundPanel and triggers flips from the controller.

- Custom images were added to the Draw Card button and Next Player button inside UNOView to improve appearance.

- New image files were added to the images folder for both backgrounds and button icons.
---
## Included Files

| File / Folder | Description                                    |
|--------------|------------------------------------------------|
| `UNO_PROJECT.jar` | Executable JAR game build                      |
| `UML Diagram` | Updated class diagram reflecting MVC structure |
| `Sequence Diagrams` | Visual models of gameplay flow events          |
| `Data Structure Explanation` | Explanation of chosen structures and reasoning |
| `User Manual` | Explanation of How to run, play, and use features|

---
## Team Contributions (Milestone #5)

### Edward Savin (101239342)
- Helped test the background flipping and button updates

### Magdi Hajjaj (101299841)
- Implemented dynamic background switching
- Integrated background flipping into the view and controller flow

### Mohammed Osman (101312104)
- Added image to the Draw Card button.

### Hamza Mohammed Salih (101311021)
- Added image to the Next player button.
## Setup Instructions
open a terminal in the parent folder, run java -jar UNO_Project.jar

### Mark Resources Folder (if card images do not appear)
