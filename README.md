# UNO Flip 🎴

> A Java Swing implementation of the UNO Flip card game, developed as a
> collaborative software-engineering project for SYSC 3110.

UNO Flip is a complete desktop game with a graphical interface, computer
players, game-state management, card effects, scoring, persistence, and
automated tests. The project demonstrates object-oriented design, MVC
separation, event-driven UI development, and collaborative Git workflows.

## Why This Project Stands Out

- **Complete playable application:** Implements the core game loop, turns,
  scoring, round transitions, and win-state handling.
- **Non-trivial game rules:** Supports two card sides, flip behavior, special
  cards, legal-move validation, and computer-player decisions.
- **Maintainable architecture:** Separates model, view, controller, domain
  objects, and tests rather than placing all behavior in the UI.
- **Persistent state:** Includes save/load and undo support through game-history
  management.
- **Tested behavior:** Includes focused JUnit tests for the model, cards,
  players, piles, AI, flip behavior, and game history.

## Features

- Playable UNO Flip game with light-side and dark-side cards
- Flip mechanic that changes the active side of every card
- Number, skip, reverse, draw, and flip cards
- Player turns, legal-move validation, and round transitions
- Human and computer players
- Score tracking and win-state handling
- Undo and game-history support
- Save and load support for game state
- Card images, custom backgrounds, and image-based game controls
- Unit tests for cards, players, piles, game history, the model, AI, and flip
  behavior

## Technical Highlights

- **Language:** Java
- **UI:** Java Swing
- **Testing:** JUnit
- **Design:** MVC-style separation, object-oriented domain model, event-driven
  controller
- **Tools:** IntelliJ IDEA, Git, GitHub

## Project Structure

| Path | Description |
| --- | --- |
| `src/game/` | Game model, controller, view, cards, players, and game logic |
| `src/tests/` | Automated unit tests |
| `resources/images/` | Card artwork, backgrounds, and button images |
| [`UNO_Project.jar`](./UNO_Project.jar) | Executable final project build |
| [`UML Modeling and Data Structures/`](./UML%20Modeling%20and%20Data%20Structures/) | UML diagrams, sequence diagrams, data-structure documentation, and user manual |

## Architecture

The application separates responsibilities using an MVC-style structure:

- **Model:** `UNOModel`, `Card`, `PileSystem`, `Player`, and related game-state
  classes manage rules and data.
- **View:** `UNOView`, `TopPanel`, and `BackgroundPanel` provide the graphical
  interface and visual game state.
- **Controller:** `UNOController` and `UNOEvent` coordinate user actions and
  updates between the view and model.

## Running the Game

### Prerequisites

- Java installed and available on your `PATH`
- Java version compatible with the provided JAR

### Run the executable JAR

From the project directory, run:

```bash
java -jar UNO_Project.jar
```

The JAR expects the project resources to be available in the project directory.
If card images or other visual resources do not load in IntelliJ, mark the
`resources` folder as **Resources Root** and run the application again.

### Run from IntelliJ IDEA

1. Open the project in IntelliJ IDEA.
2. Mark `resources` as a **Resources Root** if it is not already configured.
3. Run `game.Main`.

## Testing

The test suite is located in `src/tests/` and covers:

- Card creation and card behavior
- Player behavior
- Pile and deck operations
- UNO model rules and game state
- UNO Flip side changes
- Computer-player behavior
- Save/load history management

Run the tests through IntelliJ IDEA using the JUnit test configuration.

## Documentation

Additional project documentation is available in
[`UML Modeling and Data Structures/`](./UML%20Modeling%20and%20Data%20Structures/),
including:

- [UML and sequence diagrams](./UML%20Modeling%20and%20Data%20Structures/)
- [Data structure explanation](./UML%20Modeling%20and%20Data%20Structures/Data%20Structure%20Explanation%20.pdf)
- [UNO user manual](./UML%20Modeling%20and%20Data%20Structures/UNO%20Game%20User%20Manual.pdf)

## Team

| Member | Student number | Selected contributions |
| --- | --- | --- |
| Edward Savin | 101239342 | Card and game features, testing, save/load, and UI support |
| Magdi Hajjaj | 101299841 | Game integration, model/controller/view work, flip behavior, and background switching |
| Mohammed Osman | 101312104 | Game features, testing, and Draw Card button integration |
| Hamza Mohammed Salih | 101311021 | Player-related work and Next Player button integration |

## Repository

The source code and project history are available on GitHub:

<https://github.com/MagdiHajjaj/UNO_FLIP_Project>
