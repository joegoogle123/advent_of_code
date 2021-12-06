# Advent of Code

This repository has some helper classes to solve __AOC__ problems _quickly_. 
It has the abstract class `AdventOfCodeSolver` which users can extend and implement. 
Then you can run solver in a main method like so
```java
public static void main(String[] args) {
        var runner = AdventOfCodeRunner.getRunner(AdventConstants.YEAR_2021, AdventConstants.DAY1);
        var solver = new YourSolver();
        runner.solve(solver);
    }
```

### Requirements
1. Java 17
2. Maven 3+

### Session Token
Advent of Code uses OAuth to assign custom session tokens to each distinct logged-in user. 
This enables custom inputs and custom answers for each _AOC_ problem. 
`AdventOfCodeRunner` will attempt to download the input files for you if the session token is properly
assigned in:
```
src/main/resources/secrets.json
```
This `session` can be found by searching for cookies on the main website `https://adventofcode.com/` 
You can access your cookies on chrome via:
```
More tools -> Developer Tools -> Application -> Cookies
```
