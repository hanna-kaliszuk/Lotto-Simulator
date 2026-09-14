# Lotto Simulator
![CI](https://github.com/hanna-kaliszuk/Lotto-Simulator/actions/workflows/maven.yml/badge.svg?branch=final-version)

A Java-based simulation of a lottery system, modelling the interaction between players, lottery outlets, the central lottery office, tickets, drawings, prizes, and public finances.

The project focuses on **object-oriented design, domain modelling, exception handling, and automated testing**. It also includes a large-scale simulation demonstrating the complete flow of the system.

---

## What is Lotto Simulator?

The application models a complete lottery system consisting of several cooperating components:

- **Central Office (Centrala)** — coordinates lottery drawings, manages the system's finances, and stores drawing results.
- **Lottery Outlets (Kolektury)** — sell lottery tickets and register transactions.
- **Players (Gracze)** — use different playing strategies and purchase tickets.
- **Tickets (Kupony)** — represent lottery entries and contain one or more bets.
- **Draws (Losowania)** — generate lottery results and determine winning combinations.
- **Public Budget (Budżet Państwa)** — provides subsidies when the Central Office does not have enough funds to pay a prize.
- **Custom exceptions (Wyjątki)** — handle invalid operations and exceptional states.

The system supports several types of players with different behaviours:

- **Random Player (Losowy)** - randomly chooses an outlet and buys a random number of Quick Pick (chybił-trafił) tickets with random numbers of bets and drawings.
- **Minimalist Player (Minimalista)** - always buys one Quick Pick bet for the nearest deawing at the preferred outlet.
- **Fixed-number Player (Stałoliczbowy)** - plays their six preffered numbers for the next ten drawings and buys a new ticket only after all its drawings have been completed.
- **Fixed-slip Player (Stałoblankietowy)** - buys tickets based on their personal slip (`blankiet`) for a fixed number of drawings, rotating between their preffered outlets. 

The project contains a full simulation with **10 lottery outlets, 800 players and 20 consecutive drawings**.

---

## Key Features

- Object-oriented domain model of a lottery system
- Multiple player types with different playing strategies
- Lottery ticket and bet management
- Random lottery drawings
- Prize calculation and redemption
- Central financial management
- Public-budget subsidies when the Central Office cannot cover a prize
- Custom exception handling for invalid operations
- Detailed financial reporting
- Drawing history
- Automated unit tests with JUnit 5
- Full end-to-end simulation of the system

### Central Office

`Centrala` acts as the main coordinator of the lottery system. It manages registered lottery outlets, performs drawings, stores their results, and handles the financial side of prize payments.

### Players

`Gracz` defines the common functionality of players, while specialized classes implement different playing behaviours.

```text
Gracz
├── Losowy
├── Minimalista
├── Stałoliczbowy
└── Stałoblankietowy
```

### Tickets and Bets

Tickets (`Kupon`) contain bets (`Zakład`) and are associated with lottery drawings. Blank slips (`Blankiet`) can be used to define the bets submitted by players.

### Financial System

The financial model separates the funds managed by the Central Office from the Public Budget.

`Kwota` is used to represent monetary values, while `Centrala` and `Budżet Państwa` handle financial operations such as collecting payments, paying prizes, and covering insufficient funds through subsidies.

### Drawings

The drawing subsystem generates lottery results and keeps track of completed drawings through `Losowanie` and `Wynik`.

---

## Simulation

The project includes a dedicated demonstration program, `PrezentacjaDziałania`, which runs a larger simulation of the entire system.

The simulation creates:

- **10 lottery outlets**
- **200 Random Players**
- **200 Minimalist Players**
- **200 Fixed-number Players**
- **200 Fixed-slip Players**
- **20 lottery drawings**

During each drawing cycle:

1. Players purchase tickets.
2. The Central Office performs a lottery drawing.
3. Players' tickets are checked against the result.
4. Winning prizes are redeemed through lottery outlets.
5. The financial state of the system is updated.

After all drawings, the program displays the drawing history, the state of the Public Budget, and the financial state of the Central Office.

---

## Testing

The project uses **JUnit 5** for automated testing.

The test suite covers the main domain components, including:

- ticket and bet creation and validation
- player behaviour
- lottery outlet operations
- lottery drawings and results
- Central Office state and operations
- financial calculations
- Public Budget operations
- invalid input handling
- insufficient funds and subsidies
- state reset and initialization
- exceptional cases

Tests are organized separately from the application code using the standard Maven project structure:

```text
src/
├── main/
│   └── java/
└── test/
    └── java/
```

Run the complete test suite with:

```bash
mvn test
```

---

## Tech Stack

- **Java**
- **Maven**
- **JUnit 5**

---

## Project Structure

```text
Lotto-Simulator/
├── pom.xml
├── README.md
├── .gitignore
│
└── src/
    ├── main/
    │   └── java/
    │       ├── główny/
    │       │   ├── Main.java
    │       │   └── PrezentacjaDziałania.java
    │       │
    │       ├── totolotek/
    │       │   ├── centrala/
    │       │   │   ├── Centrala.java
    │       │   │   └── losowanie/
    │       │   │       ├── Losowanie.java
    │       │   │       └── Wynik.java
    │       │   │
    │       │   ├── finanse/
    │       │   │   ├── BudżetPaństwa.java
    │       │   │   └── Kwota.java
    │       │   │
    │       │   ├── gracze/
    │       │   │   ├── Gracz.java
    │       │   │   ├── Losowy.java
    │       │   │   ├── Minimalista.java
    │       │   │   ├── Stałoblankietowy.java
    │       │   │   └── Stałoliczbowy.java
    │       │   │
    │       │   ├── kolektura/
    │       │   │   └── Kolektura.java
    │       │   │
    │       │   └── kupony/
    │       │       ├── Blankiet.java
    │       │       ├── Kupon.java
    │       │       └── Zakład.java
    │       │
    │       └── wyjątki/
    │           ├── BrakŚrodków.java
    │           ├── MożliwaPróbaOszustwa.java
    │           └── NieprawidłoweDane.java
    │
    └── test/
        └── java/
            └── testy/
                ├── TestBlankietu.java
                ├── TestBudżetPaństwa.java
                ├── TestCentrala.java
                ├── TestGracza.java
                ├── TestKolektury.java
                ├── TestKuponu.java
                ├── TestKwota.java
                ├── TestLosowaniaOrazWyniku.java
                └── ZakładTest.java
```

---

## Running the Project

### Requirements

- **Java 21 or newer**
- **Maven**

### Run tests

```bash
mvn test
```

### Run the simulation

The full system demonstration is implemented in:

```text
główny/PrezentacjaDziałania.java
```

Run its `main` method to start the full simulation.

A smaller demonstration of a single player and a single drawing is available in:

```text
główny/Main.java
```

---

## Project Goals

The main goal of the project was to design a non-trivial object-oriented system with multiple interacting components and clearly separated responsibilities.

Beyond implementing the required functionality, the project was extended with:

- a larger end-to-end simulation,
- a dedicated JUnit test suite,
- Maven-based project management,
- explicit handling of exceptional and financial edge cases.

---

## Academic Context

Originally developed as part of the **Object-Oriented Programing (Programowanie Obiektowe)** course at the University of Warsaw.
