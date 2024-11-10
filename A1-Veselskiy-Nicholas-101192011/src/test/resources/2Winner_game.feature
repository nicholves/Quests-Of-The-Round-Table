Feature: 2_WINNER_SCENARIO

  Scenario: The 2 Winner Scenario
    Given The 2 Winner Scenario Deck is setup
    When Player 1 confirms it is their turn
    And Player 1 draws from the event deck
    And p1 decides to sponsor
    And p1 builds quest one

    # stage 1
    And all 3 Players participate
    And players act in stage 1 of quest one of the 2 winner scenario
    # stage 2
    And all 2 Players participate
    And players act in stage 2 of quest one of the 2 winner scenario
     # stage 3
    And all 2 Players participate
    And players act in stage 3 of quest one of the 2 winner scenario
     # stage 4
    And all 2 Players participate
    And players act in stage 4 of quest one of the 2 winner scenario

    And the sponsoror trims their highest 4 cards
    And Player 1 confirms
    # turn one over

    # turn two
    And Player 2 confirms it is their turn
    And Player 2 draws from the event deck
    And Player 2 confirms
    And Player 2 declines to sponsor
    And Player 3 accepts sponsoring
    And p3 builds quest two

    # stage 1
    And 1 players decline to join attack
    And all 2 Players participate
    And players act in stage 1 of quest two of the 2 winner scenario
    # stage 2
    And all 2 Players participate
    And players act in stage 2 of quest two of the 2 winner scenario
     # stage 3
    And all 2 Players participate
    And players act in stage 3 of quest two of the 2 winner scenario

    And the sponsoror trims their highest 3 cards
    And Player 1 confirms
    # turn two over

    And The game runs

    Then players 2, 4 should have won
    And player 2 should have 7 shields
    And player 4 should have 7 shields
    And player 3 should have 0 shields
    And player 1 should have 0 shields