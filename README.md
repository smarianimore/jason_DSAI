# DS_AI_Jason

Jason (AgentSpeak / BDI) multi-agent examples for the *Data Science and Artificial Intelligence* course
(Digital Automation Engineering master's degree, University of Modena and Reggio Emilia).

Course home page: <https://moodle.unimore.it/course/view.php?id=22084>

## Jason in brief

[Jason](https://jason-lang.github.io/) is a Java-based interpreter for an extended version of **AgentSpeak(L)**,
used to program BDI (Belief-Desire-Intention) agents. An agent is written in an `.asl` file as a set of
*beliefs*, *goals* and *plans* (`trigger : context <- body`); agents interact with an *environment* written in
Java and with each other through message passing (`.send`). A MAS is described by a `.mas2j` file
(environment class, agents, source paths).

Documentation:

- Jason home and documentation: <https://jason-lang.github.io/>
- Getting started / tutorials: <https://jason-lang.github.io/doc/>
- Programming manual and API: <https://jason-lang.github.io/api/>
- Source code and releases: <https://github.com/jason-lang/jason>
- Book: *Programming Multi-Agent Systems in AgentSpeak using Jason* (Bordini, Hübner, Wooldridge, Wiley, 2007)

## Repository layout

| Folder | Description |
| --- | --- |
| [vacuumCleaner/](vacuumCleaner) | Four versions of a vacuum-cleaner agent in a 2×2 world |
| [battleOfSexes/](battleOfSexes) | Two agents negotiating an evening activity ("a better battle of the sexes") |

## Requirements

- **Java 21** 
- **Jason 3.3.0** 
- A graphical desktop (both examples open a Swing window)

---

## Example 1: vacuumCleaner

**Scenario.** A vacuum-cleaner robot lives in a 2×2 grid of locations (1 top-left, 2 top-right, 3 bottom-left,
4 bottom-right). All locations start dirty; the robot starts in location 1 and every second there is a 20% chance
that a random location becomes dirty again. The Swing window *Domestic Robot* shows the robot and the dirt.

- Percepts: `pos(1..4)`, `dirty` / `clean`
- Actions: `suck`, `left`, `right`, `up`, `down`

Four agent versions of increasing quality are provided in `src/agt`:

| Agent | Behaviour |
| --- | --- |
| `cleaner1` | Purely reactive; independent plans for dirt and position, may suck in a clean cell |
| `cleaner2` | Context-based reactive plans (`dirty`/`clean`); cleans before moving |
| `cleaner3` | Uses an achievement goal `!move`, separating route logic from cleaning |
| `cleaner4` | Persistent `!clean` maintenance goal, re-adopted on failure |

**Parameters.** None on the command line. The agent to run is selected in
[vacuumCleaner/vacuumCleaner.mas2j](vacuumCleaner/vacuumCleaner.mas2j) (currently `cleaner3`): comment out the
active agent and uncomment another one. Run one agent at a time.

**Install and run.**

```sh
cd vacuumCleaner
# install Java 21 and Jason 3.3.0, with the `jason` launcher on PATH
jason vacuumCleaner.mas2j
```

Stop with `Ctrl+C`. More details in [vacuumCleaner/README.md](vacuumCleaner/README.md).

---

## Example 2: battleOfSexes

**Scenario.** A husband (prefers the *movie*) and a wife (prefers *friends*) negotiate how to spend the evening,
following the protocol of the "A better battle of the sexes" slide (see
`battleOfSexes/AI3 - Multi-Agents Systems.pdf`, slide 41). Each proposes their favourite activity, then they
exchange `offer(Tonight, Later, N)` messages: tonight's activity now, then the other activity for the next `N`
weekends. The husband keeps increasing `N` until he reaches 2 and then accepts ("Deal!"); the wife confirms and
both execute the agreed plan. The dialogue and the resulting activities are shown in a Swing window
(*Battle of the sexes - negotiation*).

Agents: `wife` and `husband` (`src/agt`), environment `example.Env` (`src/env/example/Env.java`) with the
actions `say(From, To, Msg)` and `attend(Weekend, Activity)`.

**Parameters.** None. The negotiation preferences (`preferred/1`, `other/1`) and the 1.5 s pause between messages
are set in the `.asl` files.

**Install and run**:

```sh
cd battleOfSexes
# install Java 21 and Jason 3.3.0, with the `jason` launcher on PATH
jason battleOfSexes.mas2j
```
