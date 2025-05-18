```mermaid
classDiagram
    namespace rest {
        class GreetingsStatsJson {
            -Map~String, Long~ counters
        }
    }

    namespace domain {
        class GreetingsStats {
            -Map~String, Long~ counters
            +Optional~Long~ getStatsFor(String type)
            +GreetingsStats increaseCounterFor(String type)
        }
    }

    namespace persistence {
        class Counter {
            -long id
            -String name
            -long count
        }
    }

    %% Add class annotations
    <<Record>> GreetingsStatsJson
    <<entity>> GreetingsStats
    ```
