# Jason code examples

JADE code examples for the Data Science and Artificial Intelligence course of the Digital Automation Engineering master's degree at the University of Modena and Reggio Emilia (Italy).

Course home page: [https://moodle.unimore.it/course/view.php?id=22084](https://moodle.unimore.it/course/view.php?id=22084)

## Scenario: Vacuum Cleaner

A small multi-agent system (MAS) built with Jason. It demonstrates four
versions of a vacuum-cleaner agent operating in a shared, graphical
four-location environment.

## Dependencies

- **Java 21**. 
- **Jason 3.3.0**, including its command-line launcher. The launcher must be
  available as `jason` on your `PATH`.
- A graphical desktop environment to display the Swing world window.

Jason's launcher loads the MAS configuration, ASL agents, and Java
environment.

## Project structure

```text
.
├── logging.properties           # Jason and Java logging configuration
├── vacuumCleaner.mas2j          # MAS configuration and active agent selection
└── src
    ├── agt
    │   ├── cleaner1.asl         # Reactive agent
    │   ├── cleaner2.asl         # Context-based reactive agent
    │   ├── cleaner3.asl         # Goal-based movement agent
    │   └── cleaner4.asl         # Persistent cleaning-goal agent
    └── env
        └── world
            └── VCWorld.java     # Environment model and Swing GUI
```

## Example agents

All four agents use the same environment perceptions and actions:

- **Perceptions:** `pos(1)` through `pos(4)`, and either `dirty` or `clean`
  for the robot's current location.
- **Actions:** `suck`, `left`, `right`, `up`, and `down`.

The four locations form a 2×2 grid: location 1 is top-left, 2 top-right,
3 bottom-left, and 4 bottom-right. The example route is 1 → 2 → 4 → 3 → 1.

| Agent | Behavior |
| --- | --- |
| `cleaner1` | Reacts separately to dirt and location changes. Its independent plans can move before handling a dirt perception, so it may try to suck in a clean location. |
| `cleaner2` | Uses `dirty`/`clean` as plan contexts and handles cleaning before moving. The route is duplicated between its dirty and clean plans. |
| `cleaner3` | Uses a `!move` achievement goal to keep route logic separate from its cleaning reaction. If an action fails without changing the perceived location, it may stop. |
| `cleaner4` | Repeatedly pursues a `!clean` maintenance goal and re-adopts it if the goal fails, making it more resilient to action failures. |

The selected agent is set in [`vacuumCleaner.mas2j`](vacuumCleaner.mas2j).
The configuration currently enables `cleaner3`; the other agent entries are
commented out. To try another example, comment out `cleaner3` and uncomment
the desired agent. Run one agent at a time for a direct comparison.

## Environment

[`VCWorld.java`](src/env/world/VCWorld.java) implements the Jason environment
and opens a Swing window titled **Domestic Robot**. The world is a 2×2 grid;
all four locations start dirty, and the vacuum starts at location 1.
Actions take about half a second to execute. Every second, the environment
has a 20% chance of making a randomly chosen location dirty. The GUI shows
the robot and dirt in each cell.

The environment does not simulate action failures. A graphical desktop is
required to view the world window.

## Run the MAS

1. Install Java 21 and Jason 3.3.0, and make sure the `jason` launcher is on
   your `PATH`.
2. Start the MAS from the project root:

   ```sh
   jason vacuumCleaner.mas2j
   ```

Jason starts the configured agent and the **Domestic Robot** window. MAS
output is sent to the Jason console according to [`logging.properties`](logging.properties).
Stop the running MAS with `Ctrl+C` in the terminal.
