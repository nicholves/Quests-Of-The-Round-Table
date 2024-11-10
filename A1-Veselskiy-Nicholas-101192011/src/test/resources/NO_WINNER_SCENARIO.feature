Feature: NO_WINNER_SCENARIO

  Scenario: The 0 Winner Scenario
    Given The 0 Winner Scenario Deck is setup
    When p1 decides to sponsor
    And p1 builds the quest with two massive monsters
    # stage 1
    And all Players participate
    And all players discard their largest card
    And Player 2 uses a dagger
    And Player 3 uses a dagger
    And Player 4 uses a dagger
    And all players lose
    And Player 1 discards 4 cards
    And player 1 draws from the event deck for their turn

    Then player 1 should have 0 shields
    And player 2 should have 0 shields
    And player 3 should have 0 shields
    And player 4 should have 0 shields
    And player 1 should have 12 cards in hand
    And player 2 should have 11 cards in hand
    And player 3 should have 11 cards in hand
    And player 4 should have 11 cards in hand
    # player 1 will have 12 cards in hand if he actually drew after the quest
    # nessesarily it will be different since cards used in a quest are immediatly taken from the player in my implementation