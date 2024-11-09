Feature: A1_SCENARIO

    Scenario: The A1 Scenario
        Given the A1 Scenario Deck is setup
        When p2 decides to sponsor
        And p2 builds the quest from the slides
        # stage 1
        And all 3 Players participate
        And players act in stage 1
        # stage 2
        And all 3 Players participate
        And players act in stage 2
        # stage 3
        And all 2 Players participate
        And players act in stage 3
        # stage 4
        And all 2 Players participate
        And players act in stage 4
        And the sponsoror trims their hand
        And player 1 draws from the event deck for their turn
        Then player 4 should have 4 shields
        And player 2 should have 12 cards in hand