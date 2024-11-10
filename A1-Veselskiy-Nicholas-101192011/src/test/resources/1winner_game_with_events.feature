Feature: 1_WINNER_SCENARIO_WITH_EVENTS

  Scenario: The 1 Winner Scenario With Events
    Given The 1 Winner Scenario With Events Deck is setup
    When Player 1 confirms it is their turn
    And Player 1 draws from the event deck
    And p1 decides to sponsor
    And p1 builds quest one

    # stage 1
    And all 3 Players participate
    And players act in stage 1 of quest one
     # stage 2
    And all 3 Players participate
    And players act in stage 2 of quest one
     # stage 3
    And all 3 Players participate
    And players act in stage 3 of quest one
     # stage 4
    And all 3 Players participate
    And players act in stage 4 of quest one

    And the sponsoror trims their highest 4 cards
    And Player 1 confirms
    # turn one over

    # turn two
    And Player 2 confirms it is their turn
    And Player 2 draws from the event deck
    And Player 2 confirms
    And Player 2 confirms
    # turn two over

    # turn three
    And Player 3 confirms it is their turn
    And Player 3 draws from the event deck
    And Player 3 confirms
    And players handle the prosperity event
    And Player 3 confirms
    # turn three over

    # turn four
    And Player 4 confirms it is their turn
    And Player 4 draws from the event deck
    And Player 4 confirms
    And players handle the queen's favor event
    And Player 4 confirms
    # turn four over

    # turn five (p1's second turn)
    And Player 1 confirms it is their turn
    And Player 1 draws from the event deck
    And p1 decides to sponsor
    And p1 builds quest two

    # stage 1
    And all 3 Players participate
    And players act in stage 1 of quest two
     # stage 2
    And all 2 Players participate
    And players act in stage 2 of quest two
     # stage 3
    And all 2 Players participate
    And players act in stage 3 of quest two

    And the sponsoror trims their highest 4 cards
    # end turn five

    And The game runs

    Then player 3 should have won
    And player 3 should have 7 shields
    And player 4 should have 4 shields
    And player 2 should have 5 shields
    And player 1 should have 0 shields