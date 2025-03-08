# System Patterns

## Architecture

The application follows a Model-View-ViewModel (MVVM) architecture.

-   **Model:** Represents the game data and logic (e.g., `Card`, `Deck`, `Hand`, `Player`, `Dealer`, `BlackjackGame`).
-   **View:** The UI components that display the game state (e.g., `BlackjackScreen`, `CardGrid`, `CardView`).
-   **ViewModel:** Mediates between the Model and View, exposing data and handling user interactions.

## Key Components

- `BlackjackGame`: Manages the overall game state and rules.
- `Deck`: Represents the deck of cards.
- `Hand`: Represents a player's or dealer's hand.
- `Card`: Represents a single playing card.
- `Player`, `Dealer`: Represent the participants in the game.