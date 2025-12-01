# Group #25 — UNO Flip Project — Milestone #3

**UNO_PROJECT.jar** contains a fully functional playable version of our UNO game for **Milestone #3**.  
Running this file starts the full game with turn handling, scoring, round transitions, and wild card logic.

---

**AI** The Game has an option to play versus computer players. The computer players check their hand for cards that are valid to play according to the current game state, including the top card on the discard pile and any active wild color. They then play the first valid card they find. If no card is valid, the computer draws a card instead.

---

## Included Files

| File / Folder | Description |
|--------------|-------------|
| `UNO_PROJECT.jar` | Executable JAR game build |
| `UML Diagram` | Updated class diagram reflecting MVC structure |
| `Sequence Diagrams` | Visual models of gameplay flow events |
| `Data Structure Explanation` | Explanation of chosen structures and reasoning |

---

## Team Contributions (Milestone #3)

### Edward Savin (101239342)
- Updated and expanded test cases
- Supported debugging and rules validation during game refinement

### Magdi Hajjaj (101299841)
- Implemented `UNOEvent` Class
- Implemented Flip rules.
- Helped implement the Ai
- Contributed to controller logic and interaction flow
- Helped align gameplay rules with user interface updates

### Mohammed Osman (101312104)
- Implemented AI
- Added Javadoc documentation across classes

### Hamza Mohammed Salih (101311021)
- Created and adjusted sequence diagrams to reflect final game flow
- Created UML


## Setup Instructions
open a terminal in the parent folder, run java -jar UNO_Project.jar

### Mark Resources Folder (if card images do not appear)
